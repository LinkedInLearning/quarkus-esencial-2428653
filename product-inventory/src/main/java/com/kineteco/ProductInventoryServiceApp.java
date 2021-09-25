package com.kineteco;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(tags = {
      @Tag(name = "inventory", description = "Operations handling products inventory.")
}, info =
@Info(title = "Product Inventory Service", version = "1.0", description = "Operations handling Products Inventory.")
)
public class ProductInventoryServiceApp extends Application {
}
