# Quarkus esencial
## 08_02 API REST Reactiva con Quarkus y RESTEasy Reactive

Vamos a aprender a convertir el API imperativa a reactiva para obtener todos los beneficios de exponr un API que 
no sea blocking para los clientes.

* arrancamos quarkus en modo desarrollo 
```shell
 ./mvnw quarkus:dev
```
* Llamamos a `http localhost:8080/products/health` y miramos los logs
  ==> 2021-10-10 11:14:05,021 DEBUG [com.kin.ProductInventoryResource] (executor-thread-0) health called

* Cambiamos nuestras dependencias reasteasy por resteasy-reactive (resteasy y jsonb)
* Vemos como se descargan, lanzamos los test y todo pasa correctamente.
  - el log cita vert.x worker thread
* Utilizamos la a anotación ```@NonBlocking``` en el método ```health```
* vemos que funciona y vemos en el log
``2021-10-10 11:15:50,136 DEBUG [com.kin.ProductInventoryResource] (vert.x-eventloop-thread-7) health called``
Es el loop de eventos de vert.x que ahora coge el mando. 
  La anotación @blocking indicará en un thread worker 
* Para evitar tener que utilizar la anotación @NonBlocking, RestEasy reactivo funciona por defecto
  - Si un método retorna un objeto, por defecto es el worker thread salvo que pongamos @NonBlocking
  - Si utilizamos Mutiny, Uni o Multi, como respuesta, se ejecurará en un I/O thread salvo que utilicemos @Blocking
    
Cuando delegamos al thread I/O estamos gestionando las peticiones de forma reactiva y incrementando rendimiento de
nuestro servicio. Para comprobarlo, podemos utilizar herramientas como "wrk".

* Convertimos con Uni el listado de productos
```java
        Uni<List<ProductInventory>>
        
        Uni.createFrom().item(itemList)
```
* Lanzamos los test y vemos que nos indica que estamos usando código blocking en un I/O thread y esto tiene consecuencias 
de rendimiento. Para ello usamos la anotación `Blocking`.

El caso es que si nos fijamos bien, el problema es que no es suficiente con utilizar REST Easy reactive y poder utilizar Multi
o Uni y crear stream de datos.
Toda la stack hasta la base de datos tiene que ser reactiva. 

