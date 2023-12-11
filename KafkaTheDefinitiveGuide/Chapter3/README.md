# CHAPTER 3: Kafka Producers: Writing Messages to Kafka

## Source code

The source code of a sample Java Kafka Producer, based on the content from this chapter, can be found
[here](https://github.com/lealceldeiro/kafkaproducer).

## Producer Overview

Messages to Kafka are produced by creating a `ProducerRecord`, which includes

- (**mandatory**) the topic we want to send the record to
- (**mandatory**) and a value
- (_optional_) a key
- (_optional_) a partition
- (_optional_) a timestamp
- (_optional_) a collection of headers

When the `ProducerRecord` is sent, the first thing the producer will do is serialize the key and value objects to
byte arrays, so they can be sent over the network.

If a partition isn't explicitly specified, the data is sent to a partitioner (which chooses a partition for the
message, usually, based on the key).

Then, the record is added to a batch of records that will also be sent to the same topic and partition. A separate
thread is responsible for sending those batches of records to the appropriate Kafka brokers.

## Constructing a Kafka Producer

Mandatory properties of a Kafka producer:

- `bootstrap.servers`: List of host:port pairs of brokers that the producer will use to establish initial connection to
  the Kafka cluster.
- `key.serializer`: Name of a class that will be used to serialize the keys of the records that will be produced to
  Kafka.
- `value.serializer`: Name of a class that will be used to serialize the values of the records that will be produced to
- Kafka.

All the configuration options are available in
the [official docs](https://kafka.apache.org/documentation.html#producerconfigs).

Primary methods of sending messages:

- Fire-and-forget
- Synchronous send
- Asynchronous send

## Configuring Producers

Some of the parameters that have a significant impact on memory use, performance, and reliability of the producers:

- `client.id`: logical identifier for the client and the application it is used in
- `acks`: controls how many partition replicas must receive the record before the producer can consider the write
  successful (valid values: `0`, `1`, `all`)
- `max.block.ms`: controls how long the producer may block when calling `send()` and when explicitly requesting metadata
  via `partitionsFor()`
- `delivery.timeout.ms`: limits the amount of time spent from the point a record is ready for sending (`send()` returned
  successfully and the record is placed in a batch) until either the broker responds or the client gives up, including
  time spent on retries
- `request.timeout.ms`: controls how long the producer will wait for a reply from the server when sending data
- `retries`: control how many times the producer will retry sending the message before giving up and notifying the
  client of an issue. `retries=0` disables retrying
- `retry.backoff.ms`: controls the time to wait when retrying (time-lapse between one and the subsequents retry) to send
  a message
- `linger.ms`: controls the amount of time to wait for additional messages before sending the current batch
- `buffer.memory`: sets the amount of memory the producer will use to buffer messages waiting to be sent to brokers
- `compression.type`: sets the compression algorithms to be used to compress the data before sending it to the brokers.
  valid values are `snappy`, `gzip`, `lz4`, and `zstd`
- `batch.size`: controls the amount of memory **in bytes** used for each batch when batching multiple messages together
  by the producer before sending them to the broker

## Serializers

### Apache Avro

Apache Avro is a language-neutral data serialization format.

Generating Avro classes can be done either using the `avro-tools.jar` or the Avro Maven plugin, both part of Apache
Avro ([see docs](https://avro.apache.org/docs/current/getting-started-java/)).

## Partitions

When partitioning keys is important, the easiest solution is to create topics with sufficient partitions, see
[How to Choose the Number of Topics/Partitions in a Kafka Cluster?](https://www.confluent.io/blog/how-choose-number-topics-partitions-kafka-cluster/)

## Headers

Records can also include headers. Record headers give us the ability to add some metadata about the Kafka record,
without adding any extra information to the key/value pair of the record itself.

## Interceptors

Interceptors can help us to modify the behavior of our Kafka client application without modifying its code.

Common use cases for producer interceptors include capturing monitoring and tracing information; enhancing the message
with standard headers, especially for lineage tracking purposes; and redacting sensitive information.

## Quotas and Throttling

Kafka brokers have the ability to limit the rate at which messages are produced and consumed. This is done via the
quota mechanism. Kafka has three quota types: produce, consume, and request.

Produce and consume quotas limit the rate at which clients can send and receive data, measured in bytes per second.
Request quotas limit the percentage of time the broker spends processing client requests.

When a client reaches its quota, the broker will start throttling the client’s requests to prevent it from exceeding
the quota. This means that the broker will delay responses to client requests; in most clients this will automatically
reduce the request rate (since the number of in-flight requests is limited) and bring the client traffic down to a level
allowed by the quota. To protect the broker from misbehaved clients sending additional requests while being throttled,
the broker will also mute the communication channel with the client for the period of time needed to achieve compliance
with the quota.
