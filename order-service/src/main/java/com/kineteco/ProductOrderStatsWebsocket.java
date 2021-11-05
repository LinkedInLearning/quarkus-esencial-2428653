package com.kineteco;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kineteco.model.ManufactureOrder;
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
@ServerEndpoint("/orders")
public class ProductOrderStatsWebsocket {
   private static final Logger LOGGER = Logger.getLogger(ProductOrderStatsWebsocket.class);

   @Inject
   ObjectMapper mapper;

   @Channel("orders")
   Multi<ManufactureOrder> orders;

   private final List<Session> sessions = new CopyOnWriteArrayList<>();

   private Cancellable cancellable;

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
      cancellable = orders
            .map(Unchecked.function(order -> mapper.writeValueAsString(order)))
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
