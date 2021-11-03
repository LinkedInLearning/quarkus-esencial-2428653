# Quarkus esencial
## 05_04 Acceso a base de datos utilizando el patrón Data Repository con Quarkus

* Creamos el repositorio

```java
  @ApplicationScoped
public class ProductInventoryRepository implements PanacheRepository<ProductInventory>  {
   private static final Logger LOGGER = Logger.getLogger(ProductInventoryRepository.class);

}
```

* Anotamos con `@Entity` ProductInventory y añadimos un ID
```java
@Entity
public class ProductInventory {
   @Id @GeneratedValue private Long id;
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

* Inyectamos ProductInventoryRepository en el ProductInventoryResource
```java
@Inject
ProductInventoryRepository productInventoryRepository;
```

* Creamos list all
```java
@GET
@Produces(MediaType.APPLICATION_JSON)
public Collection<ProductInventory> listInventory() {
    LOGGER.debug("Product inventory list");
    return productInventoryRepository.listAll();
}
```

* Get by sku

```java
@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/{sku}")
public Response inventory(@PathParam("sku") String sku) {
    LOGGER.debugf("get by sku %s", sku);
    ProductInventory productInventory = productInventoryRepository.findBySku(sku);
    return Response.ok(productInventory).build();
}

// Repository
public ProductInventory findBySku(String sku) {
    LOGGER.debugf("find by sku %s", sku);
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
    productInventoryRepository.persist(productInventory);
    return Response.created(URI.create(productInventory.getSku())).build();
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
    ProductInventory existingInventory = productInventoryRepository.findBySku(sku);
    existingInventory.setName(productInventory.getName());
    existingInventory.setCategory(productInventory.getCategory());
    //...
    productInventoryRepository.persist(existingInventory);
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
    productInventoryRepository.delete("sku", sku);
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
    int currentStock = productInventoryRepository.getStock(sku);
    productInventoryRepository.update("unitsAvailable = ?1 where sku= ?2", currentStock + stock, sku);
    return Response.accepted(URI.create(sku)).build();
}
```
ProductInventory
```java
public int getStock(String sku) {
    LOGGER.debugf("get stock sku %s", sku);
    UnitsAvailable unitsAvailable = find("sku", sku).project(UnitsAvailable.class).firstResult();
    
    if (unitsAvailable == null) {
    return 0;
    }
    return unitsAvailable.unitsAvailable;
}
```
