# Quarkus esencial
## 03_04 Declarar, utilizar y validar la configuración aplicativa con Quarkus

Antes o después todas las aplicaciones necesitan ser configuradas para responder a los requisitos o adaptase en función 
de esa misma configuración. Vamos a aprender cómo declarar configuración que será utilizada por nuestro
servicio para cargar únicamente los productos correspondientes a un tipo de catálogo: DOMESTIC, PERSONAL o CORPORATE

* Declaramos com.kineteco.greeting-message
* Lo inyectamos usando @ConfigProperty(name = "com.kineteco.greeting-message")
* Explicamos el problema, que ademas se puede @Inject @Config pero los inconvenientes de organizarlo asi 
* Ventajas de utilizar un @ConfigMapping
* Crear la interfaz ProductInventoryConfig en el paquete config
* Crear un método llamado greetingMessage()
* Inyectar la clase, cambiar el código y ver como funciona
* Ahora vamos a crear una propiedad que fullCatalog(), que usaremos para diferenciar el catálogo profesional del completo
* Una vez vemos que esa propiedad utilizada no funciona, explicamos @WithDefault("true")
* Añadimos un test que va a verificar que los productos que no son profesionales no están
* Vamos a crear el mock ProductInventoryMockProducer
* Utilizando el mock 
* Añadimos una propiedad que no vamos a usar pero sirve para ilustrar la validacion utilizando @Min(5) llamada minUnits, la configuramos a 1
* Arrancamos y vemos que todo va bien. Añadimos `quarkus-hibernate-validator` para activar la validación.
* Vemos que la aplicacion muestra un error si el valor es inferior a 5

La configuración soporta también listas y Maps para gestionar las necesidades de configuración de las aplicaciones de forma
eficiente y potente.

Hemos aprendido a configurar nuestra aplicación, cómo utilizar la inyección de dependencias y ConfigMapping para agruparlas,
poner valores por defecto e incluso validarlas