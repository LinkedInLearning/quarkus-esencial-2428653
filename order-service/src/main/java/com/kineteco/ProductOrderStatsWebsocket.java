package com.kineteco;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kineteco.model.ProductOrderStats;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.Cancellable;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
@ServerEndpoint("/stats/orders")
public class ProductOrderStatsWebsocket {
   private static final Logger LOGGER = Logger.getLogger(ProductOrderStatsWebsocket.class);

   @Inject
   ObjectMapper mapper;

   @Channel("order-stats")
   Multi<Iterable<ProductOrderStats>> stats;

   private Cancellable cancellable;

   private final List<Session> sessions = new CopyOnWriteArrayList<>();

   @OnOpen
   public void onOpen(Session session) {
      LOGGER.info("Session opened");
      sessions.add(session);
   }

   @OnClose
   public void onClose(Session session) {
      LOGGER.info("Session closed");
      sessions.remove(session);
   }

   @PostConstruct
   public void subscribe() {
      LOGGER.info("subscribe to order stats");
      cancellable = stats
            .map(Unchecked.function(scores -> mapper.writeValueAsString(scores)))
            .subscribe().with(serialized -> sessions.forEach(session -> write(session, serialized)));
   }

   private void write(Session session, String serialized) {
      session.getAsyncRemote().sendText(serialized, result -> {
         if (result.getException() != null) {
            LOGGER.error("Unable to write message to web socket", result.getException());
         }
      });
   }

   @PreDestroy
   public void cleanup() {
      cancellable.cancel();
   }
}
