package com.kineteco.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "com.kineteco")
public interface ProductInventoryConfig {
   String service();

   @WithName("greeting-message")
   String message();

   @WithName("full-catalog")
   @WithDefault("true")
   boolean retrieveFullCatalog();

}
