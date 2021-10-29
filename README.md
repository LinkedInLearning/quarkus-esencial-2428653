# Quarkus esencial
## 04_03 Gestión de errores personalizados en Quarkus con ExceptionMapper

La forma en gestionar las respuestas y la forma de presentar los errores a los consumidores de un API Rest es
importante para la calidad de un API Rest.
Vamos a aprender como poder personalizar el mensaje de salida de la validación de un API.

* Utilizamos el API de crear productos con 
```shell
http post http://localhost:8080/products/ nada=nada
```

Monstramos el error, con demasiados campos.

```json
HTTP/1.1 400 Bad Request
Content-Length: 353
Content-Type: application/json
validation-exception: true

{
    "classViolations": [],
    "parameterViolations": [
        {
            "constraintType": "PARAMETER",
            "message": "Name is mandatory and should be provided",
            "path": "createProduct.productInventory.name",
            "value": ""
        },
        {
            "constraintType": "PARAMETER",
            "message": "must not be blank",
            "path": "createProduct.productInventory.sku",
            "value": ""
        }
    ],
    "propertyViolations": [],
    "returnValueViolations": []
}

```

* Vamos a usar jsonb ahora 
  
* Creamos la clase KinetecoExceptionMapper en el paquete validation
  
```java
@Provider
public class KinetecoExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
   
   1)
   @Override
   public Response toResponse(ConstraintViolationException e) {
      return Response
            .status(Response.Status.BAD_REQUEST)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .entity(e.getConstraintViolations().toString()).build();
   }

   2)
   private JsonObject errorMessage(ConstraintViolationException e) {
      JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

      for (ConstraintViolation v : e.getConstraintViolations()) {
         objectBuilder.add(v.getPropertyPath().toString(), v.getMessage());
      }
      return objectBuilder.build();
   }
   
   3)
   @Override
   public Response toResponse(ConstraintViolationException e) {
      return Response
            .status(Response.Status.BAD_REQUEST)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .entity(errorMessage(e)).build();
   }
}
```
* La clase implementa implements ExceptionMapper<ConstraintViolationException>. Implementamos con Json.
  Un apunte sobre este API. Es el estándar viene con JsonB. Para eso he cambiado el marshalling de Jackson a Json b. El
  api es estandard y limpio para manipular json en java.

* Vamos a utilizar una de las anotaciones de JAX-RS. @Provider. Esta anotación nos permite cambiar el funcionamiento en
  ejecucción de la forma de funcionar de RestEasy en este caso y cambiar su comportamiento. En este caso vamos a proveer
  una instancia que controla la asignacion de exceptiones.
  
* Volvemos a probar y vemos que la salida es mucho mas limpia.

```json
HTTP/1.1 400 Bad Request
Content-Length: 139
Content-Type: application/json

{
"createProduct.productInventory.name": "Name is mandatory and should be provided",
"createProduct.productInventory.sku": "must not be blank"
}
```

Hemos aprendido a utilizar un proveedor de excepciones y así modificar el comportamiento y la valicación de campos de nuestra API
REST para un control de errores más limpio.
