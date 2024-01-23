# CHAPTER 12: Administering Kafka

While Apache Kafka implements authentication and authorization to control topic operations, default configurations do
not restrict the use of these tools. This means that these CLI tools can be used without any authentication required,
which will allow operations such as topic changes to be executed with no security check or audit.

## Topic Operations

The `kafka-topics.sh` tool provides easy access to most topic operations. It allows you to create, modify, delete, and
list information about topics in the cluster. While some topic configurations are possible through this command, they
have been deprecated, and it is recommended to use the more robust method of using the `kafka-config.sh` tool for
configuration changes.

## Consumer Groups

The `kafka-consumer-groups.sh` tool helps manage and gain insight into the consumer groups that are consuming from
topics in the cluster.

## Dynamic Configuration Changes

The `kafka-configs.sh` is the main tool for modifying all the configurations for topics, clients, brokers, and more that
can be updated dynamically during runtime without having to shut down or redeploy a cluster.

## Producing and Consuming

To manually produce or consume some sample messages these tools are provided: `kafka-console-consumer.sh` and
`kafka-console-producer.sh`.

### Example

Consuming the earliest message from the `__consumer_offsets` topic:

```shell
kafka-console-consumer.sh --bootstrap-server localhost:9092
                          --topic __consumer_offsets --from-beginning --max-messages 1
                          --formatter "kafka.coordinator.group.GroupMetadataManager\$OffsetsMessageFormatter"
                          --consumer-property exclude.internal.topics=false
```

This will show something like this:

```text
[my-group-name,my-topic,0]::[OffsetMetadata[1,NO_METADATA]
CommitTime 1623034799990 ExpirationTime 1623639599990]
Processed a total of 1 messages
```

## Partition Management

A default Kafka installation contains a few scripts for working with the management of partitions. One of these tools
allows for the reelection of leader replicas; another is a low-level utility for assigning partitions to brokers.
Together these tools can assist in situations where a more manual hands-on approach to balance message traffic within a
cluster of Kafka brokers is needed.

The `kafka-leader-election.sh` utility can be used to trigger manually a new leader election.

### Example

Start a preferred leader election for all topics in a cluster:

```shell
kafka-leader-election.sh --bootstrap-server localhost:9092 --election-type PREFERRED --all-topic-partitions
```

It is also possible to start elections on specific partitions or topics. This can be done by passing in a topic name
with the `--topic` option and a partition with the `--partition` option directly. It is also possible to pass in a list
of several partitions to be elected through a JSON file with the topic names.

### Example

Start a preferred replica election with a specified list of partitions in a file named `partitions.json`, with the
following content:

```json
{
  "partitions": [
    {
      "partition": 1,
      "topic": "my-topic"
    },
    {
      "partition": 2,
      "topic": "foo"
    }
  ]
}
```

```shell
kafka-leader-election.sh --bootstrap-server localhost:9092 --election-type PREFERRED --path-to-json-file partitions.json
```

The `kafka-reassign-partitions.sh` can be used to change the replica assignments manually for a partition.

The `kafka-dump-log.sh` tool can be used to decode the log segments for a partition.

The `kafka-replica-verification.sh` tool can be used to validate that the replicas for a topic's partitions are the same
across the cluster.

## Unsafe Operations

- Moving the Cluster Controller
- Removing Topics to Be Deleted
- Deleting Topics Manually
