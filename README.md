# Quarkus esencial
## 08_03 Acceso a base de datos reactivo con Hibernate Reactive y Quarkus

* Añadimos las extensiones necesarias
```shell
  ./mvnw quarkus:remove-extension -Dextensions="quarkus-jdbc-postgresql,quarkus-hibernate-orm-panache,quarkus-jdbc-h2"
  ./mvnw quarkus:add-extension -Dextensions="quarkus-reactive-pg-client,quarkus-hibernate-reactive-panache"
 ```

* Reload maven en intellij e importamos el buen Panache Entity

* Simplificamos un poco el código de negocio y vamos a ir modificando cada parte poco a poco.

* Primero testListProducts

* inventoryEndpoint()

* Por último el CRUD. Cambiamos create

En este video hemos aprendido como cambiar un API rest desde el servicio hasta la base de datos utilizando Hibernate y Rest Easy Reactivo.

