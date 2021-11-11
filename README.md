# Quarkus esencial
## 04_05 Consideraciones adicionales para un API REST

* Scope of REST API. Singleton por defecto
```java
@ApplicationScoped
```

* URI creation. De momento usamos @Context para poder inyectar diferentes contextos utiles.
En el momento de la grabacion usamos context, pero CDI está planteando un cambio para usar otra
  en el futuro próximo.
```java
@POST
@Consumes(MediaType.APPLICATION_JSON)
public Response createProduct(@Context UriInfo uriInfo, @Valid @ConvertGroup(to = ValidationGroups.Post.class) ProductInventory productInventory) {
  LOGGER.debugf("create %s", productInventory);
  productInventoryService.addProductInventory(productInventory);
  UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(productInventory.getSku());
  return Response.created(builder.build()).build();
}
```
  
* Open API, no olvidar de documentar correctamente @APIResponse
```java
@APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ProductInventory.class, type = SchemaType.ARRAY)))
@Operation(summary = "Updates an product inventory")
@APIResponse(responseCode = "200", description = "The updated product", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ProductInventory.class)))
@APIResponse(responseCode = "404", description = "No product")
@PUT
@Path("/{sku}")
@Consumes(APPLICATION_JSON)
public Response updateProduct(@PathParam("sku") String sku, @ConvertGroup(to = ValidationGroups.Put.class)  @Valid ProductInventory productInventory) {
  LOGGER.debugf("update %s", productInventory);
  ProductInventory updated = productInventoryService.updateProductInventory(sku, productInventory);
  if (updated == null) {
  return Response.status(Response.Status.NOT_FOUND).build();
  }
  return Response.ok(productInventory).build();
}
```

* index.xml

* En recursos podemos utilizar el index.xml, pero en general tendremos una applicacion front
  que esté en otro puerto, por lo que podemos tener problemas de CORS en javascript. 
  https://quarkus.io/guides/http-reference#cors-filter
```properties
%dev.quarkus.http.cors=true
```
