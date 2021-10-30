package com.kineteco;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.enterprise.event.Observes;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(tags = {
      @Tag(name = "inventory", description = "Operations handling products inventory.")
}, info =
@Info(title = "Product Inventory Service", version = "1.0", description = "Operations handling Products Inventory.")
)
public class ProductInventoryServiceApp extends Application {

   private static final Logger LOGGER = Logger.getLogger(ProductInventoryServiceApp.class);

   void onStart(@Observes StartupEvent ev) {
      LOGGER.info("Product Inventory Service is starting Powered by Quarkus");
      LOGGER.info("  _   _   _   _   _   _   _   _");
      LOGGER.info(" / \\ / \\ / \\ / \\ / \\ / \\ / \\ / \\");
      LOGGER.info("( K | i | n | e | t | e | c | o )");
      LOGGER.info(" \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/");
   }

   void onStop(@Observes ShutdownEvent ev) {
      LOGGER.info("Product Inventory Service shutting down...");
   }

}
