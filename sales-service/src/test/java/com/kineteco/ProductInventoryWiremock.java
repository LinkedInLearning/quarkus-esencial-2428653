package com.kineteco;

import com.github.tomakehurst.wiremock.WireMockServer;
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
      stubOk();
      stubTimeout();
      return Collections.singletonMap("kineteco-product-inventory/mp-rest/url", wireMockServer.baseUrl());
   }

   private void stubOk() {
      stubFor(get(urlEqualTo("/products/123/stock"))
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withBody("42")
            ));

      stubFor(get(urlEqualTo("/products/456/stock"))
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withBody("2")
            ));
   }

   static void stubTimeout() {
      stubFor(get(urlEqualTo("/products/falloTimeout/stock"))
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withFixedDelay(150)
                  .withBody("42")
            ));
   }

   @Override
   public void stop() {
      if (wireMockServer != null) {
         wireMockServer.stop();
      }
   }
}
