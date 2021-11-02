# Quarkus esencial
## 07_06 Tolerancia a fallos con Quarkus: Bulkhead

* Ponemos la anotación `@Bulkhead(1)` para permitir una sola llamada cada vez
* Cambiamos SalesServiceFallback
```java
 case "BulkheadException":
            response = Response.status(Response.Status.TOO_MANY_REQUESTS).build();
            break;
```  
* 

Llamamos al endpoint sales con un producto que es DELUXE
```shell
./run-deluxe.sh
```

Llamamos al endpoint sales con un producto que es ECONOMY

```shell
./run-economy.sh
```

