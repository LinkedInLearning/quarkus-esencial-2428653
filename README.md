# Quarkus esencial
## 02.02 Crea tu primera aplicación en Quarkus

La mejor forma de empezar a utilizar un nuevo framework es empezando por un projecto de ejemplo y explorando 
la documentación y web al respecto.
Quarkus.io contiene una serie de guias muy completas de unos 15 minutos para ir poco a poco aprendiendo y profundizando
diferentes aspectos del framework.

Una de las herramientas es la generacion online de un proyecto de ejemplo. 

* Explorar quarkus y el repositorio git 
* Iremos a ver la documentación de Quarkus y a la web de code.quarkus.io
  - *group* com.kineteco
  - *artifact* product-inventory
  - ProductInventoryResource
* Elegiremos las funcionalidades que queramos tener
  - RESTEasy JAX-RS
  - RESTEasy Jackson

Nosotros vamos a colocarnos en directorio vacio de quarkus esencial y vamos a crear un proyecto en linea de comandos de maven.  
* En línea de comandos, con Maven instalado, crearemos el servicio ProductInventoryResource de nuestra compañia.


mvn io.quarkus:quarkus-maven-plugin:2.2.3.Final:create \
-DprojectGroupId=com.kineteco \
-DprojectArtifactId=product-inventory \
-DclassName="com.kineteco.ProductInventoryResource" \
-Dpath="/products"

* Exploramos lo que nos ha creado y lo añadimos como framework maven

Hemos aprendido como partir de cero y tener una aplicación completa, además de los tutoriales, documentación y ejemplos
básicos para empezar con cada una de las extensiones de Quarkus.