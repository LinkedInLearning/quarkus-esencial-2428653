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
    public void testHelloEndpoint() {
        given()
          .when().get("/products/health")
          .then()
             .statusCode(200)
             .body(is("Product Inventory Service is up!"));
    }

    @Test
    public void testListProducts() {
        Collection products = given().when().get("/products").then().statusCode(200).extract().body().as(Collection.class);
        assertThat(products).size().isGreaterThanOrEqualTo(52);
    }

    @Test
    public void inventoryEndpoint() {
        ProductInventory domesticProduct = given().when().get("/products/{sku}", "KE180")
              .then()
              .statusCode(200)
              .extract().body().as(ProductInventory.class);
        assertThat(domesticProduct.sku).isEqualTo("KE180");
        assertThat(domesticProduct.targetConsumer).containsExactly(ConsumerType.DOMESTIC);

        ProductInventory personalProduct = given().when().get("/products/{sku}", "KE5")
              .then()
              .statusCode(200)
              .extract().body().as(ProductInventory.class);
        assertThat(personalProduct.sku).isEqualTo("KE5");
        assertThat(personalProduct.targetConsumer).containsExactly(ConsumerType.PERSONAL);

        ProductInventory corporateProduct = given().when().get("/products/{sku}", "KEBL800")
              .then()
              .statusCode(200).extract()
              .body().as(ProductInventory.class);
        assertThat(corporateProduct.sku).isEqualTo("KEBL800");
        assertThat(corporateProduct.targetConsumer).containsExactly(ConsumerType.CORPORATE);

        given().when().get("/products/{sku}", "foo")
              .then()
              .statusCode(404);
    }

    @Test
    public void testCRUD() {
        given()
              .body("{\"sku\": \"123\", \"name\": \"Super Productttt\"}")
              .contentType(ContentType.JSON)
              .when()
              .post("/products")
              .then()
              .statusCode(201);

        ProductInventory createdProduct = given().when().get("/products/{sku}", "123")
              .then()
              .statusCode(200).extract()
              .body().as(ProductInventory.class);
        assertThat(createdProduct.sku).isEqualTo("123");
        assertThat(createdProduct.name).isEqualTo("Super Productttt");

        given()
              .body("{\"name\": \"Super Product\" }")
              .contentType(ContentType.JSON)
              .when()
              .put("/products/{sku}", "123")
              .then()
              .statusCode(202);

        ProductInventory updatedProduct = given().when().get("/products/{sku}", "123")
              .then()
              .statusCode(200).extract()
              .body().as(ProductInventory.class);

        assertThat(updatedProduct.sku).isEqualTo("123");
        assertThat(updatedProduct.name).isEqualTo("Super Product");

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

        assertThat(productInventoryAfter.unitsAvailable).isEqualTo(productInventoryPre.unitsAvailable + 3);
    }

}