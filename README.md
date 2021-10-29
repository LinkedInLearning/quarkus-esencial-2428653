# Quarkus esencial
## 04_04 Documentar el API REST con OpenAPI y Swagger

Intro en power point!!!

* Añadimos la extension openapi
```shell
./mvnw quarkus:add-extension -Dextensions="quarkus-smallrye-openapi"  
```

* Lanzamos quarkus and vamos a la dev console. Explore Open API and Swagger
```shell
h
d
```


* Añadimos cambios documentales con anotaciones en el código de forma que el código vive al mismo que tiempo que
  la documentación

* Añadimos documentación de ejemplo en patch
```java
@Operation(summary = "Update the stock of a product by sku.", description = "Longer description that explains all.")
```  

* Para cambiar la cabecera tebemos que crear una applicacion

```java
 @OpenAPIDefinition(tags = {
        @Tag(name = "inventory", description = "Operations handling products inventory.")
}, info =
@Info(title = "Product Inventory Service", version = "1.0", description = "Operations handling Products Inventory.")
)
public class ProductInventoryServiceApp extends Application {
}
```

Open API nos va a permitir también generar código de cliente front en type script por ejemplo.

En este video hemos aprendido como documentar con OpenAPI y poder exponer nuestra API para ser usada con Swagger
y mantener la documentación y el código de un API REST en el mismo lugar.
Además podremos utilizar el enfoque "Design First" para pensar nuestra API pública.