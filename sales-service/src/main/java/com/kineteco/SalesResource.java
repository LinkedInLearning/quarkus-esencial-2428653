package com.kineteco;

import com.kineteco.api.ProductInventoryService;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/sales")
public class SalesResource {

    @Inject
    @RestClient
    ProductInventoryService productInventoryService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String health() {
        return "Sales Service is up!!";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{sku}/availability")
    public Boolean available(@PathParam("sku") String sku, @QueryParam("units") Integer units) {
       return productInventoryService.getStock(sku) >= units;
    }
}