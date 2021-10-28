# Quarkus esencial
## 03_07 Configuración de logs en Quarkus

Las trazas de logs aplicativos son fundamentales para poder analizar el estado de nuestros servicios así como para 
diagnosticar errores. Es por ello que una buena estrategia de trazas, o logs, debe de tenerse en cuenta desde 
el principio. 
Quarkus soporta diferentes librerías: JDK de java, JBoss logging, SLF4J y Apache Logging, 
por defecto siendo JBoss Logging.

* Cambiar globalmente
```properties
quarkus.log.level=DEBUG
```
* Añadir un fichero
```properties
quarkus.log.file.enable=true
```

* Modificar para ver trazas de debug solamente nuestras
```properties
quarkus.log.category."com.kineteco".level=DEBUG
```

Para ir más lejos, utilizar un sistema de Logs Centralizado como GreyLog, Logstash o Fluentd
en la guía `https://quarkus.io/guides/centralized-log-management`


