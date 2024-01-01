# CHAPTER 8: Exactly-Once Semantics

Mechanisms that provide exactly-once guarantees:

1. idempotent producer - which avoids duplicates that are caused by the retry mechanism
2. transactions - form the basis of exactly-once semantics in Kafka Streams

When the idempotent producer is enabled, each message will include a unique identified producer ID (_PID_) and a
_sequence number_, which together with the target _topic_ and _partition_, uniquely identify each message.

The idempotent producer will only prevent duplicates caused by the retry mechanism of the producer itself, whether the
retry is caused by producer, network, or broker errors. But nothing else.

Exactly-once processing means that consuming, processing, and producing are done _atomically_. Either the offset of the
original message is committed and the result is successfully produced or neither of these things happen. We need to make
sure that partial results—where the offset is committed but the result isn't produced, or vice versa—can't happen. To
support this behavior, Kafka transactions introduce the idea of atomic multi-partition writes.

A transactional producer is simply a Kafka producer that is configured with a `transactional.id` and has been
initialized using `initTransactions()`.

Transactions provide exactly-once guarantees when used within chains of consume-process-produce stream processing tasks.
In other contexts, transactions will either straight-out not work or will require additional effort in order to
achieve the guarantees we want.

Two mistakes are assuming that exactly-once guarantees apply on actions other than producing to Kafka, and that
consumers always read entire transactions and have information about transaction boundaries.

The most common and most recommended way to use transactions is to enable
exactly-once guarantees in Kafka Streams.

To enable exactly-once guarantees for a Kafka Streams application, we simply set the `processing.guarantee`
configuration to either `exactly_once` or `exactly_once_beta`.
