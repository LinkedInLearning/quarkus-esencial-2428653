# Quarkus esencial
## 03_02 Ciclo de vida de las aplicaciones en Quarkus

Introducción en power point.

* Quarkus está arrancado en modo desarrollo
* Modificamos la clase ProductInventoryService para añadirle los eventos 
  
```java
void onStart(@Observes StartupEvent ev) {
      System.out.println("==========> onStart");
      try {
         loadData();
      } catch (Exception e) {
         System.out.println(e);
      }
   }

```

```java
 void onStop(@Observes ShutdownEvent ev) {
      System.out.println("===========> onStop");
   }

```

* Eliminamos el banner de Quarkus

```properties
quarkus.banner.enabled=true
```

Hemos aprendido más cómo funciona el ciclo de vida de las aplicaciones en Quarkus y cuales son los eventos que se ejecutan
en ese mismo ciclo. Estos eventos son útiles para tareas de configuración del estato inicial.
Utilizaremos la inyección de dependencias y las anotaciones continuamente con las diferentes extensiones de Quarkus.

