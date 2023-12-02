# CHAPTER 2: Installing Kafka

### My own experience running Kafka locally with Docker

For this to work, [Docker](https://www.docker.com/) must be installed.

The book starts by mentioning the installation of Kafka and ZooKeeper, but after
[KRaft was marked as Production Ready](https://cwiki.apache.org/confluence/display/KAFKA/KIP-833%3A+Mark+KRaft+as+Production+Ready),
only the Kafka image is actually needed when the version 2.8 or later of Apache Kafka is used.

The following example uses [bitnami/kafka](https://hub.docker.com/r/bitnami/kafka) and is based in that official
documentation examples.

- **Create a Docker network** (see [networking](https://docs.docker.com/network/)). This is to get the Apache Kafka
  server
  running inside the container accessed by another Apache Kafka client, running inside another container.

> This is because containers attached to the same network can communicate with each other using the container name as
> the hostname.

```shell
docker network create app-tier --driver bridge
```

- **Launch the Apache Kafka server** instance from a docker container

> Use the `--network app-tier` argument to run the docker command to attach the Apache Kafka container to the `app-tier`
> network.

```shell
docker run -d --name kafka-server --hostname kafka-server --network app-tier \
    -e KAFKA_CFG_NODE_ID=0 \
    -e KAFKA_CFG_PROCESS_ROLES=controller,broker \
    -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 \
    -e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT \
    -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka-server:9093 \
    -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER \
    bitnami/kafka:latest
```

- **Launch an Apache Kafka client** instance from another container and list topics

> This new container instance running the client will connect to the server created in the previous step

```shell
docker run -it --rm --network app-tier bitnami/kafka:latest kafka-topics.sh --bootstrap-server kafka-server:9092 --list
```

> At this point there should not be any topics displayed

- **Create a topic**

```shell
docker run -it --rm --network app-tier bitnami/kafka:latest kafka-topics.sh --bootstrap-server kafka-server:9092 \
    --create --replication-factor 1 --partitions 1 --topic topic1
```

- **Verify the previously created topic**

```shell
docker run -it --rm --network app-tier bitnami/kafka:latest kafka-topics.sh --bootstrap-server kafka-server:9092 \
    --describe --topic topic1
```

- **Produce messages** to the previously created topic

```shell
docker run -it --rm --network app-tier \
    bitnami/kafka:latest kafka-console-producer.sh --bootstrap-server kafka-server:9092 --topic topic1
```

> When the console waits for input (symbol `>` visible) enter some message and hit enter (once for every message).
> To finish producing messages do `Ctrl` + `C` (`^C`)

- **Consume messages** from the previously created topic

```shell
docker run -it --rm --network app-tier \
    bitnami/kafka:latest kafka-console-consumer.sh --bootstrap-server kafka-server:9092 \
    --topic topic1 --from-beginning
```

> When the console waits for input (symbol `>` visible) enter some message and hit enter (once for every message).
> To finish producing messages do `Ctrl` + `C` (`^C`)

<details>
<summary>Docker run info</summary>
See https://docs.docker.com/engine/reference/commandline/run/ for more details.

-d: run container in background and print container ID

-it: instructs Docker to allocate a pseudo-TTY connected to the container's stdin; creating an interactive bash shell in
the container.

--rm: automatically remove the container when it exits
</details>

### General Broker Parameters

- broker.id: it must be unique for each broker within a single Kafka cluster
- listeners: defined as `<protocol>://<hostname>:<port>`, i.e.: `PLAINTEXT://localhost:9092,SSL://:9091`
- zookeeper.connect: location of the ZooKeeper used for storing the broker metadata in the format `hostname:port/path`
- log.dirs: comma-separated list of paths on disk where all the message log segments are persisted
- num.recovery.threads.per.data.dir: number of recovery threads per log directory
- auto.create.topics.enable: whether to create (or not) automatically topics under certain conditions
- auto.leader.rebalance.enable: this config can be specified to ensure leadership is balanced as much as possible
- delete.topic.enable: this config can be set to prevent arbitrary deletions of topics (false: disabling topic deletion)
