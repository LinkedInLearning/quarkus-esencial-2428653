package com.kineteco.health;

import com.kineteco.SalesResource;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.inject.Inject;

@Liveness
public class PingSalesResourceHealthCheck implements HealthCheck {

   @Inject
   SalesResource salesResource;

   @Override
   public HealthCheckResponse call() {
      String response = salesResource.health();
      return HealthCheckResponse.named("Ping Sales Service")
            .withData("Response", response)
            .up()
            .build();
   }
}
