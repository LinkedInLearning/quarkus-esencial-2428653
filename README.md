# Quarkus esencial
## 03_05 Declarar Perfiles de Quarkus: Dev, Test y Prod

`./mvnw quarkus:add-extension -Dextensions="kubernetes-config"`

`kubectl create configmap kineteco --from-file=applicaction.yaml`

`kubectl get configmaps kineteco -o yaml`
`kubectl get configmaps`
`kubectl get secrets`

* Kubernetes Config Map
* create application.yaml with the content
* Add quarkus-kubernetes-config dependency
* Configure config maps for production environment in the application.properties
  - %prod.quarkus.kubernetes-config.enabled=true 
  - %prod.quarkus.kubernetes-config.config-maps=kineteco
* Secret      
* Build image and Deploy the app `./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true`

# Cleanup kubernetes

```
eval $(minikube -p minikube docker-env) 
kubectl delete service product-inventory-service      
kubectl delete deployment product-inventory-service
docker rmi (imageId)
`./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true`
```