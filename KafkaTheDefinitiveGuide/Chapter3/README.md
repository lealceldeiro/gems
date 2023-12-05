# CHAPTER 3: Kafka Producers: Writing Messages to Kafka

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
