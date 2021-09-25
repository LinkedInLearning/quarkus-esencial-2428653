# Quarkus esencial
## 04_01 Crear un API CRUD con Quarkus y la extension RESTEasy de Quarkus

La creación de APIs en REST es hoy en día fundamental en la escritura de Microservicios. Nos vamos a focalizar en 
utilizar los verbos HTTP para crear un API en Quarkus que nos permita gestionar
el inventario de productos.

* Arrancamos Quarkus en modo desarrollo
* Abrimos la clase REST, bySku está ya. 
* Abrimos el test y escribimos un nuevo método que llamara a un servicio que va a crear un nuevo producto 
con un json que contendrá únicamente el sku. 
* Implementamos el método en el api rest
* Vamos a utilizar PUT para modificar el nombre, añadiendoo el test unitario.
* Implementamos el código
* Vamos a utilizar DELETE para borrar el producto creado, partiendo del test primero
* Implementamos DELETE
* Por ultimo creamos un método que use PATCH por no ser indempotente, para cambiar el stock.
* Implementamos el método. Para el stock utilizamos parametros de Query porque el numero de stock no forma parte del
resource.
  
Hemos aprendido como crear una API REST para implementar un API sencilla de gestion del inventario de Productos
utilizando los verbos HTTP POST, PUT, DELETE, GET y PATCH.
  



# Cleanup kubernetes
```
eval $(minikube -p minikube docker-env) 
kubectl delete service product-inventory-service      
kubectl delete deployment product-inventory-service
docker rmi (imageId)
`./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true`
```