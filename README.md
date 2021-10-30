# Quarkus esencial
## 05_02 Acceso a base de datos utilizando el patrón Active Record con Quarkus

En todas las aplicaciones vamos a necesitar antes o después una capa de persistencia de datos, sea en memoria o en disco,
para poder guardar los datos de forma permanente.
En el ecosistema java Hibernate es desde hace años el framework más popular para ayudarnos a escribir nuestra lógica de negocio.
Hibernate Panache nos ayuda en concreto a realizar esto con Quarkus.

Aprenderemos como utilizarlo con el patron Active Record.
* Nos aseguramos de tener docker instalado, porque utilizaremos los DEV SERVICES de docker en desarrollo.
* Añadimos las extensiones Panache, Postgres y H2 para los test.

```shell 
./mvnw quarkus:add-extension -Dextensions="quarkus-jdbc-postgresql,quarkus-hibernate-orm-panache,quarkus-jdbc-h2"`
```

* Vamos a asegurarnos que la dependencia h2 está solamente en el entorno de tests en el pom.xml
* Vamos a tener Docker además para ver el modo desarrollo
* Lanzamos quarkus en modo desarrollo
* Configuramos `quarkus.hibernate-orm.database.generation = drop-and-create`
* Anotamos con `@Entity` ProductInventory
* Extendemos PanacheEntity
* Nos damos cuenta de que nuestro ID es el sku, por lo que lo anotamos con @Id
* Leemos el error y nos damos cuenta que hay que extender de PanacheBaseEntity
* Vamos a indicar que las enumeraciones son String con `@Enumerated(EnumType.STRING)`
* Vamos a indicar que la Lista de Enum sea mapeada de forma custom. Existen diferentes formas de mapping entre una lista
  de enumeraciones y la base de datos. Para simplificar el desarrollo, guardamos los datos en una lista de String y lo convertimos
  a la lista de enumeraciones con la clase ConsumerTypeListConverter que se proporciona.
  `@Convert(converter = ConsumerTypeListConverter.class)`
* Cambiamos el api rest para implementar y arreglar el test que falla.
* Añadir `@Transactional` a los métodos que necesitan una tx de base de datos
* Cambiamos el código para obtener el full catalog utilizando streamAll
  http http://localhost:8080/products/KE180
* Añadimos un nuevo método para contar la catidad de productos economy y deluxe
```java
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/line/{productLine}")
    public Response economyProductsCount(@PathParam("productLine") ProductLine productLine) {
        LOGGER.debug("Economy products");
        return Response.ok(ProductInventory.count("productLine", productLine)).build();
    }

```

Si quisiéramos cortar el acceso a la base de datos para probar los recursos y el api por un lado sin base de datos,
nos podemos encontrar con el problema de mock con mockito. Pero afortunadamente utilizando la extension quarkus-panache-mock
podemos desacoplar los tests.

Hemos aprendido como arrancar en modo desarrollo y modo test dos bases de datos sin añadir ninguna configuración, y
como gracias a un script la base de datos se crea y podemos empezar a focalizarnos en nuestra logica de negocio.
Comprobamos ademas como el patron ActiveRecord con Hibernate Panache simplica la ló