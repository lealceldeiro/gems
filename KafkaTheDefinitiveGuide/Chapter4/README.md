# CHAPTER 4: Kafka Consumers: Reading Data from Kafka

The source code of a sample Java Kafka Consumer, based on the content from this chapter, can be found
[here](https://github.com/lealceldeiro/kafkaconsumer).

Applications that need to read data from Kafka use a KafkaConsumer to subscribe to Kafka topics and receive messages
from these topics.

### Consumers and Consumer Groups

A consumer group must be created for each application that needs all the messages from one or more topics.

Consumers are added to an existing consumer group to scale the reading and processing of messages from the topics, so
each additional consumer in a group will only get a subset of the messages.

### Consumer Groups and Partition Rebalance

Moving partition ownership from one consumer to another is called a _rebalance_.

Types of rebalances:

- Eager rebalances: all consumers stop consuming, give up their ownership of all partitions, rejoin the consumer group,
  and get a brand-new partition assignment.
- Cooperative rebalances: typically involve reassigning only a small subset of the partitions from one consumer to
  another, and allowing consumers to continue processing records from all the partitions that are not reassigned.

Consumers maintain membership in a consumer group and ownership of the partitions assigned to them by sending
_heartbeats_ to a Kafka broker designated as the _group coordinator_.

he first consumer to join the group becomes the group _leader_.

By default, the identity of a consumer as a member of its consumer group is transient, unless it's configured with a
unique `group.instance.id`, which makes the consumer a _static_ member of the group.

If two consumers join the same group with the same group.instance.id, the second consumer will get an error saying that
a consumer with this ID already exists.

To consume records from a Kafka broker: create a `KafkaConsumer` (create a Java `Properties` instance with the
properties to be passed to the consumer; three mandatory properties: `bootstrap.servers`, `key.deserializer`, and
`value.deserializer`).

- `bootstrap.servers`: connection string to a Kafka cluster
- `key.deserializer` and `value.deserializer`: specify classes that turn Java objects to byte arrays

Another common property is `group.id`, and it specifies the consumer group the `KafkaConsumer` instance belongs to.

### Creating a Kafka Consumer

#### The Poll Loop

At the heart of the Consumer API is a simple loop for polling the server for more data.

##### Thread Safety

Multiple consumers that belong to the same group cannot coexist in the same thread, and there cannot be multiple threads
safely use the same consumer. One consumer per thread is the rule.

To run multiple consumers in the same group in one application, each of them needs to run in its own thread. It is
useful to wrap the consumer logic in its own object and then use Java's `ExecutorService` to start multiple threads,
each with its own consumer. See an example in this
[Tutorial](https://www.confluent.io/blog/tutorial-getting-started-with-the-new-apache-kafka-0-9-consumer-client/).

### Configuring Consumers

All the consumer configuration is documented in the
[Apache Kafka documentation](https://kafka.apache.org/documentation.html#newconsumerconfigs).

Assignment strategies (used to configure `partition.assignment.strategy` with one of
the `org.apache.kafka.clients.consumer.*Assignor` values):

- Range
- RoundRobin
- Sticky
- Cooperative Sticky

### Standalone Consumer: Why and How to Use a Consumer Without a Group

When it's known exactly which partitions the consumer should read, the consumer can be not subscribed to a topic,
instead, it is assigned a few partitions. A consumer can either subscribe to topics (and be part of a consumer group)
or assign itself partitions, but not both at the same time.

Example:

```java
Duration timeout = Duration.ofMillis(100);
List<PartitionInfo> partitionInfos = null;
partitionInfos = consumer.partitionsFor("topic");

if (partitionInfos != null) {
    for (PartitionInfo partition : partitionInfos)
        partitions.add(new TopicPartition(partition.topic(), partition.partition()));
        consumer.assign(partitions);
        
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(timeout);
            for (ConsumerRecord<String, String> record: records) {
                // do some work with record
            }
            consumer.commitSync();
        }
}
```
