package com.kineteco.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "com.kineteco")
public interface ProductInventoryConfig {

   String greetingMessage();

   @WithDefault("true")
   boolean fullCatalog();

}
