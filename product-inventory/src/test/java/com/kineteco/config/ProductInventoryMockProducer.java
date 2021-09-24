package com.kineteco.config;

import io.smallrye.config.SmallRyeConfig;
import org.eclipse.microprofile.config.Config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class ProductInventoryMockProducer {
   @Inject
   Config config;

   @Produces
   @ApplicationScoped
   @io.quarkus.test.Mock ProductInventoryConfig productInventoryConfig() {
      return config.unwrap(SmallRyeConfig.class).getConfigMapping(ProductInventoryConfig.class);
   }
}