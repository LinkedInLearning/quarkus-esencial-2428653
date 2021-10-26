package com.kineteco;

import com.kineteco.model.ProductInventory;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ProductInventoryResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/products")
          .then()
             .statusCode(200)
             .body(is("Product Inventory Service is up!"));
    }

    @Test
    public void inventoryEndpoint() {
        ProductInventory productInventory = given().when().get("/products/KE180").then().statusCode(200).extract().body()
              .as(ProductInventory.class);
        assertEquals("KE180", productInventory.getSku());
    }
}