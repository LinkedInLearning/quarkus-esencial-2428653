package com.kineteco;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("orders")
public class OrderService {
   private static final Logger LOGGER = Logger.getLogger(OrderService.class);

   @Inject
   ObjectMapper mapper;

   @GET
   @Path("/stats/subscribe")
   public void subscribe() {

   }
}
