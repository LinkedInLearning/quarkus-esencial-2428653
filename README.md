# Quarkus esencial
## 06_02 Comunicar servicios mediante un cliente REST con inyección de dependencias en Quarkus

* Añadimos las extensiones
```shell
  ./mvnw quarkus:add-extension -Dextensions="rest-client,rest-client-jackson"
```
* Arrancamos en modo desarrollo y cambiamos la version `%dev.quarkus.http.port=8280` para no tener conflicto con product inventory
* Creamos la interfaz 'ProductInventoryService' anotada con @RegisterRestClient
* Vamos a anotar la clase con @Path y crearemos el método

```java
@Path("/products")
@RegisterRestClient(configKey = "kineteco-product-inventory")
@Produces(MediaType.APPLICATION_JSON)
@ClientHeaderParam(name = "caller-header", value = "sales-service")
@RegisterClientHeaders
public interface ProductInventoryService {

  @GET
  @Path("/{sku}/stock")
  @ClientHeaderParam(name = "method-header", value = "stock")
  Integer getStock(@PathParam("sku") String sku);
}
```

* Podemos usar un API programática para esto mismo, pero vamos a utilizar la inyección de dependencias de CDI
  @Inject junto a @RestClient
```java
@Inject
@RestClient
ProductInventoryService productInventoryService;
```  

* Implementamos
```java
@GET
@Produces(MediaType.TEXT_PLAIN)
@Path("/{sku}/available")
public Boolean available(@PathParam("sku") String sku, @QueryParam("units") Integer units) {
  if (units == null) {
  throw new BadRequestException("units query parameter is mandatory");
  }
  return productInventoryService.getStock(sku) >= units;
}
```  
* Configuraamos kineteco-product-inventory/mp-rest/url=http://localhost:8080
```properties
kineteco-product-inventory/mp-rest/url=http://localhost:8080
```

Comprobamos que el endpoint de productos esta disponible con
```shell
http http://localhost:8080/products/`
http http://localhost:8081/sales/KE36Li/availability\?units\=300
http http://localhost:8081/sales/KE36Li/availability\?units\=900
````

Ahora que sabemos comunicar en local, nos queda desplegar a kubernetes.
* Tenemos minikube arrancado y docker y la base de datos y product inventory desplegados
* Configuramos el acceso a prod
```properties
 `%prod.kineteco-product-inventory/mp-rest/url=http://product-inventory-service:80`
``` 
* Desplegamos sales service con 
```shell
./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true
```  

```shell
http http://URL-KUBERNETES/sales/KE36Li/availability\?units\=300
http http://localhost:8081/sales/KE36Li/availability\?units\=900
```