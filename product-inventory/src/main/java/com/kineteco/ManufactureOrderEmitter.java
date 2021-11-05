package com.kineteco;

import com.kineteco.model.ManufactureOrder;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

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
