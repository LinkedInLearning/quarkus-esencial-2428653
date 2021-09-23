# Quarkus esencial
## 02.03 Ejecuta tu primera aplicación en modo desarrollo en Quarkus
* ./mvn package quarkus:dev
* Crearemos una entidad en memoria y añadiremos un servicio REST nuevo con Quarkus para leerla

Una de los focos principales de Quarkus desde su creación es la felicidad de los desarrolladores, desde el
entorno de desarrollo hasta la puesta en producción de las aplicaciones.
En este video aprenderemos a utilizar Quarkus en modo desarrollo y descubriremos toda su potencia en el desarrollo de
aplicaciones Java.

* Arrancamos la terminal con quarkus ./mvn package quarkus:dev
* Vamos a localhost:8080
* Creamos un nuevo endpoint GET con el sku como @PathParam
* En el cuerpo creamos un nuevo objeto ProductInventory del modelo
* Vamos al navegador y vemos que el nuevo endpoint funciona pero nos da un error de serializacion de datos
* Añadimos la dependencia REST Easy Jackson, pero podemos utilizar JSON B.
* Comprobamos que la aplicación arranca sola y que el despliegue de los cambios

En este video hemos aprendido cómo funciona Quarkus en modo desarrollo y comprobado cómo gracias a sus desploegues en continuo
podemos mejorar significativamente nuestros tiempos de desarrollo y concentrarnos en el código y funcionalidades que
queremos desarrollar.
  

