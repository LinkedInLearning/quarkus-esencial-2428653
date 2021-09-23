package com.kineteco.service;

import com.kineteco.model.ProductAvailability;
import com.kineteco.model.ProductInventory;
import com.kineteco.model.ProductLine;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
import org.jboss.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ProductInventoryService {
   private Map<String, ProductInventory> inventory = new HashMap();
   private static final Logger LOGGER = Logger.getLogger(ProductInventoryService.class);

   void onStart(@Observes StartupEvent ev) {
      LOGGER.info("Product Inventory Service is starting Powered by Quarkus");
      try {
         loadData();
      } catch (Exception e) {
         LOGGER.info("Loaded " + inventory.size());
         LOGGER.error("Unable to load catalog.", e);

      }
   }

   private void loadData() throws Exception {
      InputStream resourceAsStream = this.getClass().getClassLoader()
            .getResourceAsStream("KinetEco_product_inventory.csv");

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
            //values[8]
            //values[9]
            //values[10]
            productInventory.setUnitsAvailable(Integer.parseInt(values[11]));
            inventory.put(productInventory.getSku(), productInventory);
            id++;
         }
      }
   }

   void onStop(@Observes ShutdownEvent ev) {
      LOGGER.info("Product Inventory Service shutting down...");
   }

   public ProductInventory getBySku(String sku) {
      return inventory.get(sku);
   }
}
