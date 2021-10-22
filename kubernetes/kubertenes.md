## Minikube en Mac
Instalar Virtual box es lo màs recomendado.

## Arrancar Minikube
```shell
minikube start --driver=virtualbox --cpus 4 --memory "8192mb" --network-plugin=cni
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
docker rmi (imageId)
```

## Comandos útiles

```shell
kubectl get configmaps
kubectl get secrets
```
