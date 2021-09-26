package com.kineteco;

import com.kineteco.config.ProductInventoryConfig;
import com.kineteco.model.ConsumerType;
import com.kineteco.model.ProductInventory;
import com.kineteco.model.ProductLine;
import com.kineteco.model.ValidationGroups;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
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
import java.util.Collection;
import java.util.stream.Collectors;

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
    public String health() {
        LOGGER.debug("health called");
        return productInventoryConfig.greetingMessage();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listInventory() {
        LOGGER.debug("Product inventory list");
        return Response.ok(ProductInventory.<ProductInventory>streamAll()
              .filter(pi -> pi.targetConsumer.contains(ConsumerType.CORPORATE)).collect(Collectors.toList())).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/line/{productLine}")
    public Response economyProductsCount(@PathParam("productLine") ProductLine productLine) {
        LOGGER.debug("Economy products");
        return Response.ok(ProductInventory.count("productLine", productLine)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sku}")
    public Response inventory(@PathParam("sku") String sku) {
        LOGGER.debugf("get by sku %s", sku);
        ProductInventory productInventory = ProductInventory.findById(sku);
        if (productInventory == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!productInventoryConfig.retrieveFullCatalog() && !productInventory.targetConsumer.contains(ConsumerType.CORPORATE)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(productInventory).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createProduct(@Valid @ConvertGroup(to = ValidationGroups.Post.class) ProductInventory productInventory) {
        LOGGER.debugf("create %s", productInventory);
        productInventory.persist();
        return Response.created(URI.create(productInventory.sku)).build();
    }

    @PUT
    @Path("/{sku}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateProduct(@PathParam("sku") String sku, @Valid @ConvertGroup(to = ValidationGroups.Put.class) ProductInventory productInventory) {
        productInventory.sku = sku;
        LOGGER.debugf("update %s", productInventory);
        ProductInventory bySku = ProductInventory.findById(productInventory.sku);
        if (bySku != null) {
            bySku.name = productInventory.name;
            bySku.category = productInventory.category;
            // all the properties that should be modifiable

            bySku.persist();
        }
        return Response.accepted(URI.create(productInventory.sku)).build();
    }

    @PATCH
    @Path("/{sku}")
    @Operation(summary = "Update the stock of a product by sku.", description = "Longer description that explains all.")
    @Transactional
    public Response updateStock(@PathParam("sku") String sku, @QueryParam("stock") Integer stock) {
        LOGGER.debugf("get by sku %s", sku);
        ProductInventory productInventory = ProductInventory.findById(sku);

        if (productInventory == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        productInventory.unitsAvailable = productInventory.unitsAvailable + stock;
        productInventory.persist();
        return Response.accepted(URI.create(productInventory.sku)).build();
    }

    @DELETE
    @Path("/{sku}")
    @Transactional
    public Response delete(@PathParam("sku") String sku) {
        LOGGER.debugf("delete by sku %s", sku);
        ProductInventory.deleteById(sku);
        return Response.accepted().build();
    }

}
