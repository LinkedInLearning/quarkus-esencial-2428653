package com.kineteco.service;

import com.kineteco.config.ProductInventoryConfig;
import com.kineteco.model.ConsumerType;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ProductInventoryServiceTest {

   @InjectMock
   ProductInventoryConfig productInventoryConfig;

   @Inject
   ProductInventoryService service;

   @Test
   public void testLoadDataFullCatalog() {
      when(productInventoryConfig.retrieveFullCatalog()).thenReturn(true);

      service.loadData();

      assertThat(service.getBySku("KE180")).isNotNull();
      assertThat(service.getBySku("KE180").getTargetConsumer()).containsExactly(ConsumerType.DOMESTIC);

      assertThat(service.getBySku("KEBL800")).isNotNull();
      assertThat(service.getBySku("KEBL800").getTargetConsumer()).containsExactly(ConsumerType.CORPORATE);
   }

   @Test
   public void testLoadDataCorporate() {
      when(productInventoryConfig.retrieveFullCatalog()).thenReturn(false);

      service.loadData();

      assertThat(service.getBySku("KE180")).isNull();
   }
}