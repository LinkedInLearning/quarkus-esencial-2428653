package com.kineteco;

import com.kineteco.model.ProductAvailability;
import com.kineteco.model.ProductInventory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

@Path("/hello")
public class ProductsInventoryResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/123")
    public ProductInventory inventory() {
        ProductInventory productInventory = new ProductInventory();
        productInventory.setSku("KE180");
        productInventory.setName("K-Eco 180");
        productInventory.setProductAvailability(ProductAvailability.IN_STOCK);
        productInventory.setQuantity(12);
        productInventory.setPrice(BigDecimal.valueOf(315.0));
        return productInventory;
    }
}
