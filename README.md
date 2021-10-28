# Quarkus esencial
## 04_01 Crear un API CRUD con Quarkus y la extension RESTEasy de Quarkus

La creación de APIs en REST es hoy en día fundamental en la escritura de Microservicios. Nos vamos a focalizar en 
utilizar los verbos HTTP para crear un API en Quarkus que nos permita gestionar
el inventario de productos.

* Arrancamos Quarkus en modo desarrollo
  
* Vamos a iir corrigiendo los TESTS.

* Inventario
- Añadimos Path Param y gestionamos 404
```java
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sku}")
    public Response inventory(@PathParam("sku") String sku) {
        LOGGER.debugf("get by sku %s", sku);
        ProductInventory productInventory = productInventoryService.getBySku(sku);

        if (productInventory == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(productInventory).build();
    }
```

* Lista de productos
```java
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<ProductInventory> listInventory() {
        LOGGER.debug("Product inventory list");
        return productInventoryService.listInventory();
    }
```  
* Create Product
```java
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProduct(ProductInventory productInventory) {
        LOGGER.debugf("create %s", productInventory);
        productInventoryService.addProductInventory(productInventory);
        return Response.created(URI.create(productInventory.getSku())).build();
    }
```  

* Update
```java
@PUT
    @Path("/{sku}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProduct(ProductInventory productInventory) {
        LOGGER.debugf("update %s", productInventory);
        productInventoryService.updateProductInventory(productInventory);
        return Response.accepted(URI.create(productInventory.getSku())).build();
    }
```  

* Delete
```java
  @DELETE
    @Path("/{sku}")
    public Response delete(@PathParam("sku") String sku) {
        LOGGER.debugf("delete by sku %s", sku);
        productInventoryService.delete(sku);
        return Response.accepted().build();
    }
```  

* Patch para update stock
```java
 @PATCH
    @Path("/{sku}")
    public Response updateStock(@PathParam("sku") String sku, @QueryParam("stock") Integer stock) {
        LOGGER.debugf("get by sku %s", sku);
        ProductInventory productInventory = productInventoryService.stockUpdate(sku, stock);
        if (productInventory == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(productInventory).build();
    }
```  
  
Hemos aprendido como crear una API REST para implementar un API sencilla de gestion del inventario de Productos
utilizando los verbos HTTP POST, PUT, DELETE, GET y PATCH.
