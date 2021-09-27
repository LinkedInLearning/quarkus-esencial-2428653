package com.kineteco.api;

import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/products")
@RegisterRestClient(configKey = "kineteco-product-inventory")
@Produces(MediaType.APPLICATION_JSON)
@Timeout(value = 100)
public interface ProductInventoryService {

   @GET
   @Path("/{sku}/stock")
   Integer getStock(@PathParam("sku") String sku);
}
