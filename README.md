# Quarkus esencial
## 08_08 Despliegue en Kubernetes de Quarkus y Apache Kafka

* Arrancamos minikube 
  
* Nos aseguramos que tenemos la base de datos Postgresql
  
* Desplegamos un Kafka en minikube

```shell
kubectl create namespace kafka
# creamos el operador
kubectl apply -f 'strimzi-cluster-operator-0.25.0.yaml' -n kafka
#creamos el cluster
kubectl apply -f kafka_cluster.yml -n kafka

#Esperamos kafka 
kubectl wait kafka/kineteco-cluster --for=condition=Ready --timeout=300s -n kafka

# Creamos los topics
kubectl apply -f kafka_topics.yml -n kafka

```

* Configuramos el acceso a base de datos reactiva en PI
```properties
%prod.quarkus.datasource.reactive.url=postgresql://postgres.default:5432/kineteco
```
  
* Configuramos la conexión en los properties de Product Inventory y Order Service
```properties
%prod.kafka.bootstrap.servers=kineteco-cluster-kafka-bootstrap.kafka:9092
```

* Desplegamos Product Inventory y Order Service
```shell
 ./mvnw package -Dquarkus.kubernetes.deploy=true -DskipTests=true
```

* Modificamos `runStockUpgrade`

* Monitor kafka
```shell
kubectl -n kafka run kafka-consumer -it \
  --image=quay.io/strimzi/kafka:0.25.0-kafka-2.8.0 \
  --rm=true --restart=Never \
  -- bin/kafka-console-consumer.sh \
  --bootstrap-server kineteco-cluster-kafka-bootstrap.kafka:9092 \
  --topic orders \
  --from-beginning
```