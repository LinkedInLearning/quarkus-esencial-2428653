# Quarkus esencial
## 04_03 Gestión de errores personalizados en Quarkus con ExceptionMapper

La forma en gestionar las respuestas y la forma de presentar los errores a los consumidores de un API Rest es
importante para la calidad de un API Rest.
Vamos a aprender como poder personalizar el mensaje de salida de la validación de un API.

* Utilizamos el API de crear productos con http post 'http://localhost:8080/products/' 'nada=nada'

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

* Vamos a implementar un mapeo de exceptiones para personalizar este error
* Creamos la clase KinetecoExceptionMapper en el paquete validation
* La clase implementa implements ExceptionMapper<ConstraintViolationException>. Implementamos con Json.
  Un apunte sobre este API. Es el estandar viene con JsonB. Para eso he cambiado el marshalling de Jackson a Json b. El
  api es estandard y limpio para manipular json en java.
* Creamos un JSon.object y mapeamos la validación en un objeto nuevo. Y añadimos el Response con Bad Request y type
  MediaType Json.

* Vamos a utilizar una de las anotaciones de JAX-RS. @Provider. Esta anotacion nos permite cambiar el funcionamiento en
  ejecuccion de la forma de funcionar de RestEasy en este caso y cambiar su comportamiento. En este caso vamos a proveer
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