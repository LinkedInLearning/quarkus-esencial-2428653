# Quarkus esencial
## 03_01 Inyección de dependencias en Quarkus

Introducción en power point.

* Quarkus está arrancado en modo desarrollo
* Creamos una nueva clase ProductInventoryService
* Extraemos el código de ProductInventoryResource y lo ponemos en ProductInventoryService y lo anotamos con @ApplicationScoped
* Inyectamos el nuevo servicio utilizando @Inject
  2021-09-24 11:56:22,459 INFO  [io.qua.arc.pro.BeanProcessor] (build-36) Found unrecommended usage of private members (use package-private instead) in application beans:
  - @Inject field com.kineteco.ProductInventoryResource#productInventoryService
  - Quarkus recomienda el uso de privado en paquete de los atributos inyectados porque eso permite que Quarkus pueda inyectar el bean sin utilizar
    reflexion. Es recomendable evitar la reflection lo máximo posible para tener mejor rendimiento.
* Comprobamos que nuestros tests siguen funcionando

Hemos aprendido más cómo utilizar la inyección de dependencias con CDI y cómo funciona en Quarkus.
Utilizaremos la inyección de dependencias y las anotaciones continuamente con las diferentes extensiones de Quarkus.

