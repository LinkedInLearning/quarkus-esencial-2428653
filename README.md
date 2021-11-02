# Quarkus esencial
## 07_04 Tolerancia a fallos con Quarkus: Fallback

Fallback nos permite delegar el tratamiento de error a otro método, y así hacer un mejor gestion de errores.


* Abrimos `SalesResource`. Vamos a ejecutar otro en caso de fallo product inventory service.
* Anotamos el método `available` con `@Fallback` y método fallbackAvailable.
  
```java
@Fallback(fallbackMethod = "fallbackAvailable", applyOn = TimeoutException.class)
```
* Creamos el método `fallbackAvailable`, tiene que tener los mismos parámetros de entrada que el método original y pertenecer a
  la misma clase
```java
public Response fallbackAvailable(String sku, Integer units) {
  if (units <= 2) {
      return Response.ok(true).build();
  }
  return Response.status(Response.Status.GATEWAY_TIMEOUT).build();
}
```  

* Creamos Fallback handler

```java
package com.kineteco;

import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.jboss.logging.Logger;

import javax.ws.rs.core.Response;

public class AvailableProductFallbackHandler implements FallbackHandler<Response> {
   private static final Logger LOGGER = Logger.getLogger(AvailableProductFallbackHandler.class);

   @Override
   public Response handle(ExecutionContext context) {
      String exceptionName = "";
      if (context.getFailure().getCause() == null) {                 
         exceptionName = context.getFailure() .getClass().getSimpleName();
      } else {
         exceptionName = context.getFailure().getCause().getClass().getSimpleName();
      }

      LOGGER.debugf("Handle exception %s", exceptionName);

      if (exceptionName.equals(TimeoutException.class.getSimpleName())) {
         int units = (int) context.getParameters()[1];
         if (units <= 2) {
            return Response.ok(true).build();
         } else {
            return Response.status(Response.Status.GATEWAY_TIMEOUT).build();
         }
      }

      if (exceptionName.equals("ResteasyWebApplicationException")) {
         return Response.status(Response.Status.BAD_GATEWAY).build();
      }

      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
   }
}

```

* Anotamos el metodo
```java
    @Fallback(value = AvailableProductFallbackHandler.class)
```
Hemos aprendido a controlar mejor los errores y añadir robustez en los métodos usando la estrategia de tolerancia a fallos.


