package com.kineteco.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

import javax.validation.constraints.NotNull;

@ConfigMapping(prefix = "kineteco")
public interface ProductInventoryConfig {
   String greetingMessage();

   @WithName("full-catalog")
   @WithDefault("true")
   @NotNull
   boolean retrieveFullCatalog();
}
