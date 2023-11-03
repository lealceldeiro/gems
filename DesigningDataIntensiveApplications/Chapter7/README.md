# Chapter 7: Transactions

Transactions are an abstraction layer that allows an application to pretend that certain concurrency problems and
certain kinds of hardware and software faults don't exist.

## ACID: Atomicity, Consistency, Isolation, and Durability

Systems that do not meet the ACID criteria are sometimes called BASE, which stands for Basically Available, Soft state,
and Eventual consistency.

The ability to abort a transaction on error and have all writes from that transaction discarded is the defining feature
of ACID _atomicity_. In other words, if an error occurs halfway through a sequence of writes, the transaction should
be aborted, and the writes made up to that point should be discarded. This means the database saves developers from
having to worry about partial failure, by giving an all-or-nothing guarantee.

In the context of ACID, _consistency_ refers to an application-specific notion of the database being in a “good state”.

_Isolation_ in the sense of ACID means that concurrently executing transactions are isolated from each other: they
cannot step on each other's toes. In other words, concurrently running transactions shouldn’t interfere with each other.
For example, if one transaction makes several writes, then another transaction should see either all or none of those
writes, but not some subset.

_Durability_ is the promise that once a transaction has committed successfully, any data it has written will not be
forgotten, even if there is a hardware fault or the database crashes.

## Race conditions

- Dirty reads: One client reads another client's writes before they have been committed. The read committed isolation
  level and stronger levels prevent dirty reads.
- Dirty writes: One client overwrites data that another client has written, but not yet committed.Almost all transaction
  implementations prevent dirty writes.
- Read skew (nonrepeatable reads): A client sees different parts of the database at different points in time. This issue
  is most commonly prevented with snapshot isolation, which allows a transaction to read from a consistent snapshot at
  one point in time. It is usually implemented with multi-version concurrency control (MVCC).
- Lost updates: Two clients concurrently perform a read-modify-write cycle. One overwrites the other's write without
  incorporating its changes, so data is lost. Some implementations of snapshot isolation prevent this anomaly
  automatically, while others require a manual lock (`SELECT FOR UPDATE`).
- Write skew: A transaction reads something, makes a decision based on the value it saw, and writes the decision to the
  database. However, by the time the write is made, the premise of the decision is no longer true. Only serializable
  isolation prevents this anomaly.
- Phantom reads: A transaction reads objects that match some search condition. Another client makes a write that affects
  the results of that search. Snapshot isolation prevents straightforward phantom reads, but phantoms in the context of
  write skew require special treatment, such as index-range locks.

## Approaches to implementing serializable transactions

- Literally executing transactions in a serial order: If you can make each transaction very fast to execute, and the
  transaction throughput is low enough to process on a single CPU core, this is a simple and effective option.
- Two-phase locking: For decades this has been the standard way of implementing serializability, but many applications
  avoid using it because of its performance characteristics.
- Serializable snapshot isolation (SSI): Algorithm that avoids most of the downsides of the previous approaches. It uses
  an optimistic approach, allowing transactions to proceed without blocking. When a transaction wants to commit, it is
  checked, and it is aborted if the execution was not serializable.

## Weak Isolation Levels

By providing transaction isolation, databases have tried to hide concurrency issues from application developers.

Serializable isolation means that the database guarantees that transactions have the same effect as if they ran
serially (i.e., one at a time, without any concurrency).

Weaker levels of isolation protects against some concurrency issues, but not all.

### Read Committed

#### Guarantees:

1. When reading from the database, you will only see data that has been committed (no dirty reads).
2. When writing to the database, you will only overwrite data that has been committed (no dirty writes).

It is the default setting in some databases such as Oracle 11g, PostgreSQL, SQL Server 2012 and MemSQL.

### Snapshot Isolation and Repeatable Read

_Snapshot isolation_ is called _serializable_ in Oracle and _repeatable read_ in PostgreSQL and MySQL.

Nonrepeatable read or read skew: anomaly that happens when the same read to the database at different points in time
returns different values within the same transaction (without being committed yet). This is considered acceptable
under read committed isolation.

_Snapshot isolation_ is the most common solution to the previously described anomaly. The idea is that each transaction
reads from a _consistent snapshot_ of the database.

From a performance point of view, a key principle of snapshot isolation is _readers never block writers_, and
_writers never block readers_.

To implement snapshot isolation, databases must potentially keep several different committed versions of an object,
because various in-progress transactions may need to see the state of the database at different points in time. This
technique is known as _multi-version concurrency control_ (MVCC).

Storage engines that support snapshot isolation typically use MVCC for their read committed isolation level as well.

### Preventing Lost Updates

The lost update problem can occur when two transactions, concurrently, read some value from the database, modifies it,
and writes back the modified value -- one of the modifications can be lost, because the second write does not include
the first modification. Examples:

- Incrementing a counter or updating an account balance
- Making a local change to a complex value, e.g., adding an element to a list within a JSON document
- Two users editing a wiki page at the same time, where each user saves their changes by sending the entire page
  contents to the server, overwriting whatever is currently in the database

#### Possible solutions

- Atomic write operations
- Explicit locking
- Automatically detecting lost updates (PostgreSQL's _repeatable read_, Oracle's _serializable_, and SQL Server's
  _snapshot isolation_ levels automatically detect when a lost update has occurred and abort the offending transaction)
- Compare-and-set (in some databases)
- Conflict resolution and replication

In replicated databases some additional steps need to be taken to prevent lost updates.

### Write Skew and Phantoms

A write skew can be seen as a generalization of the lost update problem. It can occur if two transactions read the same
objects, and then update some of those objects (different transactions may update different objects).

## Serial execution

Serial execution of transactions has become a viable way of achieving serializable isolation within certain constraints:

- Every transaction must be small and fast, because it takes only one slow transaction to stall all transaction
  processing.
- It is limited to use cases where the active dataset can fit in memory. Rarely accessed data could potentially be moved
  to disk, but if it needed to be accessed in a single-threaded transaction, the system would get very slow.
- Write throughput must be low enough to be handled on a single CPU core, or else transactions need to be partitioned
  without requiring cross-partition coordination.
- Cross-partition transactions are possible, but there is a hard limit to the extent to which they can be used.

## Two-Phase Locking (2PL)

Several transactions are allowed to concurrently read the same object as long as nobody is writing to it. But as soon
as anyone wants to write (modify or delete) an object, exclusive access is required:

- If transaction A has read an object and transaction B wants to write to that object, B must wait until A commits or
  aborts before it can continue. This ensures that B can't change the object unexpectedly behind A's back.
- If transaction A has written an object and transaction B wants to read that object, B must wait until A commits or
  aborts before it can continue. Reading an old version of the object, is not acceptable under 2PL.
