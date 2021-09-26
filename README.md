# Quarkus esencial
## 06_03 Test unitarios con WireMock y Quarkus

El problema que necesitamos solucionar ahora es que los tests que vayamos a realizar de nuestro SalesResource dependen
de que un servicio esté arrancado, necesitamos poder simular su comportamiento.
Para ello utilizamos Wiremock.
* Añadimos test

```java
 @Test
public void testAvailability() {
      given()
      .queryParam("units", 30)
      .when().get("/sales/{sku}/availability", "123")
      .then()
      .statusCode(200)
      .body(is("true"));
} 
```
comprobamos que falla por no tener el servicio up

* Añadimos la dependencia a maven para desacoplar los tests unitarios del servicio
```xml
    <dependency>
      <groupId>com.github.tomakehurst</groupId>
      <artifactId>wiremock-jre8</artifactId>
      <version>2.27.1</version>
      <scope>test</scope>
    </dependency>
```

* Creamos una clase llamada ProductInventoryWiremock que va implementar QuarkusTestResourceLifecycleManager
* Vamos a implementar onStart y onStop que se llamaran al inicio de un test unitario y al final respectivamente 
* Implementamos ambos métodos y cambiamos la clase unitaria para que use el mock @QuarkusTestResource(ProductInventoryWiremock.class)
* Comprobamos que ahora podemos ejecutar test unitarios sin necesidad de tener los servicios up and running

Hemos aprendido a como implementar test unitarios robustos sin necesidad de tener los servicios. Eso no excluye
que no pongamos disponer de una bateria de tests importante que pruebe la integracion correcta entre los microservicios.



