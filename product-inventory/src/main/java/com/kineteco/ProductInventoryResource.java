package com.kineteco;

import com.kineteco.config.ProductInventoryConfig;
import com.kineteco.service.ProductInventoryService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/products")
public class ProductInventoryResource {

    @Inject
    ProductInventoryService productInventoryService;

    @Inject
    ProductInventoryConfig productInventoryConfig;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return productInventoryConfig.message();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/KE180")
    public Response inventory() {
        return Response.ok(productInventoryService.getBySku("KE180")).build();
    }
}
