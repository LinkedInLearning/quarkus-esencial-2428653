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

      given()
      .queryParam("units", 43)
      .when().get("/sales/{sku}/availability", "123")
      .then()
      .statusCode(200)
      .body(is("false"));
      }
```
Comprobamos que falla por no tener el servicio up

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
```java
package com.kineteco;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class ProductInventoryWiremock implements QuarkusTestResourceLifecycleManager {
  private WireMockServer wireMockServer;

  @Override
  public Map<String, String> start() {
    wireMockServer = new WireMockServer();
    wireMockServer.start();

    stubFor(get(urlEqualTo("/products/123/stock"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody("42")
        ));

    return Collections.singletonMap("kineteco-product-inventory/mp-rest/url", wireMockServer.baseUrl());
  }

  @Override
  public void stop() {
    if (null != wireMockServer) {
      wireMockServer.stop();
    }
  }
}
```  
* Implementamos ambos métodos y cambiamos la clase unitaria para que use el mock @QuarkusTestResource(ProductInventoryWiremock.class)
```java
@QuarkusTest
@QuarkusTestResource(ProductInventoryWiremock.class)
public class SalesResourceTest {
   ...
}
```  
* Comprobamos que ahora podemos ejecutar test unitarios sin necesidad de tener los servicios up and running

Hemos aprendido a como implementar test unitarios robustos sin necesidad de tener los servicios. Eso no excluye
que no pongamos disponer de una bateria de tests importante que pruebe la integracion correcta entre los microservicios.
