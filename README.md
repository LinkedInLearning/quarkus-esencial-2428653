# Quarkus esencial
## Ficheros de propiedades para la seguridad con Quarkus

* Add extension
```java
./mvnw quarkus:add-extension -Dextensions="quarkus-elytron-security-properties-file" 
```
* Add http basic
```properties
quarkus.http.auth.basic=true
```

* Configure the secured endpoint
```properties
quarkus.http.auth.policy.partner-policy.roles-allowed=partner
quarkus.http.auth.permission.partner.paths=/sales/secured/*
quarkus.http.auth.permission.partner.methods=POST
quarkus.http.auth.permission.partner.policy=partner-policy
```

* Properties (podriamos crear dos ficheros dedicados)
```properties
quarkus.security.users.embedded.enabled=true
quarkus.security.users.embedded.plain-text=true
quarkus.security.users.embedded.users.kineteco_partner=secret
quarkus.security.users.embedded.roles.kineteco_partner=partner
quarkus.security.users.embedded.users.kineteco_customer=secret
quarkus.security.users.embedded.roles.kineteco_customer=customer
```
* Probamos en desarrollo
```shell
http post  localhost:8280/sales/secured
http -a 'kineteco_customer:secret' post  localhost:8280/sales/secured customerId=123 
http -a 'kineteco_partner:secret' post  localhost:8280/sales/secured  
```

* Lo ponemos todo a %dev