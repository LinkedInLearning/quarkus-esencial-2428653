package com.kineteco.service;

import com.kineteco.model.ProductInventory;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class ProductInventoryServiceTest {

   @Inject
   ProductInventoryService productInventoryService;

   @Test
   public void testGetBySku() {
      ProductInventory productInventory = productInventoryService.getBySku("KE180");
      assertThat(productInventory).isNotNull();
      assertThat(productInventory.getSku()).isEqualTo("KE180");
   }
}