package com.kineteco;

import com.kineteco.model.ProductAvailability;
import com.kineteco.model.ProductInventory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/products")
public class ProductInventoryResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Product Inventory Service is up!";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response inventory(String sku) {
        ProductInventory productInventory = new ProductInventory("KE180");
        productInventory.setSku("KE180");
        productInventory.setName("K-Eco 180");
        productInventory.setProductAvailability(ProductAvailability.IN_STOCK);
        productInventory.setQuantity(12);
        productInventory.setPrice(BigDecimal.valueOf(315.0));
        return Response.ok(productInventory).build();
    }
}
