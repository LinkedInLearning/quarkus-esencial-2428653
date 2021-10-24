package com.kineteco;

import com.kineteco.config.ProductInventoryConfig;
import com.kineteco.model.ConsumerType;
import com.kineteco.model.ProductInventory;
import com.kineteco.model.ProductLine;
import com.kineteco.model.ValidationGroups;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
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

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("Product Inventory Service is starting Powered by Quarkus");
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("Product Inventory Service shutting down..");
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/health")
    @NonBlocking
    public String health() {
        LOGGER.debug("health called");
        Boolean.parseBoolean(null);
        return productInventoryConfig.greetingMessage();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<ProductInventory>> listInventory() {
        LOGGER.debug("Product inventory list");
        return ProductInventory.listAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sku}")
    public Uni<Response> inventory(@PathParam("sku") String sku) {
        LOGGER.debugf("get by sku %s", sku);
        Uni<ProductInventory> productInventory = ProductInventory.findById(sku);
        return productInventory
              .onItem().ifNotNull().transform(e -> Response.ok(e).build())
              .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build());
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
    public Uni<Response> updateProduct(@PathParam("sku") String sku, @Valid @ConvertGroup(to = ValidationGroups.Put.class) ProductInventory productInventory) {
        productInventory.sku = sku;
        LOGGER.debugf("update %s", productInventory);

        return Panache.withTransaction(() -> {
            Uni<ProductInventory> bySku = ProductInventory.findById(productInventory.sku);
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
        return Panache.withTransaction(() -> ProductInventory.deleteById(sku))
              .map(d -> d ? Response.accepted().build() : Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/line/{productLine}")
    public Uni<Response> economyProductsCount(@PathParam("productLine") ProductLine productLine) {
        LOGGER.debug("Economy products");
        return ProductInventory.count("productLine", productLine)
              .onItem().ifNotNull().transform(e -> Response.ok(e).build())
              .onItem().ifNull().continueWith(Response.ok(0).build());
    }

    @PATCH
    @Path("/{sku}")
    @Operation(summary = "Update the stock of a product by sku.", description = "Longer description that explains all.")
    public Uni<Response> updateStock(@PathParam("sku") String sku, @QueryParam("stock") Integer stock) {
        return Panache.withTransaction(() -> {
            Uni<ProductInventory> bySku = ProductInventory.findById(sku);
            return bySku.onItem().ifNotNull().invoke(e -> {
                e.unitsAvailable = e.unitsAvailable + stock;
            });
        }) .onItem().ifNotNull().transform(e -> Response.accepted(e).build())
           .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sku}/stock")
    public Uni<Response> stock(@PathParam("sku") String sku) {
        Uni<ProductInventory> productInventory = ProductInventory.findById(sku);
        return productInventory
              .onItem().ifNotNull().transform(e -> Response.ok(e.unitsAvailable).build())
              .onItem().ifNull().continueWith(Response.ok(0).build());
    }

}
