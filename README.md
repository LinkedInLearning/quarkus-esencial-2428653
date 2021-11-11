# Quarkus esencial
## 09_03 Implementar chequeos de salud con Quarkus

* Añadimos extension `smallrye-health` en Sales Service y desplegamos en kubernetes
```shell
./mvnw quarkus:add-extension -Dextensions="smallrye-health"
./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true 
export SALES_SERVICE=$(minikube service --url sales-service)
```
* Si hacemos un scale down de Product Inventory, sales sigue bien. Arrancamos Sales service en modo desarrollo.

* Abrimos el dev ui

* Comprobamos en linea de comandos los endpoints
```shell
http localhost:8080/q/health/live #Product Inventory está up and running
http localhost:8080/q/health/ready #Product inventory está preparada para aceptar peticiones
http localhost:8080/q/health #Acumula todos los check de salud de Product Inventory
http localhost:8080/q/health-ui  #Interfaz gráfica con todos los checks.
```
* Todos los endpoint retornan JSON con dos campos

- `status` resultado global de todos los checkeos de salud
- `checks` un array con cada resultado específico

* Añadimos un check Liveness. No olvidar de mencionar @ApplicationScope de ProductResource
  Si no añadimos ApplicationScoped el scope por defecto es @Singleton
```java
@Liveness
public class PingSalesResourceHealthCheck implements HealthCheck {
   @Inject
   SalesResource salesResource;

   @Override
   public HealthCheckResponse call() {
      String response = salesResource.health();
      return HealthCheckResponse.named("Ping Sales Service")
            .withData("Response", response).up().build();
   }
}
```
* Readiness Sales service
```java
@Readiness
public class ProductInventoryResourceHealthCheck implements HealthCheck {
   @Inject
   @RestClient
   ProductInventoryServiceClient productInventoryServiceClient;

   @Override
   public HealthCheckResponse call() {
      long size = 0;

      try {
         size = productInventoryServiceClient.size();
      } catch (WebApplicationException ex) {
         if (ex.getResponse().getStatus() >= 500) {
            return HealthCheckResponse.named("ProductInventoryServiceCheck")
                  .withData("exception", ex.getMessage())
                  .down()
                  .build();
         }
      }
      return HealthCheckResponse
            .named("ProductInventoryServiceCheck")
            .withData("size", size)
            .up()
            .build();
   }
}
```

* Probamos en kubernetes. La extension ya nos ha añadido lo necesario para kubernetes con propiedades que
  podremos configurar. Empezar con los valores por defecto y luego ir a prueba error