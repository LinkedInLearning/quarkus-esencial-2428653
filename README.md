# Quarkus esencial
## 03_05 Test unitarios con diferente aplicativa configuración con Mockito y Quarkus
  
* Inyectar la clase, cambiar el código y ver como funciona
```java
@Inject
ProductInventoryConfig productInventoryConfig;
```  
* Ahora vamos a crear una propiedad que fullCatalog(), que usaremos para diferenciar el catálogo profesional del completo
```java
  boolean retrieveFullCatalog();
```  

```java
if (productInventoryConfig.retrieveFullCatalog() || productInventory.getTargetConsumer().contains(ConsumerType.CORPORATE)) {
                  inventory.put(productInventory.getSku(), productInventory);
               }
```   
* Vamos a usar el mock `ProductInventoryMockProducer`
* Utilizando el mock con Mockito



La configuración soporta también listas y Maps para gestionar las necesidades de configuración de las aplicaciones de forma
eficiente y potente.

Hemos aprendido a configurar nuestra aplicación, cómo utilizar la inyección de dependencias y ConfigMapping para agruparlas,
poner valores por defecto e incluso validarlas