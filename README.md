# Quarkus esencial
## 2.02 Crea tu primera aplicación en Quarkus
* Iremos a ver la documentación de Quarkus y a la web de code.quarkus.io 
  - *group* com.kineteco
  - *artifact* product-inventory
  - ProductInventoryResource
* Elegiremos las funcionalidades que queramos tener
  - RESTEasy JAX-RS
  - RESTEasy Jackson
* En línea de comandos, con Maven instalado, crearemos el servicio ProductInventoryResource de nuestra compañia.


mvn io.quarkus:quarkus-maven-plugin:2.2.3.Final:create \
-DprojectGroupId=com.kineteco \
-DprojectArtifactId=product-inventory \
-DclassName="com.kineteco.ProductInventoryResource" \
-Dpath="/products"
