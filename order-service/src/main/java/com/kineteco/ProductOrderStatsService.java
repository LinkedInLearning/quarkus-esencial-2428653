package com.kineteco;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("orders")
public class ProductOrderStatsService {
   private static final Logger LOGGER = Logger.getLogger(ProductOrderStatsService.class);

   @Inject
   ObjectMapper mapper;


   @GET
   @Path("/stats/subscribe")
   public void subscribe() {
      LOGGER.info("subscribe to order stats");

   }

   @GET
   @Path("/stats/unsubscribe")
   public void unsubscribe() {
      LOGGER.info("cancel to order stats");

   }
}
