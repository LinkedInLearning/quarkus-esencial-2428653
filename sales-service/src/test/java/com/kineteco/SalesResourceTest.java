package com.kineteco;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class SalesResourceTest {

   @Test
   public void testHealthEndpoint() {
      given()
            .when().get("/sales")
            .then()
            .statusCode(200)
            .body(is("Sales Service is up!!"));
   }
}