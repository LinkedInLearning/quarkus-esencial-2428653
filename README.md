# Quarkus esencial
## 02_06 Despliegue en Kubernetes de tu primera aplicación Quarkus

Una vez sabemos como crear contenedores, vamos aprender como desplegarlos en kubernetes con Quarkus en unas lineas de comando.

En este workshop no vamos a ir al mejor lugar del mundo, como dice siempre Josh Long, que es Producción, sino que nos
quedaremos a aprender en nuestro local.
Utilizaremos Minikube por ser una solución popular y simple para poder hacer un despliegue sencillo de un cluster de
Kubernetes en local. ¡No utilizar Minikube en producción!

* Primer paso arrancar minikube.
```shell
  minikube start --cpus 4 --memory "8192mb" --driver=virtualbox
```

* Utilizar el docker de minikube
```shell
  eval $(minikube -p minikube docker-env)
```

* Comprobamos que estamos en el namespace 'default'.
```shell
minikube service list
``` 
  
* Para poder desplegar en Kubernetes tenemos que escribir la configuración YAML. La extension quarkus-kubernetes
  nos ayuda a ello. (añadir extension)
  
```shell
./mvnw quarkus:add-extension -Dextensions="quarkus-kubernetes,quarkus-container-image-jib,quarkus-minikube"             
```

Para empaquetar las aplicaciones y desplegarlas en kubernetes tenemos diferentes alternativas. Vamos a utilizar la extension de jib
  https://github.com/GoogleContainerTools/jib que es una herramienta de Google por disponer de todo lo necesario.

```shell
./mvnw clean package
```  

* cambiar las propiedades kubernetes para que el fichero yaml

```properties
quarkus.container-image.group=com.kineteco
quarkus.container-image.name=product-inventory-service
quarkus.kubernetes.name=product-inventory-service
```

* Vamos a ejecutar el comando utilizado anteriormente crear el empaquetado en un contenedor
```shell
  ./mvnw clean package -Dquarkus.container-image.build=true -DskipTests=true
```

* Para ver las images `docker images`
* Despliegue en Kubernetes:
 
```shell
  ./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true
```

Hemos aprendido como desplegar nuestro primer servicio en un cluster de kubernetes en local. 