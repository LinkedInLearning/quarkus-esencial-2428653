package com.kineteco;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class OrderLifecycleService {

   private static final Logger LOGGER = Logger.getLogger(OrderLifecycleService.class);

   void onStart(@Observes StartupEvent ev) {
      LOGGER.info("Order Service is starting Powered by Quarkus");
      LOGGER.info("  _   _   _   _   _   _   _   _");
      LOGGER.info(" / \\ / \\ / \\ / \\ / \\ / \\ / \\ / \\");
      LOGGER.info("( K | i | n | e | t | e | c | o )");
      LOGGER.info(" \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/");
   }

   void onStop(@Observes ShutdownEvent ev) {
      LOGGER.info("Order Service shutting down...");
   }

}
