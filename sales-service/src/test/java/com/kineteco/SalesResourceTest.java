package com.kineteco;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@QuarkusTestResource(ProductInventoryWiremock.class)
public class SalesResourceTest {

   @Test
   public void testHealthEndpoint() {
      given()
            .when().get("/sales")
            .then()
            .statusCode(200)
            .body(is("Sales Service is up!!"));
   }

   @Test
   public void testAvailability() {
      given()
            .queryParam("units", 30)
            .when().get("/sales/{sku}/availability", "123")
            .then()
            .statusCode(200)
            .body(is("true"));

      given()
            .queryParam("units", 20)
            .when().get("/sales/{sku}/availability", "456")
            .then()
            .statusCode(200)
            .body(is("false"));
   }

   @Test
   public void testAvailabilityTimeout() {
      given()
            .queryParam("units", 42)
            .when().get("/sales/{sku}/availability", "falloTimeout")
            .then()
            .statusCode(Response.Status.GATEWAY_TIMEOUT.getStatusCode());
   }

   @Test
   public void testAvailabilityRetry() {
      given()
            .queryParam("units", 42)
            .when().get("/sales/{sku}/availability", "falloRetry")
            .then()
            .statusCode(200)
            .body(is("true"));;
   }



   @Test
   public void testAvailabilityFallbackTimeoutLessThanTwoUnits() {
      given()
            .queryParam("units", 2)
            .when().get("/sales/{sku}/availability", "fallback_1")
            .then()
            .statusCode(200)
            .body(is("true"));;
   }

   @Test
   public void testAvailabilityFallbackHandler() {
      given()
            .queryParam("units", 2)
            .when().get("/sales/{sku}/availability", "fallback_2")
            .then()
            .statusCode(Response.Status.BAD_GATEWAY.getStatusCode());
   }
}