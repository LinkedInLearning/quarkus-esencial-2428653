package com.kineteco.health;

import com.kineteco.client.ProductInventoryServiceClient;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

@Readiness
public class ProductInventoryResourceHealthCheck implements HealthCheck {

   @Inject
   @RestClient
   ProductInventoryServiceClient productInventoryServiceClient;

   @Override
   public HealthCheckResponse call() {
      long size = 0;

      try {
         size = productInventoryServiceClient.size();
      } catch (WebApplicationException ex) {
         if (ex.getResponse().getStatus() >= 500) {
            return HealthCheckResponse
                  .named("ProductInventoryCheck")
                  .withData("err", ex.getMessage())
                  .down()
                  .build();
         }
      }
      return HealthCheckResponse
            .named("ProductInventoryCheck")
            .withData("size", size)
            .up()
            .build();
   }
}
