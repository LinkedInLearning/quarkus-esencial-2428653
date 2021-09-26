package com.kineteco.service;

import com.kineteco.config.ProductInventoryConfig;
import com.kineteco.model.ConsumerType;
import com.kineteco.model.ProductInventory;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Collection;

@ApplicationScoped
public class ProductInventoryService {
   private static final Logger LOGGER = Logger.getLogger(ProductInventoryService.class);

   @Inject
   ProductInventoryConfig productInventoryConfig;

   void onStart(@Observes StartupEvent ev) {
      LOGGER.info("Product Inventory Service is starting Powered by Quarkus");
   }

   void onStop(@Observes ShutdownEvent ev) {
      LOGGER.info("Product Inventory Service shutting down...");
   }

   public ProductInventory getBySku(String sku) {
      ProductInventory productInventory = ProductInventory.findById(sku);
      if (productInventoryConfig.retrieveFullCatalog() || productInventory.getTargetConsumer().contains(ConsumerType.CORPORATE)) {
         return productInventory;
      }

      return null;
   }

   public Collection<ProductInventory> listInventory() {
      return ProductInventory.findAll().list();
   }

   public void addProductInventory(ProductInventory productInventory) {
      productInventory.persist();
   }

   public void updateProductInventory(ProductInventory productInventory) {
      productInventory.persist();
   }

   public ProductInventory stockUpdate(String sku, Integer stock) {
      ProductInventory productInventory = ProductInventory.findById(sku);

      if (productInventoryConfig.retrieveFullCatalog() || productInventory.getTargetConsumer().contains(ConsumerType.CORPORATE)) {
         productInventory.setUnitsAvailable(productInventory.getUnitsAvailable() + stock);
         productInventory.persist();
         return ProductInventory.findById(sku);
      }

      return null;
   }

   public void delete(String sku) {
      ProductInventory.deleteById(sku);
   }
}
