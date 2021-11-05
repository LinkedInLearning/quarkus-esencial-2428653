package com.kineteco;

import com.kineteco.config.ProductInventoryConfig;
import com.kineteco.model.ProductInventory;
import com.kineteco.model.ProductLine;
import com.kineteco.model.ValidationGroups;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.panache.common.Sort;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestQuery;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@ApplicationScoped
@Path("/products")
public class ProductInventoryResource {
    private static final Logger LOGGER = Logger.getLogger(ProductInventoryResource.class);

    @Inject
    ProductInventoryConfig productInventoryConfig;

    @Inject
    ManufactureOrderEmitter manufactureOrderEmitter;

    @GET
    @Produces(TEXT_PLAIN)
    @Path("/health")
    @NonBlocking
    public String health() {
        LOGGER.debug("health called");
        return productInventoryConfig.greetingMessage();
    }

    @Operation(summary = "Returns the Product Inventory")
    @GET
    @Produces(APPLICATION_JSON)
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ProductInventory.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No products")
    public Uni<List<ProductInventory>> listInventory(@RestQuery("page") Integer page,
                                                     @RestQuery("size") Integer size) {
        LOGGER.debug("Product inventory list");
        Uni<List<ProductInventory>> fullInventory;
        if (page == null && size == null) {
            fullInventory = ProductInventory.findAll(Sort.by("name")).list();
        } else {
            PanacheQuery<ProductInventory> query = ProductInventory.findAll(Sort.by("name"));
            fullInventory = query.page(page, size).list();
        }
        return fullInventory;
    }

    @Operation(summary = "Returns a product")
    @GET
    @Produces(APPLICATION_JSON)
    @Path("/{sku}")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ProductInventory.class, type = SchemaType.OBJECT)))
    @APIResponse(responseCode = "404", description = "No product")
    public Uni<ProductInventory> inventory(String sku) {
        LOGGER.debugf("get by sku %s", sku);
        return ProductInventory.findBySku(sku);
    }

    @Operation(summary = "Returns the product stock")
    @GET
    @Produces(APPLICATION_JSON)
    @Path("/{sku}/stock")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(type = SchemaType.INTEGER)))
    public Uni<Integer> getStock(String sku) {
        LOGGER.debugf("getStock by sku %s", sku);
        return ProductInventory.findCurrentStock(sku);
    }

    @Operation(summary = "Creates a valid product inventory")
    @POST
    @Consumes(APPLICATION_JSON)
    @APIResponse(responseCode = "201", description = "The URI of the created product", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
    @ReactiveTransactional
    public Uni<Response> createProduct(@Valid @ConvertGroup(to = ValidationGroups.Post.class) ProductInventory productInventory,
                                       @Context UriInfo uriInfo) {
        LOGGER.debugf("create %s", productInventory);
       return productInventory.<ProductInventory>persist()
              .map(p -> {
                  UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(p.sku);
                  LOGGER.debugf("New product created with sku %s", p.sku);
                  return Response.created(builder.build()).build();
              });
    }

    @Operation(summary = "Updates an product inventory")
    @PUT
    @Path("/{sku}")
    @Consumes(APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "The updated product", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ProductInventory.class)))
    @ReactiveTransactional
    public Uni<Response> updateProduct(String sku, @ConvertGroup(to = ValidationGroups.Put.class)  @Valid ProductInventory productInventory) {
        return ProductInventory.findBySku(sku)
              .map(retrieved -> {
                  retrieved.name = productInventory.name;
                  retrieved.category = productInventory.category;
                  return retrieved;
              })
              .map(p -> {
                  LOGGER.debugf("Product updated with new valued %s", p);
                  return Response.ok(p).build();
              });
    }

    @Operation(summary = "Deletes an exiting product inventory")
    @DELETE
    @Path("/{sku}")
    @APIResponse(responseCode = "204")
    @APIResponse(responseCode = "404", description = "No product")
    @ReactiveTransactional
    public Uni<Response> delete(String sku) {
        LOGGER.debugf("delete by sku %s", sku);
        return ProductInventory.delete("sku", sku)
              .invoke(() -> LOGGER.debugf("deleted with sku %s", sku))
              .onItem().transform(d -> d > 0 ?  Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build());
    }

    @Operation(summary = "Updates the stock of an existing product by incrementing or decrementing it.")
    @PATCH
    @Path("/{sku}")
    @APIResponse(responseCode = "202")
    @APIResponse(responseCode = "404", description = "No product")
    @ReactiveTransactional
    public Uni<Response> updateStock(String sku, @RestQuery("stock") Integer stock) {
        return ProductInventory.findCurrentStock(sku)
              .onItem().call(currentStock -> {
                  LOGGER.debugf("update stock for sku %s with current stock %d with %d", sku, currentStock, stock);
                  int newStock = currentStock + stock;
                  if (newStock < 0) {
                      int quantity = newStock*-1;
                      manufactureOrderEmitter.sendManufactureOrder(sku, quantity);
                      newStock = 0;
                  }
                  return ProductInventory.update("unitsAvailable = ?1 where sku= ?2", newStock, sku);
              })
              .onItem().transform(u -> Response.accepted().build());
    }


    @Operation(summary = "Updates the stock of an existing product")
    @GET
    @Produces(TEXT_PLAIN)
    @Path("/line/{productLine}")
    @APIResponse(responseCode = "202", description = "The updated product", content = @Content(mediaType = TEXT_PLAIN, schema = @Schema(type = SchemaType.NUMBER )))
    public Uni<Long> productsCount(ProductLine productLine) {
        LOGGER.debug("Count productLines");
        return ProductInventory.count("productLine", productLine);
    }
}
