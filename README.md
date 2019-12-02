# outlier-detection-system
Outlier Detection System

***
- [x] Consumer
- [x] Web (squized in consumer module as of now)
- [ ] Separate web module (TODO)
- [ ] Tests (none, TODO)

## Requirements

* Java 1.8+
* Maven
* Docker for Kafka, Zookeeper, PostgreSQL

### Kafka

```console
docker run --net=host -d --name=zookeeper -e ZOOKEEPER_CLIENT_PORT=2181 confluentinc/cp-zookeeper
```

```console
docker run --net=host -d -p 9092:9092 --name=kafka -e KAFKA_ZOOKEEPER_CONNECT=localhost:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 confluentinc/cp-kafka
```

### Database
For the moment in memory H2, web and consumer sharing the same JVM.

## Build (Maven)

```console
cd outlier-detection-system
mvn clean package
```

## Run

```console
java -jar readings-consumer\target\readings-consumer-0.0.1-SNAPSHOT.jar 
```

## Sample calls:

* Publish some readings
```console
curl -XPOST -H "Content-Type: application/json" -d '{"publisher":"pub1", "readings":[29]}' localhost:8080/readings
```

* Get outliers marked with true for specified publisher (pub1)
```console
curl -XGET localhost:8080/publishers/pub1/outliers?limit=10
```
