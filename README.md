# Quarkus esencial
## 10.3

* Extension
```shell
./mvnw quarkus:add-extension -Dextensions="quarkus-oidc" 
```
* Arrancamos en modo desarrollo. Keycloak disponible

By default, the quarkus, quarkus-app client with a secret password, alice and bob users (with the passwords matching the names), and user and admin roles are created, with alice given both admin and user roles and bob - the user role.
* Vamos a la console dev. alice/alice

* configuramos quarkus.oidc.application-type=web-app
  The quarkus.oidc.application-type property is set to web-app in order to tell Quarkus that you want to enable the OpenID Connect Authorization Code Flow, so that your users are redirected to the OpenID Connect Provider to authenticate.
  