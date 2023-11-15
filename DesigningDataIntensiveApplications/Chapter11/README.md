# Chapter 11: Stream Processing

## Message brokers:

### AMQP/JMS-style message broker

The broker assigns individual messages to consumers, and consumers acknowledge individual messages when they have been
successfully processed. Messages are deleted from the broker once they have been acknowledged. This approach is
appropriate as an asynchronous form of RPC, for example in a task queue, where the exact order of message processing is
not important and where there is no need to go back and read old messages again after they have been processed.

### Log-based message broker

The broker assigns all messages in a partition to the same consumer node, and always delivers messages in the same
order. Parallelism is achieved through partitioning, and consumers track their progress by checkpointing the offset of
the last message they have processed. The broker retains messages on disk, so it is possible to jump back and reread
old messages if necessary.

The log-based approach is especially appropriate for stream processing systems that consume input streams and generate
derived state or derived output streams.

----

Streams may come from (among others) several sources: user activity events, sensors providing periodic readings, and
data feeds (e.g., market data in finance). These are naturally represented as streams.

Representing databases as streams offers opportunities for integrating other systems. i.e.: you can keep derived data
systems such as search indexes, caches, and analytics systems continually up to date by consuming the log of changes
and applying them to the derived system. You can even build fresh views onto existing data by starting from scratch
and consuming the log of changes from the beginning all the way to the present.

Several purposes of stream processing include searching for event patterns (complex event processing), computing
windowed aggregations (stream analytics), and keeping derived data systems up to date (materialized views).

Types of joins that may appear in stream processes:

- Stream-stream joins: Both input streams consist of activity events, and the join operator searches for related events
  that occur within some window of time. For example, it may match two actions taken by the same user within 30 minutes
  of each other. The two join inputs may in fact be the same stream (a self-join) if you want to find related events
  within that one stream.
- Stream-table joins: One input stream consists of activity events, while the other is a database changelog. The
  changelog keeps a local copy of the database up to date. For each activity event, the join operator queries the
  database and outputs an enriched activity event.
- Table-table joins: Both input streams are database changelogs. In this case, every change on one side is joined with
  the latest state of the other side. The result is a stream of changes to the materialized view of the join between
  the two tables.
