package com.kineteco;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kineteco.model.ManufactureOrder;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("orders")
public class ProductOrderStatsWebsocket {
   private static final Logger LOGGER = Logger.getLogger(ProductOrderStatsWebsocket.class);

   @Inject
   ObjectMapper mapper;

   @Channel("orders")
   Multi<ManufactureOrder> orders;

   @GET
   @Path("/subscribe")
   public void subscribe() {
      orders
            .map(Unchecked.function(order -> mapper.writeValueAsString(order)))
            .subscribe().with(serialized -> LOGGER.info(serialized));
   }

}
