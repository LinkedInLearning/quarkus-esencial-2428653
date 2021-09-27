# Quarkus esencial
## 07_02 Tolerancia a fallos con Quarkus: Retry

Cuando una llamada falla, puede ser que sea por un fallo temporal o una latencia concreta. En muchos casos queremos
que automáticamente se vuelva a intentar la llamada, al menos unas cuantas veces más, para evitar que nuestro servicio que
depende de una llamada a otro falle a menudo.

Para ello utilizaremos la estrategia Retry.

* Nos aseguramos que la extension `quarkus-smallrye-fault-tolerance` está como dependencia del servicio Quarkus.
* Abrimos `ProductInventoryService` y vamos a utilizar la anotacion `@Retry`


