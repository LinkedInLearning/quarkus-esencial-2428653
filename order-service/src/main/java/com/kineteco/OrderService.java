package com.kineteco;

import com.kineteco.model.KinetecoProductRanking;
import com.kineteco.model.ManufactureOrder;
import com.kineteco.model.ProductOrderStats;
import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderService {
   private static final Logger LOGGER = Logger.getLogger(OrderService.class);

   private final KinetecoProductRanking productRanking = new KinetecoProductRanking(10);

   /**
    * Multi Productos con las cantidades fabricar
    *
    * Resultado Multi de un iterable de estadísticas. SKU - Numero de Manufacture Orders
    *
    */
   public Multi<Iterable<ProductOrderStats>> computeProductsStats(Multi<ManufactureOrder> orders) {
      LOGGER.info("orders incoming");
      return orders
            .onItem().transform(order -> productRanking.onNewStat(new ProductOrderStats(order.sku, 1)))
            .invoke(() -> LOGGER.info("Manufacture order received. Stats of top orders computed %s"));
   }

   private ProductOrderStats incrementScore(ProductOrderStats stats, ManufactureOrder order) {
      stats.sku = order.sku;
      stats.units++;
      return stats;
   }
}
