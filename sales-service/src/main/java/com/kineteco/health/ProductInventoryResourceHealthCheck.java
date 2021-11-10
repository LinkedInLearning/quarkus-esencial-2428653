package com.kineteco.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductInventoryResourceHealthCheck implements HealthCheck {

   @Override
   public HealthCheckResponse call() {
      return HealthCheckResponse
            .named("ProductInventoryCheck")
            .up()
            .build();
   }
}
