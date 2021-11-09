                     ## Minikube en Mac
Instalar Virtual box es lo màs recomendado.

## Arrancar Minikube
```shell
minikube start --driver=virtualbox --cpus 4 --memory "8192mb" 
```
## Limpiar Kubernetes (Minikube)
```shell
eval $(minikube -p minikube docker-env)
kubectl delete service postgres    
kubectl delete deployment postgres
kubectl delete service product-inventory-service      
kubectl delete deployment product-inventory-service
kubectl delete service sales-service      
kubectl delete deployment sales-service
kubectl delete service order-service      
kubectl delete deployment order-service
docker rmi (imageId)
kubectl delete configmap kineteco
kubectl delete secret kineteco-credentials
kubectl delete namespace kafka

# delete all pods
kubectl delete --all pods --namespace=default
kubectl delete --all pods --namespace=kafka


# delete all deployments
kubectl delete --all deployments --namespace=default
kubectl delete --all deployments --namespace=kafka
 
 # delete all services
kubectl delete --all services --namespace=default
kubectl delete --all services --namespace=kafka
```

## Comandos útiles

```shell
kubectl create secret generic kineteco-credentials --from-literal=username=kineteco --from-literal=password=kineteco
kubectl get configmaps
kubectl get configmaps kineteco -o yaml     
kubectl get secrets
```
