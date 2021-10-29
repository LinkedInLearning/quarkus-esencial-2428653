# Quarkus esencial
## 04_02 Añadir validación de datos en tu API REST con Quarkus

Una buena API viene con un buen control de los datos. Quarkus junto con Hibernate Validator que utilizamos también en la
validación de la configuración nos permite que nuestra API sea robusta.

* Arrancamos quarkus en modo desarrollo
  
* Añadiremos la extensión quarkus-hibernate-validator que nos permite añadir campos que queramos validar.
```shell
./mvnw quarkus:add-extension -Dextensions="quarkus-hibernate-validator"   
```  
* Abrimos la clase ProductInventory y vamos a ir añadiendo una primera anotación para el nombre not blank
```java
@NotBlank(message = "Name is mandatory and should be provided") 
String name;
```
* Anotamos el parametro en el servicio rest con @Valid
```java
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProduct(@Valid ProductInventory productInventory) {
```  
* Probamos el cambio en linea de comandos
```shell
http post localhost:8080/products sku=123 
http localhost:8080/products/123 
```
* Vamos a añadir una validación al stock
```java
@PositiveOrZero
private int quantity;
```
```shell
http post localhost:8080/products sku=456 name=myproduct quantity=-10 
```
* Usar `http post 'http://localhost:8080/products/' 'sku=1234'`

* @Valid en PUT
```shell
http put localhost:8080/products/456 name=myproduct quantity=-10 
```  
* Problemática PUT y POST con el ID. Añadimos @NotNull
```java
 @NotNull
   private String sku;
```
Probamos
```shell
http post localhost:8080/products name=my-product
http put localhost:8080/products/KE180 name=my-product
```
  
* Crear validation groups y probar de nuevo

```java
package com.kineteco.model;

import javax.validation.groups.Default;

public interface ValidationGroups {
   interface Put extends Default {
   }
   interface Post extends Default {
   }
}
```

* Usar validation groups
```java
   @Null(groups = ValidationGroups.Put.class)
   @NotBlank(groups = ValidationGroups.Post.class)
    private String sku;
```

Y en el Resource
```java
@POST
@Consumes(MediaType.APPLICATION_JSON)
public Response createProduct(@Valid @ConvertGroup(to = ValidationGroups.Post.class) ProductInventory productInventory) {

@PUT
@Path("/{sku}")
@Consumes(MediaType.APPLICATION_JSON)
public Response updateProduct(@PathParam("sku") String sku, @Valid @ConvertGroup(to = ValidationGroups.Put.class) ProductInventory productInventory) {

```

```shell
http post localhost:8080/products name=my-product
http put localhost:8080/products/KE180 name=my-product
```

Hibernate validator puede también utilizarse para la validacion de la configuración.