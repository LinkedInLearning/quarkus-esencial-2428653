# Quarkus esencial
## 04_02 Añadir validación de datos en tu API REST con Quarkus

Una buena API viene con un buen control de los datos. Quarkus junto con Hibernate Validator que utilizamos también en la
validación de la configuración nos permite que nuestra API sea robusta.

* Arrancamos quarkus en modo desarrollo 
* Añadiremos la extensión quarkus-hibernate-validator que nos permite añadir campos que queramos validar.
* Abrimos la clase ProductInventory y vamos a ir añadiendo anotaciones 
* Añadimos @NotBlank(message = "Name is mandatory and should be provided") private String name;
* Comprobamos que los tests no fallan.
* Utilizamos la injeccion con @Inject Validator  y validamos 
* Cambiamos para usar @Valid en el endpoint
* Usar `http post 'http://localhost:8080/products/' 'sku=1234'` 
* `quarkus.hibernate-validator.fail-fast=false` si queremos que termine la validación al primer error
* Problematica PUT y POST con el ID. el ID no deberia venir en el cuerpo sino en el path, contrariamente a POST. 
* Crear validation groups y cambiar los endpoints

Hemos aprendido cómo utilizar hibernate validator para validar la entrada de datos de nuestra API REST, fundamental
para fallar rápido en caso de entrada de datos erroneo. Hemos aprendido cómo la validación es sencilla y viene con
una gran cantidad de opciones de validación para evitar que hagamos la validación por nosotros mismos en la mayoría
de los casos.