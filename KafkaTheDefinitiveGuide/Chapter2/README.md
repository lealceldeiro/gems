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

### Selecting Hardware

#### Factors that can contribute to overall performance bottlenecks

##### Disk throughput and capacity

Faster disk writes will equal lower produce latency.

Generally, observations show that HDD drives are typically more useful for clusters with very high storage needs but
aren't accessed as often, while SSDs are better options if there is a very large number of client connections.

The total traffic for a cluster can be balanced across the cluster by having multiple partitions per topic, which will
allow additional brokers to augment the available capacity if the density on a single broker will not suffice.

##### Memory

The normal mode of operation for a Kafka consumer is reading from the end of the partitions, where the consumer is
caught up and lagging behind the producers very little, if at all. In this situation, the messages the consumer is
reading are optimally stored in the system’s page cache, resulting in faster reads than if the broker has to reread the
messages from disk. Therefore, having more memory available to the system for page cache will improve the performance
of consumer clients.

Kafka itself does not need much heap memory configured for the JVM. Even a broker that is handling 150000 messages per
second and a data rate of 200 megabits per second can run with a 5GB heap.

It is not recommended to have Kafka running on a system with any other significant application, as it will have to
share the use of the page cache. This will decrease the consumer performance for Kafka.

##### Networking

The available network throughput will specify the maximum amount of traffic that Kafka can handle. This can be a
governing factor, combined with disk storage, for cluster sizing. To prevent the network from being a major governing
factor, it is recommended to run with at least 10 Gb NICs (Network Interface Cards). Older machines with 1 Gb NICs are
easily saturated and aren't recommended.

##### CPU

Processing power is not as important as disk and memory until you begin to scale Kafka very large, but it will affect
overall performance of the broker to some extent.

### Configuring Kafka Clusters

Typically, the size of a cluster will be bound on the following key areas:

- Disk capacity
- Replica capacity per broker
- CPU capacity
- Network capacity

Currently (2023), in a well-configured environment, it is recommended to not have more than 14000 partition replicas per
broker and 1 million replicas per cluster.

Requirements in the broker configuration to allow multiple Kafka brokers to join a single cluster:

1. All brokers must have the same configuration for the `zookeeper.connect` parameter
2. All brokers in the cluster must have a unique value for the `broker.id` parameter

The current number of dirty pages can be determined by checking `cat /proc/vmstat | egrep "dirty|writeback"`.

The most common choices for local filesystems are either Ext4 (fourth extended filesystem) or Extents File System (XFS),
being XFS the preferred option because it outperforms Ext4 for most workloads with minimal tuning required.

### Production Concerns

#### JVM Garbage Collector Options

As of this writing, the Garbage-First garbage collector (G1GC) is the recommended one, but it's worth checking the
official [Kafka documentation](https://kafka.apache.org/documentation.html#java) related to this topic to get the
latest updates; and a best practice is to use if (G1GC) for anything for Java 1.8 and later.

Configuration options for G1GC used to adjust its performance:

- `MaxGCPauseMillis`: preferred pause time for each garbage-collection cycle
- `InitiatingHeapOccupancyPercent`: specifies the percentage of the total heap that may be in use before G1GC will start
  a collection cycle

#### Datacenter Layout

A datacenter environment that has a concept of fault zones is preferable.

It is recommended to use tools that keep your cluster balanced properly to maintain rack awareness, such as
[Cruise Control](https://github.com/linkedin/cruise-control).

Overall, the best practice is to have each Kafka broker in a cluster installed in a different rack, or at the very
least not share single points of failure for infrastructure services such as power and network.
