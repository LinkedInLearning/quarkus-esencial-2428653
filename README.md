# Quarkus esencial
## 04_04 Documentar el API REST con OpenAPI y Swagger

Intro en power point!!!

* Añadimos ./mvnw quarkus:add-extension -Dextensions="quarkus-smallrye-openapi"
* Launch quarkus and go to the dev local console http://localhost:8080/q/dev/
  Explore Open API and Swagger
* Añadimos cambios documentales con anotaciones en el código de forma que el código vive al mismo que tiempo que
la documentación
* Añadimos documentación de ejemplo en patch
  @Operation(summary = "Update the stock of a product by sku.", description = "Longer description that explains all.")
* Añadimos documentacion al resource REST
 
```java
  @OpenAPIDefinition(tags = {
  @Tag(name = "inventory", description = "Operations handling products inventory.")
  }, info =
  @Info(title = "Product Inventory Service", version = "1.0", description = "Operations handling Products Inventory.")
  )
```
  
Nada cambia porque tenemos que añadirlo a una appli
```java

@OpenAPIDefinition(tags = {
@Tag(name = "inventory", description = "Operations handling products inventory.")
}, info =
@Info(title = "Product Inventory Service", version = "1.0", description = "Operations handling Products Inventory.")
)
public class ProductInventoryServiceApp extends Application {
}
```

En este video hemos aprendido como documentar con OpenAPI y poder exponer nuestra API para ser usada con Swagger
y mantener la documentación y el código de un API REST en el mismo lugar. Ademas podemos utilizar el enfoque "Design First"
para pensar nuestra API publica, porque las API son como los diamantes: para siempre.



