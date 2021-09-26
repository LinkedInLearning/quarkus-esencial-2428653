# Quarkus esencial
## 05_04 Despliegue en Kubernetes del Microservicio Quarkus y la base de datos

* Vamos a añadir la extension `quarkus-kubernetes-config` que nos permite leer ConfigMaps y Secrets de Kubernetes.

* Arrancamos minikube 
* Configuramos los secretos en kubernetes
```shell
kubectl delete secret kineteco-credentials 
kubectl create secret generic kineteco-credentials --from-literal=username=kineteco --from-literal=password=kineteco
```


* Desplegamos un servicio postgres en kubernetes  
```shell
kubectl apply -f kubernetes/postgres.yaml
```

* Activamos kubernetes config en nuestra servicio
```properties
%prod.quarkus.kubernetes-config.enabled=true
%prod.quarkus.kubernetes-config.secrets.enabled=true
%prod.quarkus.kubernetes-config.secrets=kineteco-credentials 
```

* Configuramos el acceso a la base de datos de nuestro servicio
```properties
%prod.quarkus.datasource.db-kind=postgresql
%prod.quarkus.datasource.username=${username}
%prod.quarkus.datasource.password=${password}
%prod.quarkus.datasource.jdbc.url=jdbc:postgresql://postgres.default:5432/kineteco
```

* Desplegamos el sevicio de nuevo
```shell
eval $(minikube -p minikube docker-env)
./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true
```


#Limpiar Kubernetes

```
eval $(minikube -p minikube docker-env)
kubectl delete service postgres    
kubectl delete deployment postgres
kubectl delete service product-inventory-service      
kubectl delete deployment product-inventory-service
docker rmi (imageId)
`./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true`
```