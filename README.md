# Quarkus esencial
## 08_08 Despliegue en Kubernetes de Quarkus y Apache Kafka

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

* Configuramos la conexion en los properties de PI y OS
```properties
%prod.kafka.bootstrap.servers=kineteco-cluster-kafka-bootstrap.kafka:9092
```

* Desplegamos PI y OS
```shell
 ./mvnw package -Dquarkus.kubernetes.deploy=true -DskipTests=true
```

* Modificamos `runStockUpgrade`

* check kafka
```shell
kubectl -n kafka run kafka-consumer -it \
  --image=quay.io/strimzi/kafka:0.25.0-kafka-2.8.0 \
  --rm=true --restart=Never \
  -- bin/kafka-console-consumer.sh \
  --bootstrap-server kineteco-cluster-kafka-bootstrap.kafka:9092 \
  --topic orders \
  --from-beginning
```