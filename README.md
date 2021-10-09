# Quarkus esencial
## 07_05 Tolerancia a fallos con Quarkus: Circuit breaker

Vamos a aprender a implementar un circuit breaker en un método que disponemos ahora en ServiceResource para
crear pedidos deluxe. 

* Explicamos el ProductInventory con timeout
* Explicamos que hace ServiceResource
* Enseñamos el test relativo al servicio que funciona
* Vamos a usar @CircuitBreaker y simular llamadas que fallan por indisponibilidad del servicio.
usamos la anotación 
```java
     @CircuitBreaker(
          requestVolumeThreshold=3,
          failureRatio = 0.66,
          delay = 1,
          delayUnit = ChronoUnit.SECONDS
    )
```

* Vamos a hacer un stub con timeout
* Vamos a probar el test ```public void testDeluxeCommandCircuitBreaker()```
* Haremos un primer escenario hasta llegar a ver las 3 excepciones
* Creamos un SalesServiceFallbackHandler
```java
 public SalesServiceFallbackHandler implements FallbackHandler<Response> {
      if (context.getFailure() instanceof TimeoutException) {
       return Response.status(Response.Status.GATEWAY_TIMEOUT).build();
      }

      if (context.getFailure() instanceof CircuitBreakerOpenException) {
         return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
      }
      
      
      serverError()
}
```

* Vamos a crear el escenario
  
  .inScenario("circuitBreaker")
  .whenScenarioStateIs(Scenario.STARTED) 
  .willSetStateTo("timeout-1") OK
   - timeout-2 -> timeout2 timeout
   - timeout-2 -> success timeout
   - success OK
    
* Comprobamos que sucesivas llamadas son service unavailable

* Ponemos wait one second para que el circuito se cierre de nuevo

* Comprobamos que funciona

Hemos aprendido a utilizar una estrategia de Circuit Breaker y hemos profundizado además en la gestión de excepciones
con FallBack
  

