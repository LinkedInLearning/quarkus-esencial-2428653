# Quarkus esencial
## 2.04 Tu primer test unitario con Quarkus

Una de las métricas de las que disponemos para saber que la calidad del código que vamos desarrollando y asegurar que
no hay regresiones en funcionalidades, es disponer de una buena bateria de tests unitarios.

Quarkus dispone de una integración con JUnit 5 y otros frameworks para la escritura de nuestros tests unitarios.
Además, dispone de una herramienta de ejecución continua de tests unitarios en modo desarrollo que permite obtener el
feedback de errores y regresiones posibles lo antes posible.

Una de las técnicas que disponemos como programadores para desarrollar tests en continuo es además Test Driver Development, o TDD, técnica
especialmente útil cuando trabajamos en pair programming, que utilizaré para enseñaros el framework de tests unitarios
de Quarkus.

Arrancar Quarkus en modo desarrollo de nuevo.
*
* Enseñar cómo solamente se ejecutan los tests que coresponden al código que ha cambiado.

Hemos aprendido a crear tests unitarios y a utilizar el testing en continuo de Quarkus, utilizando la integracion de Quarkus
con los populares frameworks JUnit 5 y RestAssured.