package com.kineteco;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

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
            .queryParam("units", 43)
            .when().get("/sales/{sku}/availability", "123")
            .then()
            .statusCode(200)
            .body(is("false"));
   }

   @Test
   public void testAvailabilityTimeout() {
      given()
            .queryParam("units", 6)
            .when().get("/sales/{sku}/availability", "falloTimeout")
            .then()
            .statusCode(200)
            .body(is("false"));

      given()
            .queryParam("units", 5)
            .when().get("/sales/{sku}/availability", "falloTimeout")
            .then()
            .statusCode(200)
            .body(is("true"));
   }

   @Test
   public void testDeluxeCommand() {
      given()
            .body("{\"sku\": \"deluxe\", \"customerId\": \"customer123\", \"units\": 50}")
            .contentType(ContentType.JSON)
            .when()
            .post("/sales")
            .then()
            .statusCode(201);

      given()
            .body("{\"sku\": \"economy\", \"customerId\": \"customer123\", \"units\": 50}")
            .contentType(ContentType.JSON)
            .when()
            .post("/sales")
            .then()
            .statusCode(400);
   }

   @Test
   public void testDeluxeCommandCircuitBreaker() {
      RequestSpecification request = given()
            .body("{\"sku\": \"circuitBreaker\", \"customerId\": \"customer123\", \"units\": 50}")
            .contentType(ContentType.JSON).when();

      request.post("/sales").then().statusCode(Response.Status.CREATED.getStatusCode());
      request.post("/sales").then().statusCode(Response.Status.GATEWAY_TIMEOUT.getStatusCode());
      request.post("/sales").then().statusCode(Response.Status.GATEWAY_TIMEOUT.getStatusCode());
      request.post("/sales").then().statusCode(Response.Status.SERVICE_UNAVAILABLE.getStatusCode());
      request.post("/sales").then().statusCode(Response.Status.SERVICE_UNAVAILABLE.getStatusCode());

      wait1Second();

      request.post("/sales").then().statusCode(Response.Status.CREATED.getStatusCode());
   }

   private void wait1Second() {
      try {
         TimeUnit.MILLISECONDS.sleep(1000);
      } catch (InterruptedException e) {
      }
   }
}