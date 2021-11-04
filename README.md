# Quarkus esencial
## 08_07 Utilizar un Websocket para visualizar datos en continuo con Quarkus

* Añadimos las extensiones necesarias en Order Service
```shell
  ./mvnw quarkus:add-extension -Dextensions="quarkus-websockets,quarkus-undertow-websockets"
 ```

* Websocket
```java
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
```
* Run  runStockUpgrades.sh
* Abrir el navegador en localhost:8480
