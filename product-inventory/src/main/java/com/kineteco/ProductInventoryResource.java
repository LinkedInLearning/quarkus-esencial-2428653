package com.kineteco;

import com.kineteco.config.ProductInventoryConfig;
import com.kineteco.model.ProductInventory;
import com.kineteco.model.ProductLine;
import com.kineteco.model.ValidationGroups;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;

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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/products")
public class ProductInventoryResource {
    private static final Logger LOGGER = Logger.getLogger(ProductInventoryResource.class);

    @Inject
    ProductInventoryConfig productInventoryConfig;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/health")
    @NonBlocking
    public String health() {
        LOGGER.debug("health called");
        return productInventoryConfig.greetingMessage();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<ProductInventory> listInventory(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {
        LOGGER.debug("Product inventory list");
        Uni<List<ProductInventory>> fullInventory;
        if (page == null && size == null) {
            fullInventory = ProductInventory.listAll();
        } else {
            PanacheQuery<ProductInventory> query = ProductInventory.findAll(Sort.by("name"));
            fullInventory = query.page(page, size).list();
        }
        return ProductInventory.st
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sku}")
    public Uni<ProductInventory> inventory(@PathParam("sku") String sku) {
        LOGGER.debugf("get by sku %s", sku);
        return ProductInventory.findBySku(sku);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sku}/stock")
    public Uni<Integer> getStock(@PathParam("sku") String sku) {
        LOGGER.debugf("getStock by sku %s", sku);
        return ProductInventory.findCurrentStock(sku);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createProduct(@Valid @ConvertGroup(to = ValidationGroups.Post.class) ProductInventory productInventory) {
        LOGGER.debugf("create %s", productInventory);
        return Panache.<ProductInventory>withTransaction(productInventory::persist)
              .onItem().ifNotNull().transform(e -> Response.created(URI.create(productInventory.sku)).build());
    }

    @PUT
    @Path("/{sku}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> updateProduct(@PathParam("sku") String sku, @ConvertGroup(to = ValidationGroups.Put.class)  @Valid ProductInventory productInventory) {
        productInventory.sku = sku;
        LOGGER.debugf("update %s", productInventory);

        return Panache.withTransaction(() -> {
            Uni<ProductInventory> bySku = ProductInventory.findBySku(productInventory.sku);
            return bySku.onItem().ifNotNull().invoke(e -> {
                e.name = productInventory.name;
                e.category = productInventory.category;
            });
        }) .onItem().ifNotNull().transform(e -> Response.accepted(e).build())
           .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{sku}")
    public Uni<Response> delete(@PathParam("sku") String sku) {
        LOGGER.debugf("delete by sku %s", sku);
        return Panache.withTransaction(() -> ProductInventory.delete("sku", sku))
              .map(d -> d > 0 ? Response.accepted().build() : Response.status(Response.Status.NOT_FOUND).build());
    }


    @PATCH
    @Path("/{sku}")
    @Operation(summary = "Update the stock of a product by sku.", description = "Longer description that explains all.")
    public Uni<Response> updateStock(@PathParam("sku") String sku, @QueryParam("stock") Integer stock) {
        return Panache.withTransaction(() -> {
            Uni<ProductInventory> bySku = ProductInventory.findBySku(sku);
            return bySku.onItem().ifNotNull().invoke(e -> {
                e.unitsAvailable = e.unitsAvailable + stock;
            });
        }) .onItem().ifNotNull().transform(e -> Response.accepted(e).build())
              .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/line/{productLine}")
    public Uni<Long> productsCount(@PathParam("productLine") ProductLine productLine) {
        LOGGER.debug("Count productLines");
        return ProductInventory.count("productLine", productLine);
    }
}
