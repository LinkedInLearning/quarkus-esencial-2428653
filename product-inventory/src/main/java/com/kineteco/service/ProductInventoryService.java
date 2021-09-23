package com.kineteco.service;

import com.kineteco.model.ProductAvailability;
import com.kineteco.model.ProductInventory;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ProductInventoryService {

   private Map<String, ProductInventory> inventory = new HashMap();

   public ProductInventoryService() {
      ProductInventory productInventory = new ProductInventory("KE180");
      productInventory.setSku("KE180");
      productInventory.setName("K-Eco 180");
      productInventory.setProductAvailability(ProductAvailability.IN_STOCK);
      productInventory.setQuantity(12);
      productInventory.setPrice(BigDecimal.valueOf(315.0));
      inventory.put("KE180", productInventory);
   }

   public ProductInventory getBySku(String sku) {
      return inventory.get(sku);
   }
}
