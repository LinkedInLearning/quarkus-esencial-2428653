package com.kineteco;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.Cancellable;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class ProductOrderStatsService {
   private static final Logger LOGGER = Logger.getLogger(ProductOrderStatsService.class);

   @Inject
   ObjectMapper mapper;

   @Channel("order-stats")
   Multi<Iterable<ProductOrderStats>> stats;

   private Cancellable cancellable;

   public void subscribe(@Observes StartupEvent ev) {
      LOGGER.info("subscribe to order stats");
      cancellable = stats
            .map(Unchecked.function(stats -> mapper.writeValueAsString(stats)))
            .subscribe().with(serialized -> LOGGER.info(serialized));
   }

   public void cleanup(@Observes ShutdownEvent ev) {
      cancellable.cancel();
   }
}
