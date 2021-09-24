package com.kineteco.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

import javax.validation.constraints.Min;

@ConfigMapping(prefix = "com.kineteco")
public interface ProductInventoryConfig {
   String greetingMessage();

   @WithName("full-catalog")
   @WithDefault("true")
   boolean retrieveFullCatalog();

   @Min(5)
   int minUnits();
}
