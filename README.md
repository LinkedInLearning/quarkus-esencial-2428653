# Quarkus esencial
## 07_04 Tolerancia a fallos con Quarkus: Fallback

Fallback nos permite delegar el tratamiento de error a otro método, y así hacer un mejor gestion de errores.


* Abrimos `SalesResource`. Vamos a ejecutar otro en caso de fallo product inventory service.
* Anotamos el método `available` con `@Fallback` y metodo fallbackAvailable.
* Creamos el método `fallbackAvailable`, tiene que tener los mismos parámetros de entrada que el método original y pertenecer a
  la misma clase
* Vamos a suponer una respuesta funcional, de que nuestros productos se reponen a menudo, por lo que si el inventario pedido es 
menor o igual a 5 unidades, diremos que están disponible. En el caso contrario, diremos que no.
* Ejecutamos el test y vemos que hay cambios porque el timeout ya no falla a error 500, sino que responde 200.
* Cambiamos el test y probamos el caso de menos de 5 unidades.

Hemos aprendido a controlar mejor los errores y añadir robustez en los métodos usando la estrategia de tolerancia a fallos.


