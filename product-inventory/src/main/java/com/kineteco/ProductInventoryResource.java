package com.kineteco;

import com.kineteco.config.ProductInventoryConfig;
import com.kineteco.model.ProductInventory;
import com.kineteco.service.ProductInventoryService;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/products")
public class ProductInventoryResource {
    private static final Logger LOGGER = Logger.getLogger(ProductInventoryResource.class);

    @Inject
    ProductInventoryService productInventoryService;

    @Inject
    ProductInventoryConfig productInventoryConfig;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/health")
    public String health() {
        LOGGER.debug("health called");
        return productInventoryConfig.greetingMessage();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("KE180")
    public Response inventory(String sku) {
        LOGGER.debugf("get by sku %s", sku);
        ProductInventory productInventory = productInventoryService.getBySku(sku);
        return Response.ok(productInventory).build();
    }
}
