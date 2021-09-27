# Quarkus esencial
## 07_02 Tolerancia a fallos con Quarkus: Timeout

La llamadas entre microservicios sufren de latencia, más o menos larga, que podemos considerar normal. 
Los tiempos de respuesta optimos es algo que debemos de decidir en cada caso.
Para evitar que un hilo de llamada externa se quede bloqueado demasiado tiempo, utilizaremos la estrategia timeout.

* Añadimos la extensión `smallrye-fault-tolerance`
```shell
./mvnw quarkus:add-extension -Dextensions="smallrye-fault-tolerance" 
```
* Abrimos ProductInventoryService en sales service, la clase que encapsula la llamada externa.
* Anotamos con `@Timeout`. Enseñamos que por defecto son 1000 ms, podemos darle otro valor
Esta anotación hará que un TimeoutException sea lanzada cuando el servicio no responde antes de 1000 ms.
* Para probar el funcionamiento, vamos a utilizar el test que emula el servicio externo con WireMock
* Creamos un nuevo test para probar el timeout

```java
 @Test
   public void testAvailabilityTimeout() {
      given()
            .queryParam("units", 43)
            .when().get("/sales/{sku}/availability", "falloTimeout")
            .then()
            .statusCode(200);
   }
```

* Añadimos un nuevo caso de mock en `ProductInventoryWiremock`. 
  `withFixedDelay(2000)` significa que el servicio para el id 'falloTimeout' tardará 2 segundos en responder.
  
```java
 stubFor(get(urlEqualTo("/products/falloTimeout/stock"))
          .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withFixedDelay(2000)
                .withBody("42")
          ));
```

* Lanzamos el test unitario, vemos que la respuesta no es 200, sino 500. Cambiamos el estado a  .statusCode(200);
* Para acelerar el proceso y que no espere 1 segundo, podemos cambiar el tiempo de espera en la anotación @Timeout

Hemos aprendido a utilizar la estrategia de Timeout para forzar el fallo a aplicar en tiempos de respuesta demasiado 
largos y no aceptables para nuestro servicio y evitar que los recursos se queden bloquedos demasiado tiempo.

