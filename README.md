# Quarkus esencial
## 02.06 Compilación nativa y Contenedores con Quarkus

Uno de las necesidades fundamentales en los entornos de Cloud y microservicios es la utilizacion eficaz de los recursos
de los que disponemos. 
Quarkus y la compilacion nativa nos permite desplegar aplicaciones Java como si fuesen ejecutables nativos, por lo que
no necesitamos que nuestro contendor instale ningun runtime de Java para poder ejecutarlo ya que los ejecutables nativos
se ejecutan directamente por el sistema operativo.

Veremos ahora como funciona.

- Tenemos que tener instalado ${GRAALVM_HOME}/bin/gu install native-image para utilizar GRAAL_VM para crear los ejecutables nativos.
  Mac puede dar algun problema a la hora de instalar native-image, con `xattr -r -d com.apple.quarantine ${GRAALVM_HOME}/../..` se
  soluciona el problema.
  
* Mover el codigo a un HASHMAP en la creación de la clase
* Cambiar el test para que si un valor no se encuentre responda 404
* Cambiar el código para utilizar Response en vez del objeto
* Compilar nativamente el código. Explicar en ese paso todo lo que viene en maven.
* Hacer run del ejecutable que se ha creado
* Vemos que hay un problema con el servicio. Explicar que la compilacion nativa borra código que no se usa.
* Ademas ponemos el ejecutable en un container docker. La aplicacion no arranca porque estamos en mac.  
* Utilizar @RegusterForReflection para solucionar el primer problema
* Volver a compilar esta vez utilizando el flag ./mvnw package -Pnative -Dquarkus.native.container-build=true  
* Comprobar que todo funciona

Hemos aprendido como desplegar en un contenedor con la linea de comandos y solucionado problemas comunes con los que nos encontraremos
en caso de utilizar la compilacion nativa en vez de aplicaciones JVM. 