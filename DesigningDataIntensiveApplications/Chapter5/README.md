# PART II - Distributed Data

## Chapter 5: Replication

Replication can serve several purposes:

- High availability
- Enable disconnected operation
- Decrease latency
- Scalability

### Synchronous Versus Asynchronous Replication

Synchronous: the leader waits until the followers have confirmed that they received the write before reporting success
to the user, and before making the write visible to other clients.

Asynchronous: the leader sends the message, but doesn't wait for a response from the followers.

### Implementation of Replication Logs

- Statement-based replication
- Write-ahead log (WAL) shipping
- Logical (row-based) log replication
- Trigger-based replication

### Problems with Replication Lag

The delay between a write happening on a leader and being reflected on a follower —the replication lag— may be only a
fraction of a second, and not noticeable in practice. However, if the system is operating near capacity or if there is a
problem in the network, the lag can easily increase to several seconds or even minutes.

#### Reading Your Own Writes

If the user views the data shortly after making a write, the new data may not yet have reached the replica. In this
situation, we need _read-after-write consistency_, also known as _read-your-writes consistency_.

The same issue can arise when the user is accessing the service from multiple devices, for example a desktop web browser
and a mobile app. In this case _cross-device read-after-write consistency_ needs to be provided: if the user enters some
information on one device and then views it on another device, they should see the information they just entered.

#### Monotonic Reads

It's possible for a user to see things _moving backward in time_ when reading from asynchronous followers. This can
happen if a user makes several reads from different replicas (because the lagging follower has not yet picked up a
write which has been picked up already by the replica that handled the first read). In effect, the second query is
observing the system at an earlier point in time than the first query.

_Monotonic reads_ is a guarantee that this kind of anomaly does not happen (i.e.: users will not read older data after
having previously read newer data). It’s a lesser guarantee than strong consistency, but a stronger guarantee than
eventual consistency.

#### Consistent Prefix Reads

_Consistent prefix reads guarantee_ says that if a sequence of writes happens in a certain order, then anyone reading
those write will see them appear in the same order.

### Main approaches to replication

- Single-leader
- Multi-leader
- Leaderless

### Multi-Leader Replication

Leader-based replication has one major downside: there is only one leader, and all writes must go through it.

In a _multi-leader_ configuration (also known as master–master or active/active replication) replication still happens
in the same way: each node that processes a write must forward that data change to all the other nodes.

In this setup, each leader simultaneously acts as a follower to the other leaders.

#### Use Cases for Multi-Leader Replication

- Multi-datacenter operation
- Clients with offline operation
- Collaborative editing

#### Handling Write Conflicts

In a multi-leader setup, two concurrent conflicting writes, handled by two different leaders are successful, and the
conflict is only detected asynchronously at some later point in time.

The simplest strategy for dealing with conflicts is to avoid them: if the application can ensure that all writes for a
particular record go through the same leader, then conflicts cannot occur.

If conflict must be resolved, it must be done in a _convergent_ way, which means that all replicas must arrive at the
same final value when all changes have been replicated.

Ways of achieving convergent conflict resolution:

- Last write wins (LWW). This approach is prone to data loss.
- Let writes that originated at a higher-numbered replica always take precedence over writes that originated at a
  lower-numbered replica. This approach is prone to data loss.
- "Merge" the values together.
- Record the conflict and write application code that resolves it at some later time.

#### Multi-Leader Replication Topologies

A replication topology describes the communication paths along which writes are propagated from one node to another.

#### Quorums for reading and writing

If there are `n` replicas, every write must be confirmed by `w` nodes to be considered successful, and we must query at
least `r` nodes for each read.

As long as `w + r > n`, we expect to get an up-to-date value when reading, because at least one of the r nodes we’re
reading from must be up-to-date.
