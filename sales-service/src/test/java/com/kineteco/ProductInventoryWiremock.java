package com.kineteco;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class ProductInventoryWiremock implements QuarkusTestResourceLifecycleManager {
  private WireMockServer wireMockServer;

  @Override
  public Map<String, String> start() {
    wireMockServer = new WireMockServer();
    wireMockServer.start();

     stubStock();
     stubTimeoutStock();
     stubGetProducts();
     stubCircuitBreakerGetProducts();

     return Collections.singletonMap("kineteco-product-inventory/mp-rest/url", wireMockServer.baseUrl());
  }

   private void stubCircuitBreakerGetProducts() {
      // Circuit breaker scenario
      stubFor(get(urlEqualTo("/products/circuitBreaker"))
            .inScenario("circuitBreaker")
            .whenScenarioStateIs(Scenario.STARTED)
            .willSetStateTo("timeout-1")
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withBody("{\"sku\": \"circuitBreaker\", \"productLine\": \"DELUXE\"}")
            ));

      // Da un timeout 1 vez
      stubFor(get(urlEqualTo("/products/circuitBreaker"))
            .inScenario("circuitBreaker")
            .whenScenarioStateIs("timeout-1")
            .willSetStateTo("timeout-2")
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withFixedDelay(2000)
            ));

      // Da un timeout 2 vez
      stubFor(get(urlEqualTo("/products/circuitBreaker"))
            .inScenario("circuitBreaker")
            .whenScenarioStateIs("timeout-2")
            .willSetStateTo("success")
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withFixedDelay(2000)
            ));

      stubFor(get(urlEqualTo("/products/circuitBreaker"))
            .inScenario("circuitBreaker")
            .whenScenarioStateIs("success")
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withBody("{\"sku\": \"circuitBreaker\", \"productLine\": \"DELUXE\"}")
            ));
   }

   private void stubGetProducts() {
      stubFor(get(urlEqualTo("/products/deluxe"))
           .willReturn(aResponse()
                 .withHeader("Content-Type", "application/json")
                 .withBody("{\"sku\": \"deluxe\", \"productLine\": \"DELUXE\"}")
           ));

      stubFor(get(urlEqualTo("/products/economy"))
            .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"sku\": \"economy\", \"productLine\": \"ECONOMY\"}")
            ));
   }

   private void stubTimeoutStock() {
      stubFor(get(urlEqualTo("/products/falloTimeout/stock"))
           .willReturn(aResponse()
                 .withHeader("Content-Type", "application/json")
                 .withFixedDelay(2000)
                 .withBody("42")
           ));
   }

   private void stubStock() {
      stubFor(get(urlEqualTo("/products/123/stock"))
          .willReturn(aResponse()
              .withHeader("Content-Type", "application/json")
              .withBody("42")
          ));
   }

   @Override
  public void stop() {
    if (null != wireMockServer) {
      wireMockServer.stop();
    }
  }
}
