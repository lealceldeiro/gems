# Chapter 6: Partitioning

_partition_

- _shard_ in MongoDB, Elasticsearch, and SolrCloud
- _region_ in HBase
- _tablet_ in Bigtable
- _vnode_ in Cassandra and Riak
- _vBucket_ in Couchbase

Partitioning is usually combined with replication so that copies of each partition are stored on multiple nodes.

The goal with partitioning is to spread the data and the query load evenly across nodes.

If the partitioning is unfair, so that some partitions have more data or queries than others, it's called _skewed_. The
presence of skew makes partitioning much less effective.

A partition with disproportionately high load is called a hot spot.

## Partitioning strategies

### Key range partitioning

While partitioning by key range, the keys are sorted, and a partition owns all the keys from some minimum up to some
maximum. Sorting has the advantage that efficient range queries are possible, but there is a risk of hot spots if the
application often accesses keys that are close together in the sorted order. In this approach, partitions are typically
rebalanced dynamically by splitting the range into two subranges when a partition gets too big.

### Hash partitioning

While partitioning by the hash of the keys, a hash function is applied to each key and each partition si assigned a
range of these hashes. This method destroys the ordering of keys, making range queries inefficient, but may distribute
load more evenly. When partitioning by hash, it is common to create a fixed number of partitions in advance, to assign
several partitions to each node, and to move entire partitions from one node to another when nodes are added or removed.
Dynamic partitioning can also be used.

### Main approaches to partitioning a database with secondary indexes:

- Document-partitioned indexes (local indexes), where the secondary indexes are stored in the same partition as the
  primary key and value. This means that only a single partition needs to be updated on write, but a read of the
  secondary index requires a scatter/gather across all partitions.
- Term-partitioned indexes (global indexes), where the secondary indexes are partitioned separately, using the indexed
  values. An entry in the secondary index may include records from all partitions of the primary key. When a document
  is written, several partitions of the secondary index need to be updated; however, a read can be served from a single
  partition.

## Rebalancing

The process of moving load from one node in the cluster to another is called rebalancing.

### Strategies

- Fixed number of partitions
- Dynamic partitioning
- Partitioning proportionally to nodes

## Request Routing

### Service discovery options

- Allow clients to contact any node (e.g., via a round-robin load balancer)
- Send all requests from clients to a routing tier first, which determines the node that should handle each request and
  forwards it accordingly. This routing tier does not itself handle any requests; it only acts as a partition-aware load
  balancer
- Require that clients be aware of the partitioning and the assignment of partitions to nodes. In this case, a client
  can connect directly to the appropriate node, without any intermediary
