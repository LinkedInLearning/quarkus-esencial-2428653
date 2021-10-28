# Quarkus esencial
## 03_06 Declarar Perfiles de Quarkus: Dev, Test y Prod

En cualquier aplicación tenemos la necesidad de distinguir los perfiles de desarrollo, de testing y de producción 
concretamente en lo que a la configuración se refiere. 
Quarkus viene con 3 perfiles definidos "dev, test y prod", pero podemos definir también nuestros propios perfiles.

* Abrimos el fichero `application.properties`
  
* Configuramos
```properties
quarkus.application.name=Product Inventory 
%dev.quarkus.application.name=${com.kineteco.service} Dev Mode
%test.quarkus.application.name=${com.kineteco.service} Test Mode
%prod.quarkus.application.name=${com.kineteco.service} Production
```
* Arrancamos en modo desarrollo

 Vemos en los logs

 ``` 
 Listening on: http://localhost:8080
 Product Inventory Dev Mode 1.0.0-SNAPSHOT on JVM 
 (Quarkus Main Thread) Profile dev activated. Live Coding activated.
 Product Inventory Dev Mode stopped in 0.014
 ```
* Ejecutamos un test unitario en el editor
  Vemos en los logs
```shell
   Listening on: http://localhost:8081
   2021-09-25 11:52:48,923 INFO  [io.quarkus] (main) Profile test activated.
   2021-09-25 11:52:48,923 INFO  [io.quarkus] (main) Installed features: [cdi, hibernate-validator, kubernetes, kubernetes-client, resteasy, resteasy-jackson, smallrye-context-propagation]
   Product Inventory Test Mode
```

* Lanzamos la aplicación en JVM
```shell
  java -jar target/quarkus-app/quarkus-run.jar`
````
  Comprobamos que puede haber un conflicto con el puerto si tenemos la aplicación en modo desarrollo. 
```shell
===========> onStart
===========> loaded 52
2021-09-25 11:56:46,316 INFO  [io.quarkus] (main) Product Inventory Production 1.0.0-SNAPSHOT on JVM (powered by Quarkus 2.2.3.Final) started in 1.818s. Listening on: http://0.0.0.0:8080
2021-09-25 11:56:46,319 INFO  [io.quarkus] (main) Profile prod activated. 
2021-09-25 11:56:46,319 INFO  [io.quarkus] (main) Installed features: [cdi, hibernate-validator, kubernetes, kubernetes-client, resteasy, resteasy-jackson, smallrye-context-propagation]
^C===========> onStop
2021-09-25 11:57:36,095 INFO  [io.quarkus] (Shutdown thread) Product Inventory Production stopped in 0.032s

```
* Si queremos activar un perfil en concreto, simplemente declaramos uno nuevo y lo pasamos en cualquiera de los entornos con una variable
de entorno
```properties
  %preprod.quarkus.application.name=${kineteco.service} Perfil Custom Pre Production```
```

Probar el preprod en pre prod con la variable de entorno quarkus-profile

```shell
./mvnw quarkus:dev -Dquarkus-profile=preprod  -Dquarkus-profile=preprod
``` 

* Usaremos ConfigMap de Kubernetes. Añadimos la extension
```shell
./mvnw quarkus:add-extension -Dextensions="kubernetes-config"
```
* Verificamos que Minikube está arrancado
```shell
minikube status
eval $(minikube -p minikube docker-env)
```
  
* Usamos el fichero `application.yaml` y vamos a crear el config map en minikube

* Pasos para crear el config map
```shell
kubectl create configmap kineteco --from-file=application.yaml
kubectl get configmaps
kubectl get configmaps kineteco -o yaml
kubectl create secret generic kineteco-credentials --from-literal=username=admin --from-literal=password=password
kubectl get secrets
```  
* Configurar servicio para usar el config map
  
```properties
  %prod.quarkus.kubernetes-config.enabled=true
  %prod.quarkus.kubernetes-config.config-maps=kineteco
  %prod.quarkus.kubernetes-config.secrets.enabled=true
  %prod.quarkus.kubernetes-config.secrets=kineteco-credentials
```

* Desplegamos la aplicación en kubernetes y comprobamos que la configuración de producción se lee de kubernetes
```shell
./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true`
```
  
En este video hemos aprendido lo sencillo que es en Quarkus configurar nuestra aplicaciones para que utilicen diferentes
perfiles en función de las necesidades de cada entorno, además de lo sencillo que es utilizar la extension de kubertenes
config para facilitarnos el despliegue en kubernetes.
