# Quarkus esencial
## 08_03 Acceso a base de datos reactivo con Hibernate Reactive y Quarkus

* Añadimos las extensiones necesarias
```shell
  ./mvnw quarkus:remove-extension -Dextensions="quarkus-jdbc-postgresql,quarkus-hibernate-orm-panache,quarkus-jdbc-h2"
  ./mvnw quarkus:add-extension -Dextensions="quarkus-reactive-pg-client,quarkus-hibernate-reactive-panache"
 ```

* Importamos el buen Panache Entity
```java
 public static Uni<ProductInventory> findBySku(String sku) {
      return find("sku", sku)
      .<ProductInventory>firstResult()
      .onItem().ifNull().failWith(new NotFoundException());
      }

public static Uni<Integer> findCurrentStock(String sku) {
      Uni<UnitsAvailable> unitsAvailable =
      find("sku", sku).project(UnitsAvailable.class).firstResult();

      return unitsAvailable
      .onItem().ifNotNull().transform(e -> e.unitsAvailable)
      .onItem().ifNull().failWith(() -> new NotFoundException());
      }
```

* List

```java
public Uni<List<ProductInventory>> listInventory(@RestQuery("page") Integer page,
@RestQuery("size") Integer size) {
      LOGGER.debug("Product inventory list");
      Uni<List<ProductInventory>> fullInventory;
      if (page == null && size == null) {
      fullInventory = ProductInventory.findAll(Sort.by("name")).list();
      } else {
      PanacheQuery<ProductInventory> query = ProductInventory.findAll(Sort.by("name"));
      fullInventory = query.page(page, size).list();
      }
      return fullInventory;
      }
```

* Create
```java
@ReactiveTransactional
public Uni<Response> createProduct(@Valid @ConvertGroup(to = ValidationGroups.Post.class) ProductInventory productInventory,
@Context UriInfo uriInfo) {
      LOGGER.debugf("create %s", productInventory);
      return productInventory.<ProductInventory>persist()
      .map(p -> {
      UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(p.sku);
      LOGGER.debugf("New product created with sku %s", p.sku);
      return Response.created(builder.build()).build();
      });
      }
```

* Update

```java
@ReactiveTransactional
public Uni<Response> updateProduct(String sku, @ConvertGroup(to = ValidationGroups.Put.class)  @Valid ProductInventory productInventory) {
      return ProductInventory.findBySku(sku)
      .map(retrieved -> {
      retrieved.name = productInventory.name;
      retrieved.category = productInventory.category;
      return retrieved;
      })
      .map(p -> {
      LOGGER.debugf("Product updated with new valued %s", p);
      return Response.ok(p).build();
      });
      }
```
* Delete

```java
 @ReactiveTransactional
public Uni<Response> delete(String sku) {
      LOGGER.debugf("delete by sku %s", sku);
      return ProductInventory.delete("sku", sku)
      .invoke(() -> LOGGER.debugf("deleted with sku %s", sku))
      .onItem().transform(d -> d > 0 ?  Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build());
      }
```

* Upgrade stock
```java
@PATCH
@Path("/{sku}")
@APIResponse(responseCode = "202")
@APIResponse(responseCode = "404", description = "No product")
@ReactiveTransactional
public Uni<Response> updateStock(String sku, @RestQuery("stock") Integer stock) {
      return ProductInventory.findCurrentStock(sku)
      .onItem().call(currentStock -> {
      LOGGER.debugf("update stock for sku %s with current stock %d with %d", sku, currentStock, stock);
      return ProductInventory.update("unitsAvailable = ?1 where sku= ?2", currentStock + stock, sku);
      })
      .onItem().transform(u -> Response.accepted().build());
      }
```

* Products count

```java
@ReactiveTransactional
public Uni<Long> productsCount(ProductLine productLine) {
    LOGGER.debug("Count productLines");
    return ProductInventory.count("productLine", productLine);
}
```
En este video hemos aprendido como cambiar un API rest desde el servicio hasta la base de datos utilizando Hibernate y Rest Easy Reactivo.

