package com.kineteco;

import com.kineteco.model.ManufactureOrder;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderEmitterService {
   @Channel("orders")
   Emitter<ManufactureOrder> emitter;

   public void sendManufactureOrder(String sku, int quantity) {
      ManufactureOrder manufactureOrder = new ManufactureOrder();
      manufactureOrder.sku = sku;
      manufactureOrder.quantity = quantity;
      emitter.send(manufactureOrder);
   }
}
