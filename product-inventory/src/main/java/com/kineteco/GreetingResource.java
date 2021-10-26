package com.kineteco;

import com.kineteco.model.ProductInventory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/123")
    public ProductInventory inventory() {
        ProductInventory productInventory = new ProductInventory();
        productInventory.setSku("123");
        return productInventory;
    }
}
