# Quarkus esencial
## 03_02 Ciclo de vida de las aplicaciones en Quarkus

Introducción en power point.

* Quarkus está arrancado en modo desarrollo
* Modificamos la clase ProductInventoryService para añadirle los eventos @Observes StartupEvent ev
* Añadimos ahi el código de loading de datos
* Lo mismo void onStop(@Observes ShutdownEvent ev)
* Eliminamos el banner de Quarkus para añadir el nuestro
* @Priority(value = 1)

Hemos aprendido más cómo funciona el ciclo de vida de las aplicaciones en Quarkus y cuales son los eventos que se ejecutan
en ese mismo ciclo. Estos eventos son útiles para tareas de configuración del estato inicial.
Utilizaremos la inyección de dependencias y las anotaciones continuamente con las diferentes extensiones de Quarkus.

