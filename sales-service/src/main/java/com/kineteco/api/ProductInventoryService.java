package com.kineteco.api;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.TimeoutException;

@Path("/products")
@RegisterRestClient(configKey = "kineteco-product-inventory")
@Produces(MediaType.APPLICATION_JSON)
public interface ProductInventoryService {

   @GET
   @Path("/{sku}/stock")
   @Timeout(value = 100)
   @Retry(retryOn = TimeoutException.class, delay = 100, jitter = 25)
   Integer getStock(@PathParam("sku") String sku);
}
