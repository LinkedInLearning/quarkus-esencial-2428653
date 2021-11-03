package com.kineteco.repository;

import com.kineteco.model.ProductInventory;
import com.kineteco.model.UnitsAvailable;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductInventoryRepository implements PanacheRepository<ProductInventory> {

   public  ProductInventory findBySku(String sku) {
      return find("sku", sku).firstResult();
   }

   public int findCurrentStock(String sku) {
      UnitsAvailable unitsAvailable = find("sku", sku).project(UnitsAvailable.class).firstResult();

      if (unitsAvailable == null) {
         return 0;
      }

      return unitsAvailable.unitsAvailable;
   }
}
