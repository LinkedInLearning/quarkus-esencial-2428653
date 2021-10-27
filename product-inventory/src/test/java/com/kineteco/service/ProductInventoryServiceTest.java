package com.kineteco.service;

import com.kineteco.model.ConsumerType;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class ProductInventoryServiceTest {

   @Inject
   ProductInventoryService service;

   @Test
   public void testLoadDataFullCatalog() {
      service.loadData();

      assertThat(service.getBySku("KE180")).isNotNull();
      assertThat(service.getBySku("KE180").getTargetConsumer()).containsExactly(ConsumerType.DOMESTIC);
   }
}
