# CHAPTER 7: Reliable Data Delivery

Reliability is not just a matter of specific Kafka features. An entire reliable system needs to be built, including the
application architecture, the way applications use the producer and consumer APIs, producer and consumer configuration,
topic configuration, and broker configuration. Making the system more reliable always has trade-offs in application
complexity, performance, availability, or disk-space usage. By understanding all the options and common patterns and
understanding requirements for each use case, more informed decisions can be made regarding how reliable the
application and Kafka deployment need to be and which trade-offs make sense.

Guarantees provided by Kafka:

- Kafka provides order guarantee of messages in a partition. If message B was written after message A, using the same
  producer in the same partition, then Kafka guarantees that the offset of message B will be higher than message A, and
  that consumers will read message B after message A.
- Produced messages are considered “committed” when they were written to the partition on all its in-sync replicas (but
  not necessarily flushed to disk). Producers can choose to receive acknowledgments of sent messages when the message
  was fully committed, when it was written to the leader, or when it was sent over the network.
- Messages that are committed will not be lost as long as at least one replica remains alive.
- Consumers can only read messages that are committed.

These guarantees can then be "tweaked" by changing Kafka's configuration, depending on the business needs and
trade-offs made taking into account other important considerations, such as availability, high throughput, low latency,
and hardware costs.

If we allow out-of-sync replicas to become leaders, we risk data loss and inconsistencies. If we don’t allow them to
become leaders, we face lower availability as we must wait for the original leader to become available before the
partition is back online.

There are two important things that everyone who writes applications that produce to Kafka must pay attention to:

- Use the correct `acks` configuration to match reliability requirements
- Handle errors correctly both in configuration and in code

Some failure conditions under which integration tests can be run:

- Clients lose connectivity to one of the brokers
- High latency between client and broker
- Disk full
- Hanging disk (also called “brown out”)
- Leader election
- Rolling restart of brokers
- Rolling restart of consumers
- Rolling restart of producers

[Trogdor](https://github.com/apache/kafka/blob/trunk/trogdor/README.md), the test framework for Apache Kafka could be
used to simulate systems faults.

[Burrow, the Kafka Consumer Lag Checking](https://github.com/linkedin/Burrow) could be useful in helping monitoring of
consumer lags.
