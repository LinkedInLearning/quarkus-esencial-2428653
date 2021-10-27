package com.kineteco.service;

import com.kineteco.model.ProductAvailability;
import com.kineteco.model.ProductInventory;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;

@ApplicationScoped
public class ProductInventoryService {

   public ProductInventory getBySku(String sku) {
      ProductInventory productInventory = new ProductInventory();
      productInventory.setSku(sku);
      productInventory.setName("K-Eco 180");
      productInventory.setProductAvailability(ProductAvailability.IN_STOCK);
      productInventory.setQuantity(12);
      productInventory.setPrice(BigDecimal.valueOf(315.0));
      return productInventory;
   }
}
