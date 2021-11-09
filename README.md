# Quarkus esencial
## 08_08 Añadir chequeo de salud (Health Check) con el estándar Microprofile y Quarkus

* Arrancamos minikube y tenemos todo instalado

* Añadimos extension `smallrye-health` en Product Inventory
```shell
./mvnw quarkus:add-extension -Dextensions="smallrye-health"
```
* Desplegamos en kube de nuevo
```shell
./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true 
```

* Exportamos la URL de minikube
```properties
export PRODUCT_INVENTORY=$(minikube service --url product-inventory-service) 
```

* Probamos los endpoint de salud
```shell
http $PRODUCT_INVENTORY/q/health
http $PRODUCT_INVENTORY/q/health/live
http $PRODUCT_INVENTORY/q/health/ready
```

Podemos probar como escalar up y down la base de datos por ejemplo.