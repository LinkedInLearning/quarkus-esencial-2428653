# Quarkus esencial
## 06_02 Comunicar servicios mediante un cliente REST con inyección de dependencias en Quarkus


* `./mvnw quarkus:add-extension -Dextensions="rest-client,rest-client-jackson"`
* Arrancamos en modo desarrollo y cambiamos la version %dev.quarkus.http.port=8081 para no tener conflicto con product inventory  
* Creamos la interfaz 'ProductInventoryService' anotada con @RegisterRestClient
* Vamos a anotar la clase con @Path y crearemos el método

```java
   @GET
   @Path("/{sku}/stock")
   Integer getStock(@PathParam("sku") String sku);
```

* Podemos usar un API programática para esto mismo, pero vamos a utilizar la inyección de dependencias de CDI
@Inject junto a @RestClient
  
* El error nos indica de configurar kineteco-product-inventory/mp-rest/url=http://localhost:8080

Comprobamos que el endpoint de productos esta disponible con `http http://localhost:8080/products/`
Probamos `http http://localhost:8081/sales/KE36Li/availability\?units\=300`
Probamos `http http://localhost:8081/sales/KE36Li/availability\?units\=900`
