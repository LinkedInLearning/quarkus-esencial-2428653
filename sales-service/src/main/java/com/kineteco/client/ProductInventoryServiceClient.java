package com.kineteco.client;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/products")
@RegisterRestClient(configKey = "kineteco-product-inventory")
@Produces(MediaType.APPLICATION_JSON)
@ClientHeaderParam(name = "caller-header", value = "sales-service")
@RegisterClientHeaders
public interface ProductInventoryServiceClient {

   @GET
   @Path("/{sku}/stock")
   @ClientHeaderParam(name = "method-header", value = "stock")
   Integer getStock(@PathParam("sku") String sku);

   @GET
   @Path("/{sku}")
   Product inventory(@PathParam("sku") String sku);
}
