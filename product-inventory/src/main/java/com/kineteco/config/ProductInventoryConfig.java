package com.kineteco.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "kineteco")
public interface ProductInventoryConfig {
   @WithName("greeting-message")
   String message();

   @WithDefault("true")
   @WithName("full-catalog")
   boolean retrieveFullCatalog();

}
