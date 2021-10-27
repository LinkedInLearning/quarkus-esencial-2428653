package com.kineteco;

import com.kineteco.model.ConsumerType;
import com.kineteco.model.ProductInventory;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

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
        ProductInventory domesticProduct = given().when().get("/products/{sku}", "KE180")
              .then()
              .statusCode(200)
              .extract().body().as(ProductInventory.class);
        assertThat(domesticProduct.getSku()).isEqualTo("KE180");
        assertThat(domesticProduct.getTargetConsumer()).containsExactly(ConsumerType.DOMESTIC);
    }
}