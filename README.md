# Quarkus esencial
## 07_02 Tolerancia a fallos con Quarkus: Retry

Cuando una llamada falla, puede ser que sea por un fallo temporal o una latencia concreta. En algunos casos queremos
que automáticamente se vuelva a intentar la llamada, al menos unas cuantas veces más, para evitar que nuestro servicio que
depende de una llamada a otro falle a menudo.

Para ello utilizaremos la estrategia Retry.

* Nos aseguramos que la extension `quarkus-smallrye-fault-tolerance` está como dependencia del servicio Quarkus.
* Abrimos `ProductInventoryService` y vamos a utilizar la anotacion `@Retry`
* Lanzamos el test y vemos que tarda más en ejecutarse. 
* Explicamos diferentes opciones en la anotacion

- maxRetrys que es 3 por defecto, nos permite configurar cuantas veces queremos volver a llamar el servicio antes de 
  fallar definitivamente
- delay: indica cuanto tiempo debe esperarse entre 
- retryOn: permite filtrar el tipo de excepción por la que queremos re intentarlo. Al utilizar la estrategia timeout,
si ponemos la excepción se re intentará la llamada en caso de obtener un TimeoutException gestionado por Timeout.
- jitter permite incrementar o decremetar el intervalo de tiempos entre las llamadas.


Hemos aprendido como re intentar una llamada fallida, en el caso de un timeout por ejemplo.
La estrategia Retry debe de utilizarse con precaución, ya que reintentar multiples veces en intervalos cortos un servicio
que esté sobrecargado por alguna razón puede empeorar la situación.
    

