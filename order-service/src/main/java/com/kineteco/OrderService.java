package com.kineteco;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.jboss.logging.Logger;

@QuarkusMain
public class OrderServiceMain {
   private static final Logger LOGGER = Logger.getLogger(OrderServiceMain.class);

   public static void main(String ... args) {
      LOGGER.info("Running main method");
      Quarkus.run(args);
   }
}
