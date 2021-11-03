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
```java
@Entity
public class ProductInventory extends PanacheEntity {
   
}
```  
* Vamos a indicar que las enumeraciones son String con `@Enumerated(EnumType.STRING)`
```java
@Enumerated(EnumType.STRING)
private ProductLine productLine;

@Enumerated(EnumType.STRING)
private ProductAvailability productAvailability;

```  
* Para map de una lista de enumeraciones usamos el conversor
```java
 @Convert(converter = ConsumerTypeConverter.class)
 private List<ConsumerType> targetConsumer = new ArrayList<>();
```

* Implementamos listAll

```java
@GET
@Produces(MediaType.APPLICATION_JSON)
public Collection<ProductInventory> listInventory() {
    LOGGER.debug("Product inventory list");
    return ProductInventory.listAll();
}
```
* Implementamos inventory
```java
@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/{sku}")
public Response inventory(@PathParam("sku") String sku) {
    LOGGER.debugf("get by sku %s", sku);
    ProductInventory productInventory = ProductInventory.findBySku(sku);

    if (productInventory == null) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    return Response.ok(productInventory).build();
}
    
ProductInventory.java
public static ProductInventory findBySku(String sku) {
   return find("sku", sku).firstResult();
}
```

* CRUD
Create
```java
@POST
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public Response createProduct(@Valid @ConvertGroup(to = ValidationGroups.Post.class) ProductInventory productInventory) {
    LOGGER.debugf("create %s", productInventory);
    productInventory.persist();
    return Response.created(URI.create(productInventory.sku)).build();
}
```  
Update
```java
@PUT
@Path("/{sku}")
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public Response updateProduct(@PathParam("sku") String sku, @ConvertGroup(to = ValidationGroups.Put.class)  @Valid ProductInventory productInventory) {
      LOGGER.debugf("update %s", productInventory);
      ProductInventory existingProduct = ProductInventory.findBySku(sku);
      if (productInventory == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
      }
      existingProduct.name = productInventory.name;
      existingProduct.category = productInventory.category;
      existingProduct.persist();
      return Response.accepted(productInventory).build();
}
```
Delete
```java
@DELETE
@Path("/{sku}")
@Transactional
public Response delete(@PathParam("sku") String sku) {
    LOGGER.debugf("delete by sku %s", sku);
    ProductInventory.delete("sku", sku);
    return Response.accepted().build();
}
```

* Update stock
```java
@PATCH
@Path("/{sku}")
@Operation(summary = "Update the stock of a product by sku.", description = "Longer description that explains all.")
@Transactional
public Response updateStock(@PathParam("sku") String sku, @QueryParam("stock") Integer stock) {
    LOGGER.debugf("get by sku %s", sku);
    int currentStock = ProductInventory.findCurrentStock(sku);
    ProductInventory.update("unitsAvailable = ?1 where sku= ?2", currentStock + stock, sku);
    return Response.accepted(sku).build();
}
```
ProductInventory
```java
public static int findCurrentStock(String sku) {
  UnitsAvailable unitsAvailable = find("sku", sku).project(UnitsAvailable.class).firstResult();

  if (unitsAvailable == null) {
     return 0;
  }

  return unitsAvailable.unitsAvailable;
}
```

Si quisiéramos cortar el acceso a la base de datos para probar los recursos y el api por un lado sin base de datos,
nos podemos encontrar con el problema de mock con mockito. Pero afortunadamente utilizando la extension quarkus-panache-mock
podemos desacoplar los tests.

Hemos aprendido como arrancar en modo desarrollo y modo test dos bases de datos sin añadir ninguna configuración, y
como gracias a un script la base de datos se crea y podemos empezar a focalizarnos en nuestra logica de negocio.
Comprobamos además como el patron ActiveRecord con Hibernate Panache simplica la ló