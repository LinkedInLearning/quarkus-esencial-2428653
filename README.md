# Quarkus esencial
## 02_06 Despliegue en Kubernetes de tu primera aplicación Quarkus

Una vez sabemos como crear contenedores, vamos aprender como desplegarlos en kubernetes con Quarkus en unas lineas de comando.

En este workshop no vamos a ir al mejor lugar del mundo, como dice siempre Josh Long, que es Producción, sino que nos
quedaremos a aprender en nuestro local.
Utilizaremos Minikube por ser una solución popular y simple para poder hacer un despliegue sencillo de un cluster de
Kubernetes en local. ¡No utilizar Minikube en producción!

* Para poder desplegar en Kubernetes tenemos que escribir la configuración YAML. La extension quarkus-kubernetes
  nos ayuda a ello. (añadir extension)
* cambiar las propiedades kubernetes para que el fichero yaml
```
quarkus.container-image.group
quarkus.container-image.name
quarkus.kubernetes.name
```
* Añadir extension minikube que nos permite generar ficheros extra para el despliegue

* Primer paso arrancar minikube.
  `minikube start --cpus 4 --memory "8192mb"`
  Suelo darle bien de memoria y más CPUs. Para estos ejemplos utilizo minikube con el driver de docker, pero
  VirtualBox para los usuarios de Windows y Mac es una muy buena opción.

* minikube service list comprobamos que estamos en el namespace 'default'.
* Para empaquetar las aplicaciones y desplegarlas en kubernetes tenemos diferentes alternativas. Vamos a utilizar la extension de jib
  https://github.com/GoogleContainerTools/jib que es una herramienta de Google por disponer de todo lo necesario.

* Vamos a ejecutar el comando utilizado anteriormente crear el empaquetado en un contenedor
  `./mvnw clean package -Dquarkus.container-image.build=true`

* Para ver las images `docker images`
* Despliegue en Kubernetes:
  `./mvn clean package -Dquarkus.kubernetes.deploy=true`

Hemos aprendido como desplegar nuestro primer servicio en un cluster de kubernetes en local. 