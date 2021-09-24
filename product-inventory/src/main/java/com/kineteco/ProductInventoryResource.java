package com.kineteco;

import com.kineteco.config.ProductInventoryConfig;
import com.kineteco.model.ProductInventory;
import com.kineteco.service.ProductInventoryService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collection;

@Path("/products")
public class ProductInventoryResource {

    @Inject
    ProductInventoryService productInventoryService;

    @Inject
    ProductInventoryConfig productInventoryConfig;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/health")
    public String health() {
        return productInventoryConfig.greetingMessage();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<ProductInventory> listInventory() {
        return productInventoryService.listInventory();
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProduct(ProductInventory productInventory) {
        productInventoryService.addProductInventory(productInventory);
        return Response.created(URI.create(productInventory.getSku())).build();
    }

//    @PUT
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response createProduct(int stock) {
//        productInventoryService.addProductInventory(productInventory);
//        return Response.created(URI.create(productInventory.getSku())).build();
//    }

//    @DELETE
}
