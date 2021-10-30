# Quarkus esencial
## 05_05 Queries avanzadas con Hibernate Panache y Quarkus

* Not Found

```java
 ProductInventory existingProduct = ProductInventory.findBySku(sku);
        if (productInventory == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
 by

ProductInventory existingProduct = ProductInventory.findBySku(sku);

        
--- Probar primero con RuntimeException

public static ProductInventory findBySku(String sku) {
      return find("sku", sku)
      .<ProductInventory>firstResultOptional()
      .orElseThrow(()-> new RuntimeException());
      }

--- Cambiar a NotFoundException
public static ProductInventory findBySku(String sku) {
      return find("sku", sku)
      .<ProductInventory>firstResultOptional()
      .orElseThrow(()-> new NotFoundException());
      }

```

* Count

```java
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/line/{productLine}")
    public Response productsCount(@PathParam("productLine") ProductLine productLine) {
        LOGGER.debug("Count productLines");
        return Response.ok(ProductInventory.count("productLine", productLine)).build();
    }
```

* Paging
```java
@GET
@Produces(MediaType.APPLICATION_JSON)
public Collection<ProductInventory> listInventory(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {
      LOGGER.debug("Product inventory list");
      if (page == null && size == null) {
      return ProductInventory.listAll();
      }
      PanacheQuery<ProductInventory> query = ProductInventory.findAll();
      query.page(page, size);
      return query.list();
}

```

* Sorting

```java
///Quitamos el comentario del test y añadimos en inventario list

PanacheQuery<ProductInventory> query = ProductInventory.findAll(Sort.by("name"));
```