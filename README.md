# Quarkus esencial
## 04_01 Crear un API CRUD con Quarkus y la extension RESTEasy de Quarkus


# Cleanup kubernetes
```
eval $(minikube -p minikube docker-env) 
kubectl delete service product-inventory-service      
kubectl delete deployment product-inventory-service
docker rmi (imageId)
`./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true`
```