# Quarkus esencial
## 03_01 Inyección de dependencias en Quarkus

Introducción en power point.

* Quarkus está arrancado en modo desarrollo
* Creamos una nueva clase `ProductInventoryService`
* Extraemos el código de ProductInventoryResource y lo ponemos en ProductInventoryService y lo anotamos con `@ApplicationScoped`
```java
    @ApplicationScoped
    public class ProductInventoryService {
    
    public ProductInventory getBySku(String sku) {
      ProductInventory productInventory = new ProductInventory();
      productInventory.setSku(sku);
      productInventory.setName("K-Eco 180");
      productInventory.setProductAvailability(ProductAvailability.IN_STOCK);
      productInventory.setQuantity(12);
      productInventory.setPrice(BigDecimal.valueOf(315.0));
      return productInventory;
    }
}
```

* Añadimos un test unitario
```java
@QuarkusTest
public class ProductInventoryServiceTest {

   @Inject
   ProductInventoryService productInventoryService;

   @Test
   public void testGetBySku() {
      ProductInventory productInventory = productInventoryService.getBySku("KE180");
      assertThat(productInventory).isNotNull();
      assertThat(productInventory.getSku()).isEqualTo("KE180");
   }
}
```

* Inyectamos el nuevo servicio utilizando en privado `@Inject`
  2021-09-24 11:56:22,459 INFO  [io.qua.arc.pro.BeanProcessor] (build-36) Found unrecommended usage of private members (use package-private instead) in application beans:
  - @Inject field com.kineteco.ProductInventoryResource#productInventoryService
  - Quarkus recomienda el uso de privado en paquete de los atributos inyectados porque eso permite que Quarkus pueda inyectar el bean sin utilizar
  reflexion. Es recomendable evitar la reflection lo máximo posible para tener mejor rendimiento.

Hemos aprendido más cómo utilizar la inyección de dependencias con CDI y cómo funciona en Quarkus. 
Utilizaremos la inyección de dependencias y las anotaciones continuamente con las diferentes extensiones de Quarkus.

