package com.kineteco;

import com.kineteco.model.ConsumerType;
import com.kineteco.model.ProductInventory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ProductInventoryResourceTest {

    @Test
    public void testHealthEndpoint() {
        given()
          .when().get("/products/health")
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

        ProductInventory personalProduct = given().when().get("/products/{sku}", "KE5")
              .then()
              .statusCode(200)
              .extract().body().as(ProductInventory.class);
        assertThat(personalProduct.getSku()).isEqualTo("KE5");
        assertThat(personalProduct.getTargetConsumer()).containsExactly(ConsumerType.PERSONAL);

        ProductInventory corporateProduct = given().when().get("/products/{sku}", "KEBL800")
              .then()
              .statusCode(200).extract()
              .body().as(ProductInventory.class);
        assertThat(corporateProduct.getSku()).isEqualTo("KEBL800");
        assertThat(corporateProduct.getTargetConsumer()).containsExactly(ConsumerType.CORPORATE);

        given().when().get("/products/{sku}", "foo")
              .then()
              .statusCode(404);
    }
}
