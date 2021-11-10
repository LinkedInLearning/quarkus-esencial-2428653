package com.kineteco.health;

import com.kineteco.ProductInventoryResource;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Liveness
public class PingProductInventoryResourceHealthCheck implements HealthCheck {
   @Inject
   ProductInventoryResource productInventoryResource;

   @Override
   public HealthCheckResponse call() {
      String response = productInventoryResource.health();
      return HealthCheckResponse
            .named("Ping Product Inventory Service")
            .withData("Response", response)
            .up()
            .build();
   }
}
