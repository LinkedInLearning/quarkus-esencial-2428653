# Quarkus esencial
## 08_06 Comunicar Microservicios Quarkus con mensajes y Apache Kafka

* Añadimos las extensiones necesarias
  Tenemos quarkus-resteasy-reactive-jackson
```shell
  ./mvnw quarkus:add-extension -Dextensions="quarkus-smallrye-reactive-messaging-kafka"
 ```

* En Product Inventory al arrancarlo y tener docker instancia automaticamente Kafka por mi
* Update stock

```java
 @Inject 
 ManufactureOrderEmitter manufactureOrderEmitter;

public Uni<Response> updateStock(String sku, @RestQuery("stock") Integer stock) {
return ProductInventory.findCurrentStock(sku)
  .onItem().call(currentStock -> {
  LOGGER.debugf("update stock for sku %s with current stock %d with %d", sku, currentStock, stock);
    int newStock = currentStock + stock;
    if (newStock < 0) {
        int quantity = newStock*-1;
        manufactureOrderEmitter.sendManufactureOrder(sku, quantity);
        newStock = 0;
    }
         return ProductInventory.update("unitsAvailable = ?1 where sku= ?2", newStock, sku);
   }).onItem().transform(u -> Response.accepted().build());
}
```
* Manufacture Order Emitter
```java
@ApplicationScoped
public class ManufactureOrderEmitter {
   private static final Logger LOGGER = Logger.getLogger(ManufactureOrderEmitter.class);

   @Channel("orders")
   Emitter<ManufactureOrder> emitter;

   public void sendManufactureOrder(String sku, int quantity) {
      LOGGER.debugf("Emit manufacturing message for sku %s with %d quantity", sku, quantity);
      ManufactureOrder manufactureOrder = new ManufactureOrder();
      manufactureOrder.sku = sku;
      manufactureOrder.quantity = quantity;
      emitter.send(manufactureOrder);
   }
}
```

* Properties
```properties
mp.messaging.outgoing.orders.connector=smallrye-kafka
mp.messaging.outgoing.orders.value.serializer=io.quarkus.kafka.client.serialization.ObjectMapperSerializer
```


ProductOrderStats
```java
package com.kineteco;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kineteco.model.ProductOrderStats;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.Cancellable;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class ProductOrderStatsService {
   private static final Logger LOGGER = Logger.getLogger(ProductOrderStatsService.class);

   @Inject
   ObjectMapper mapper;

  @Channel("orders")
  Multi<ManufactureOrder> orders;

   public void subscribe(@Observes StartupEvent ev) {
      LOGGER.info("subscribe to order stats");
      orders
             .map(Unchecked.function(stats -> mapper.writeValueAsString(stats)))
             .subscribe().with(serialized -> LOGGER.info(serialized));
   }

   public void cleanup(@Observes ShutdownEvent ev) {
      cancellable.cancel();
   }
}
```

Properties
```properties
mp.messaging.incoming.orders.connector=smallrye-kafka
mp.messaging.incoming.orders.auto.offset.reset=earliest
mp.messaging.incoming.orders.value.deserializer=com.kineteco.model.ManufactureOrderDeserializer
```