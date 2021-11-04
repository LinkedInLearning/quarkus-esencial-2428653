package com.kineteco;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class ProductOrderStatsService {
   private static final Logger LOGGER = Logger.getLogger(ProductOrderStatsService.class);

   @Inject
   ObjectMapper mapper;

   public void subscribe(@Observes StartupEvent ev) {
      LOGGER.info("subscribe to order stats");

   }

   public void cleanup(@Observes ShutdownEvent ev) {
      LOGGER.info("cancel to order stats");
   }
}
