# Quarkus esencial
## 05_06 Despliegue en Kubernetes del Microservicio Quarkus y la base de datos

* Añadimos la extension kubernetes config si no la tenemos
```shell
 ./mvnw quarkus:add-extension -Dextensions="quarkus-kubernetes-config"
```
* Minikube arrancado y docker eval realizados

* Vamos a crear los credenciales
```shell
kubectl create secret generic kineteco-credentials --from-literal=username=kineteco --from-literal=password=kineteco
```

* Explicamos el YAML de postgress y lo desplegamos en kubernetes
```shell
kubectl apply -f kubernetes/postgres.yaml
```
* Añadimos para importar el sql también en producción
```properties
quarkus.hibernate-orm.sql-load-script=import.sql
```  

* Configuramos el acceso a los secretos de kubernetes en nuestro servicio
```shell
%prod.quarkus.kubernetes-config.enabled=true
%prod.quarkus.kubernetes-config.secrets.enabled=true
%prod.quarkus.kubernetes-config.secrets=kineteco-credentials 
```
  
* Configuramos la connexion 
```properties
%prod.quarkus.datasource.db-kind=postgresql
%prod.quarkus.datasource.username=${username}
%prod.quarkus.datasource.password=${password}
%prod.quarkus.datasource.jdbc.url=jdbc:postgresql://postgres.default:5432/kineteco
```

* Desplegamos nuestro servicio
```shell
./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true
```