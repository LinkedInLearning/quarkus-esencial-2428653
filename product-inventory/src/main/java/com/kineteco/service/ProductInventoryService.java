package com.kineteco.service;

import com.kineteco.config.ProductInventoryConfig;
import com.kineteco.model.ConsumerType;
import com.kineteco.model.ProductAvailability;
import com.kineteco.model.ProductInventory;
import com.kineteco.model.ProductLine;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ProductInventoryService {
   private static final Logger LOGGER = Logger.getLogger(ProductInventoryService.class);

   private Map<String, ProductInventory> inventory = new HashMap();

   @Inject
   ProductInventoryConfig productInventoryConfig;

   void onStart(@Observes StartupEvent ev) {
      LOGGER.info("Product Inventory Service is starting Powered by Quarkus");
      LOGGER.info("  _   _   _   _   _   _   _   _");
      LOGGER.info(" / \\ / \\ / \\ / \\ / \\ / \\ / \\ / \\");
      LOGGER.info("( K | i | n | e | t | e | c | o )");
      LOGGER.info(" \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/");
      loadData();
   }

   void loadData() {
      inventory.clear();
      InputStream resourceAsStream = this.getClass().getClassLoader()
            .getResourceAsStream("KinetEco_product_inventory.csv");
      try {
         try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream))) {
            String line;
            int id = 0;
            // Category,Name,Package Quantity,SKU,Power (Watts),Footprint (SQ FT),Manufacturing Cost,Suggested Retail,Product Line,Target Consumer,Availability,Units Available
            while ((line = br.readLine()) != null) {
               String[] values = line.split(",");
               ProductInventory productInventory = new ProductInventory();
               productInventory.setCategory(values[0]);
               productInventory.setName(values[1]);
               productInventory.setQuantity(Integer.parseInt(values[2]));
               productInventory.setSku(values[3]);
               productInventory.setPowerWatts(values[4]);
               productInventory.setFootprint(values[5]);
               productInventory.setManufacturingCost(new BigDecimal(values[6].replace("$","")));
               productInventory.setPrice(new BigDecimal(values[7].replace("$","")));
               productInventory.setProductLine(ProductLine.valueOf(values[8].toUpperCase()));
               List<ConsumerType> targetConsumers = new ArrayList<>();
               String toUpperCaseConsumerTypes = values[9].toUpperCase();
               for( ConsumerType consumerType: ConsumerType.values()) {
                  parseConsumerTypes(toUpperCaseConsumerTypes, consumerType, targetConsumers);
               }
               productInventory.setTargetConsumer(targetConsumers);
               productInventory.setProductAvailability(ProductAvailability.valueOf(values[10].replace(" ", "_").toUpperCase()));
               productInventory.setUnitsAvailable(Integer.parseInt(values[11]));
               if (productInventoryConfig.retrieveFullCatalog() || productInventory.getTargetConsumer().contains(ConsumerType.CORPORATE)) {
                  inventory.put(productInventory.getSku(), productInventory);
               }
               id++;
            }
         }
      } catch (Exception e) {
         LOGGER.info("Loaded " + inventory.size());
         LOGGER.error("Unable to load catalog.", e);
      }
   }

   private void parseConsumerTypes(String values, ConsumerType consumerType, List<ConsumerType> targetConsumers) {
      if (values.contains(consumerType.name())) {
         targetConsumers.add(consumerType);
      }
   }

   void onStop(@Observes ShutdownEvent ev) {
      LOGGER.info("Product Inventory Service shutting down...");
   }

   public ProductInventory getBySku(String sku) {
      return inventory.get(sku);
   }

   public Collection<ProductInventory> listInventory() {
      return inventory.values();
   }

   public void addProductInventory(ProductInventory productInventory) {
      inventory.putIfAbsent(productInventory.getSku(), productInventory);
   }

   public void updateProductInventory(String sku, ProductInventory productInventory) {
      productInventory.setSku(sku);
      inventory.put(sku, productInventory);
   }

   public ProductInventory stockUpdate(String sku, Integer stock) {
      ProductInventory productInventory = inventory.get(sku);
      if (productInventory != null) {
         productInventory.setUnitsAvailable(productInventory.getUnitsAvailable() + stock);
      }
      return productInventory;
   }

   public void delete(String sku) {
      inventory.remove(sku);
   }
}
