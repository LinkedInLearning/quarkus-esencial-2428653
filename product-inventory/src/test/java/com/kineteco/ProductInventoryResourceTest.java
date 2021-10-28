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

        given().when().get("/products/{sku}", "foo")
              .then()
              .statusCode(404);
    }

    @Test
    public void testListProducts() {
        Collection products = given().when().get("/products").then().statusCode(200).extract().body().as(Collection.class);
        assertThat(products).size().isGreaterThanOrEqualTo(52);
    }

    @Test
    public void testCRUD() {
        given()
              .body("{\"sku\": \"123\"}")
              .contentType(ContentType.JSON)
              .when()
              .post("/products")
              .then()
              .statusCode(201);

        ProductInventory createdProduct = given().when().get("/products/{sku}", "123")
              .then()
              .statusCode(200).extract()
              .body().as(ProductInventory.class);
        assertThat(createdProduct.getSku()).isEqualTo("123");
        assertThat(createdProduct.getName()).isNull();

        given()
              .body("{\"sku\": \"123\", \"name\": \"Super Product\" }")
              .contentType(ContentType.JSON)
              .when()
              .put("/products/{sku}", "123")
              .then()
              .statusCode(202);

        ProductInventory updatedProduct = given().when().get("/products/{sku}", "123")
              .then()
              .statusCode(200).extract()
              .body().as(ProductInventory.class);

        assertThat(updatedProduct.getSku()).isEqualTo("123");
        assertThat(updatedProduct.getName()).isEqualTo("Super Product");

        given().when().delete("/products/{sku}", "123")
              .then()
              .statusCode(202);

        given().when().get("/products/{sku}", "123")
              .then()
              .statusCode(404);
    }

    @Test
    public void testUpgradeStock() {
        ProductInventory productInventoryPre = given().when().get("/products/{sku}", "KE200")
              .then()
              .statusCode(200)
              .extract().body().as(ProductInventory.class);

        assertThat(productInventoryPre).isNotNull();

        given()
              .when().queryParam("stock", 3)
              .patch("/products/{sku}", "KE200")
              .then()
              .statusCode(202);

        ProductInventory productInventoryAfter = given().when().get("/products/{sku}", "KE200")
              .then()
              .statusCode(200)
              .extract().body().as(ProductInventory.class);

        assertThat(productInventoryAfter.getUnitsAvailable()).isEqualTo(productInventoryPre.getUnitsAvailable() + 3);
    }
}
