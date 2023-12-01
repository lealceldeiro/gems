# CHAPTER 1: Meet Kafka

Data within Kafka is stored durably, in order, and can be read deterministically.

In addition, the data can be distributed within the system to provide additional protections against failures, as well
as significant opportunities for scaling performance.

The unit of data within Kafka is the _message_.

A message can have an optional piece of metadata, which is referred to as a _key_.

The message and the key are byte arrays and have no specific meaning to Kafka.

The _offset_, an integer value that continually increases, is another piece of metadata that Kafka adds to each message
as it is produced.

A single Kafka server is called a _broker_.

Kafka brokers are designed to operate as part of a _cluster_.

Within a cluster of brokers, one broker will also function as the cluster _controller_.

A partition is owned by a single broker in the cluster, and that broker is called the _leader_ of the partition.

A replicated partition is assigned to additional brokers, called _followers_ of the partition.

### Some pros

- Support for multiple producers
- Support for multiple consumers
- Disk-based retention
- Scalable
- High performance

### Use Cases

- Activity tracking
- Messaging
- Metrics and logging
- Commit log
- Stream processing
