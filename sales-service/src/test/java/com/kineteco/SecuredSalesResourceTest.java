package com.kineteco;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(ProductInventoryWiremock.class)
public class SecuredSalesResourceTest {

   @Test
   @TestSecurity(user = "kineteco_partner", roles = { "partner" })
   public void testDeluxePartnerSecured() {
      RequestSpecification request = given()
            .body("{\"sku\": \"secured\", "
                  + "" + "\"customerId\": \"customer123\", " + "\"units\": 50}")
            .contentType(ContentType.JSON).when();

      request.post("/sales/secured").then().statusCode(Response.Status.CREATED.getStatusCode());
   }

   @Test
   @TestSecurity(user = "kineteco_customer", roles = { "customer" })
   public void testDeluxeCustomerSecured() {
      RequestSpecification request = given()
            .body("{\"sku\": \"secured\", "
                  + "" + "\"customerId\": \"customer123\", " + "\"units\": 50}")
            .contentType(ContentType.JSON).when();

      request.post("/sales/secured").then().statusCode(Response.Status.FORBIDDEN.getStatusCode());
   }
}
