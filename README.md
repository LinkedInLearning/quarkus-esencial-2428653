# Quarkus esencial
## 05_02 Dependencias para la persistencia y Dev Services de Quarkus

En todas las aplicaciones vamos a necesitar antes o después una capa de persistencia de datos, sea en memoria o en disco,
para poder guardar los datos de forma permanente.
En el ecosistema java Hibernate es desde hace años el framework más popular para ayudarnos a escribir nuestra lógica de negocio.
Hibernate Panache nos ayuda en concreto a realizar esto con Quarkus.

* Nos aseguramos de tener docker instalado, porque utilizaremos los DEV SERVICES de docker en desarrollo.
* Añadimos las extensiones Panache, Postgres y H2 para los test.

```shell 
./mvnw quarkus:add-extension -Dextensions="quarkus-jdbc-postgresql,quarkus-hibernate-orm-panache,quarkus-jdbc-h2"`
```

* Vamos a asegurarnos que la dependencia h2 está solamente en el entorno de tests en el pom.xml
  
* Vamos a tener Docker además para ver el modo desarrollo
* Lanzamos quarkus en modo desarrollo
  
* Configuramos `quarkus.hibernate-orm.database.generation = drop-and-create`