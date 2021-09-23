package com.kineteco;

import com.kineteco.model.ProductInventory;
import com.kineteco.service.ProductInventoryService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/products")
public class ProductInventoryResource {

    @Inject
    ProductInventoryService productInventoryService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Product Inventory Service is up!";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sku}")
    public Response inventory(@PathParam("sku") String sku) {
        ProductInventory productInventory = productInventoryService.getBySku(sku);

        if (productInventory == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(productInventory).build();
    }
}
