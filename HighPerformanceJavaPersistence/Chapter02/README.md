# Chapter II: JDBC and Database Essentials

## 3. JDBC Connection Management

The JDBC (Java Database Connectivity) API provides a common interface for communicating to a database server.

Previously, the application required to load the driver prior to establishing a connection but, since JDBC 4.0, the Service Provider Interfaces mechanism can automatically discover all the available drivers in the current application class-path.

Every time the `getConnection()` method is called, the DriverManager will request a new physical connection from the underlying Driver.

### 3.2 DataSource

Opening and closing database connections is a very expensive operation, so reusing them has the following advantages:

* it avoids both the database and the driver overhead for establishing a TCP connection
* it prevents destroying the temporary memory buffers associated with each database connection
* it reduces client-side JVM object garbage

When using a connection pooling solution, the connection acquisition time is smaller. By reducing the connection acquisition interval, the overall transaction response time gets shorter too. All in all, in an enterprise application reusing connections is a much better choice than always establishing them on a transaction basis.

#### 3.2.1 Why is pooling so much faster?

* When a connection is being requested, the pool looks for unallocated connections
* If the pool finds a free one, it handles it to the client
* If there is no free connection, the pool tries to grow to its maximum allowed size
* If the pool already reached its maximum size, it will retry several times before giving up with a connection acquisition failure exception
* When the client closes the logical connection, the connection is released and returns to the pool without closing the underlying physical connection

The connection pool doesn’t return the physical connection to the client, but instead it offers a proxy or a handle. When a connection is in use, the pool changes its state to allocated to prevent two concurrent threads from using the same database connection. The proxy intercepts the connection close method call, and it notifies the pool to change the connection state to unallocated.

Apart from reducing connection acquisition time, the pooling mechanism can also limit the number of connections an application can use at once.

The connection pool acts as a bounded buffer for the incoming connection requests. If there is a traffic spike, the connection pool will level it, instead of saturating all the available database resources.

### 3.3 Queuing theory capacity planning

According to [Little’s Law](https://en.wikipedia.org/wiki/Little%27s_law), the average time for a request to be serviced depends only on the long-term request arrival rate and the average number of requests in the system.

```
L = λ × W
```

* `L` - average number of requests in the system (including both the requests being serviced and the ones waiting in the queue)
* `λ` - long-term average arrival rate
* `W` - average time a request spends in a system


**Example:**

Assuming that an application-level transaction uses the same database connection throughout its whole lifecycle, and the average transaction response time is 100 milliseconds, `W = 100 ms = 0.1 s`

if the average connection acquisition rate is 50 requests per second, `λ = 50`

then the average number of connection requests in the system is:

```
L = λ × W = 50 × 0.1 = 5 connection requests
```

A pool size of 5 can accommodate the average incoming traffic without having to enqueue any connection request. If the pool size is 3, then, on average, 2 requests are enqueued and waiting for connections to become available.

In queueing theory, throughput is represented by the departure rate (`μ`), and, for a connection pool, it represents the number of connections offered in a given unit of time:

```
μ = Ls / Ws = pool size / connection lease time
```

When the arrival rate equals departure rate, the system becomes saturated, all connections being in use.

```
λ = μ = Ls / Ws
```

If the arrival rate outgrows the connection pool throughput, the overflowing requests must wait for connections to become available.

Following the previous **example**, a one second traffic burst of 150 requests is handled as follows:

* the first 50 requests can be served in the first second
* the following 100 requests are first enqueued and processed in the following two seconds

```
μ = Ls / Ws = 5 / 0.1 = Lq / Wq = 10 / 0.2
```

For a constant throughput, the number of enqueued connection requests (`Lq`) is proportional to the connection acquisition time (`Wq`).

### 3.4 Practical database connection provisioning

By continuously monitoring the connection usage patterns, it’s much easier to react and adjust the pool size when the initial configuration doesn’t hold anymore.

## 4. Batch Updates

JDBC 2.0 introduced batch updates, so that multiple DML statements can be grouped into a single database request. Sending multiple statements in a single request reduces the number of database roundtrips, therefore decreasing transaction response time.

### 4.1 Batching Statements

For executing static SQL statements, JDBC defines the `Statement` interface and batching multiple DML statements is as straightforward as the following code snippet:

```java
statement.addBatch("INSERT INTO post (title, version, id) VALUES ('Post no. 1', 0, 1)");
statement.addBatch("INSERT INTO post_comment (post_id, review, version, id) VALUES (1, 'Post comment 1.1', 0, 1)");
int[] updateCounts = statement.executeBatch();
```

The numbers of database rows affected by each statement is included in the return value of the `executeBatch()` method.

### 4.2 Batching PreparedStatements

For dynamic statements, JDBC offers the `PreparedStatement` interface for binding parameters in a safely manner. The driver must validate the provided parameter at runtime, therefore discarding unexpected input values.

Here, the batch update can group multiple parameter values belonging to the same prepared statement. Example:

```java
PreparedStatement postStatement = connection.prepareStatement("INSERT INTO Post (title, version, id) VALUES (?, ?, ?)");

postStatement.setString(1, String.format("Post no. %1$d", 1));
postStatement.setInt(2, 0);
postStatement.setLong(3, 1);
postStatement.addBatch();

postStatement.setString(1, String.format("Post no. %1$d", 2));
postStatement.setInt(2, 0);
postStatement.setLong(3, 2);
postStatement.addBatch();

int[] updateCounts = postStatement.executeBatch();
```

For dynamic statements, PreparedStatement provides better performance (when enabling batching) and stronger security guarantees. Most ORM tools use prepared statements, and since entities are inserted/update/deleted individually, they can take advantage of batching.

#### 4.2.1 Choosing the right batch size

Like any other performance optimization technique, measuring the application performance gain in response to a certain batch size value remains the most reliable tuning option.

As a rule of thumb you should always measure the performance improvement for various batch sizes. In practice, a relatively low value (between 10 and 30) is usually a good choice.

### 4.2.2 Bulk operations

SQL offers bulk operations to modify all rows that satisfy a given filtering criteria. Bulk update or delete statements can also benefit from indexing, just like select statements.

### 4.3 Retrieving auto-generated keys

It’s common practice to delegate the row identifier generation to the database system. This way, the developer doesn’t have to provide a monotonically incrementing primary key since the database takes care of this upon inserting a new record.

To retrieve the newly created row identifier, the JDBC `PreparedStatement` must be instructed to return the auto-generated keys:

```java
PreparedStatement postStatement = connection.prepareStatement(
    "INSERT INTO post (title, version) VALUES (?, ?)",
    Statement.RETURN_GENERATED_KEYS
);
```

One alternative is to hint the driver about the column index holding the auto-generated key column:

```java
PreparedStatement postStatement = connection.prepareStatement(
    "INSERT INTO post (title, version) VALUES (?, ?)",
    new int[] {1}
);
```

Or, the column name can also be used to instruct the driver about the auto-generated key column:

```java
PreparedStatement postStatement = connection.prepareStatement(
    "INSERT INTO post (title, version) VALUES (?, ?)",
    new String[] {"id"}
);
```
> **Oracle auto-generated key retrieval gotcha**
>
> When using Statement.RETURN_GENERATED_KEYS , Oracle returns a ROWID instead of the actual generated column value. A workaround is to supply the column index or the column name, and so the auto-generated value can be extracted after executing the statement.

Not all database systems support fetching auto-generated keys from a batch of statements.

#### 4.3.1 Sequences to the rescue

As opposed to identity columns, database sequences offer the advantage of decoupling the identifier generation from the actual row insert.

To make use of batch inserts, the identifier must be fetched prior to setting the insert statement parameter values.

For batch processors inserting large amounts of data, the extra sequence calls can add up. As an optimization, the identifier generation process can be split among the database and the data access logic. The database sequences can be incremented in steps.

The data access logic can assign identifiers in-between the database sequence calls (e.g. 2, 3, 4, ..., N-1, N ), and so it mitigates the extra network roundtrips penalty.

## 5. Statement Caching

### 5.1 Statement lifecycle

The main database modules responsible for processing an SQL statement are the *Parser*, the *Optimizer* and the *Executor*.

#### 5.1.1 Parser

The Parser checks the SQL statement and ensures its validity. The statements are verified both syntactically and semantically.

During parsing, the SQL statement is transformed into a database internal representation, called the syntax tree.

#### 5.1.2 Optimizer

When finding an optimal execution plan, the Optimizer might evaluate multiple options, and, based on their overall cost, it will choose the one requiring the least amount of time to execute.

Finding a proper execution plan is resource intensive, and, for this purpose, some database vendors offer execution plan caching.

Among the many challenging aspects of the caching mechanism, the most challenging one is to ensure that only a good execution plan goes in the cache, since a bad plan, getting reused over and over, can really hurt application performance.

##### 5.1.2.1 Execution plan visualization

Oracle uses the `EXPLAIN PLAN FOR` syntax, and the output goes into the `dbms_xplan` package.

PostgreSQL reserves the `EXPLAIN` keyword for displaying execution plans.

The SQL Server Management Studio provides an execution plan viewer. Another option is to enable the `SHOWPLAN_ALL` setting prior to running a statement.

In MySQL, the plan is displayed using `EXPLAIN` or `EXPLAIN EXTENDED`.

#### 5.1.3 Executor

From the Optimizer, the execution plan goes to the Executor where it is used to fetch the associated data and build the result set.

The Executor makes use of the Storage Engine and the Transaction Engine. Having a reasonably large in-memory buffer allows the database to reduce the I/O contention, therefore reducing transaction response time.

The consistency model also has an impact on the overall transaction performance since locks may be acquired to ensure data integrity, and the more locking, the less the chance for parallel execution.


### 5.2 Caching performance gain

Most database systems can benefit from reusing statements and, in some particular use cases, the performance gain is quite substantial.

Statement caching plays a very important role in optimizing high-performance OLTP (Online transaction processing) systems.

### 5.3 Server-side statement caching

Dynamic-generated JDBC `Statement`s are not suitable for reusing execution plans.

Server-side prepared statements allow the data access logic to reuse the same execution plan for multiple executions.

For prepared statements, the execution plan can either be compiled on every execution or it can be cached and reused. Recompiling the plan can generate the best data access paths for any given bind variable set while paying the price of additional database resources usage. Reusing a plan can spare database resources, but it might not be suitable for every parameter value combination.

#### 5.3.1 Bind-sensitive execution plans

In database terminology, the number of rows returned by a given predicate is called *cardinality*.

The *predicate selectivity* is obtained by dividing cardinality with the total number of rows.

The lower the selectivity, the less rows will be matched for a given bind value, and the more selective the predicate gets.

The execution plan depends on bind parameter value selectivity. For example, the Optimizer tends to prefer sequential scans over index lookups for high selectivity percentages, to reduce the total number of disk-access roundtrips (especially when data is scattered among multiple data blocks).

If the selectivity is constant across the whole bind value domain, the execution plan is no longer sensitive to parameter values. A *generic* execution plan is much easier to reuse than a bind-sensitive one.

### 5.4 Client-side statement caching

The main goals of the client-side statement caching can be summarized as follows:

* reducing client-side statement processing, which, in turn, lowers transaction response time
* sparing application resources by recycling statement objects along with their associated database-specific metadata

## 6. ResultSet Fetching

Unlike the insert, update and delete statements, which only return the affected row count, a JDBC select query returns a `ResultSet` instead.

### 6.1 ResultSet scrollability

By default, the ResultSet uses a *forward-only* application-level cursor, which can be traversed only once, from the first position to last one.

JDBC also offers *scrollable* cursors, therefore allowing the row-level pointer to be positioned freely (in any direction and on every record).

The main difference between the two scrollable result sets lays in their selectivity. An *insensitive* cursor offers a static view over the current result set, so the data needs to be fetched entirely prior to being iterated. A *sensitive* cursor allows the result set to be fetched dynamically, so it can reflect concurrent changes.

### 6.2 ResultSet changeability

As a rule of thumb, if the current transaction doesn’t require updating selected records, the forward-only and read-only default result set type is the most efficient option. Even if it is a reminiscence from JDBC 1.0, the default result set is still the right choice in most situations.

### 6.3 ResultSet holdability

Unlike scrollability and updatability, the default value for holdability is implementation specific.

In a typical enterprise application, database connections are reused from one transaction to another, so holding a result set after a transaction ends is risky. Depending on the underlying database system and on the cursor type, a result set might allocate system resources, which, for scalability reasons, need to be released as soon as possible.

Although the `CLOSE_CURSORS_AT_COMMIT` holdability option is not supported by all database engines, the same effect can be achieved by simply closing all acquired `ResultSet`(s) and their associated Statement objects.

### 6.4 Fetching size

The JDBC `ResultSet` acts as an application-level cursor, so whenever the statement is traversed, the result must be transferred from the database to the client. The transfer rate is controlled by the `Statement` fetch size.

`statement.setFetchSize(fetchSize);`

A custom fetch size gives the driver a hint as to the number of rows needed to be retrieved in a single database roundtrip. The default value of *0* leaves each database choose its own driver-specific fetching policy.

### 6.5 ResultSet size

Setting the appropriate fetching size can undoubtedly speed up the result set retrieval, as long as a statement fetches only the data required by the current business logic.

#### 6.5.1 Too many rows

When the result set size is limited by external factors, it makes no sense to select more data than necessary.

Without placing upper bounds, the result sets grow proportionally with the underlying table data. A large result set requires more time to be extracted and to be sent over the wire too.

Limiting queries can therefore ensure predictable response times and database resource utilization. The shorter the query processing time, the quicker the row-level locks are released, and the more scalable the data access layer becomes.

The most efficient strategy of limiting a result set is to include the row restriction clause in the SQL statement. Another one is to configure a maximum row count at the JDBC `Statement` level.

##### 6.5.1.2 JDBC max rows

The JDBC specification defines the [`maxRows`](https://docs.oracle.com/javase/8/docs/api/java/sql/Statement.html#setMaxRows-int-) attribute which limits all ResultSet(s) for the current statement.

`statement.setMaxRows(maxRows);`

Unlike the SQL construct, the JDBC alternative is portable across all driver implementations. And according to the JDBC documentation, the driver is expected to discard the extra rows when the maximum threshold is reached, which is a poor strategy because it wastes both database resources (CPU, I/O, Memory) as well as networking bandwidth. This strategy is implementation specific and it might be more effective or ineffective from one DB implementation to another.

#### 6.5.2 Too many columns

Extracting too many columns can increase the result set processing response time. This situation is more prevalent among ORM tools, as for populating entities entirely, all columns are needed to be selected.

So, if a business case requires only a subset of all entity properties, fetching extra columns becomes a waste of database and application resources (CPU, Memory, I/O, Networking).

## 7. Transactions

A transaction is a collection of read and write operations that can either succeed or fail together, as a unit. All database statements must execute within a transactional context, even when the database client doesn’t explicitly define its boundaries.

ACID (Atomicity, Consistency, Isolation and Durability) are the four disctintive characteristics of transactions.

### 7.1 Atomicity

Atomicity is the property of grouping multiple operations into an all-or-nothing unit of work, which can succeed only if all individual operations succeed.

### 7.2 Consistency

Consistency is about validating the transaction state change, so that all committed transactions leave the database in a proper state. If only one constraint gets violated, the entire transaction is rolled back and all modifications are going to be reverted.

### 7.3 Isolation

The serializable execution is the only transaction isolation level that doesn’t compromise data integrity, while allowing a certain degree of parallelization.

#### 7.3.1 Concurrency control

There are basically two strategies for handling data collisions:

* avoiding conflicts (e.g. two-phase locking) requires locking to control access to shared resources
* detecting conflicts (e.g. Multi-Version Concurrency Control) provides better concurrency, at the price of relaxing serializability and possibly accepting various data anomalies.

##### 7.3.1.1 Two-phase locking

Each database system comes with its own lock hierarchy but the most common types remain the following ones:

* shared (read) lock, preventing a record from being written while allowing concurrent reads
* exclusive (write) lock, disallowing both read and write operations

Locks alone are not sufficient for preventing conflicts. A concurrency control strategy must define how locks are being acquired and released because this also has an impact on transaction interleaving. That's why the 2PL protocol splits a transaction in two sections:

* expanding phase (locks are acquired and no lock is released)
* shrinking phase (all locks are released and no other lock is further acquired)

In a locking-based concurrency control implementation, all currently interleaved transactions must follow the 2PL protocol as otherwise serializability might be compromised, resulting in data anomalies.

Using locking for controlling access to shared resources is prone to deadlocks, and the transaction scheduler alone cannot prevent their occurrences. Preserving the lock order becomes the responsibility of the data access layer, and the database can only assist in recovering from a deadlock situation.

The database engine runs a separate process that scans the current conflict graph for lock-wait cycles. When a cycle is detected, the database engine picks one transaction and aborts it, causing its locks to be released, so the other transaction can make progress.

##### 7.3.1.2 Multi-Version Concurrency Control

While 2PL prevents conflicts, Multi-Version Concurrency Control (MVCC) uses a conflict detection strategy instead.

#### 7.3.2 Phenomena

Relaxing serializability guarantees may generate data integrity anomalies, which are also referred as phenomena.

The SQL-92 standard introduced three phenomena that can occur when moving away from a serializable transaction schedule:

* dirty read
* non-repeatable read
* phantom read

There are other phenomena that can occur due to transaction interleaving, described in [A Critique of ANSI SQL Isolation Levels](https://www.microsoft.com/en-us/research/wp-content/uploads/2016/02/tr-95-51.pdf):

* dirty write
* read skew
* write skew
* lost update

> Choosing a certain isolation level is a trade-off between increasing concurrency and acknowledging the possible anomalies that might occur.
>
> Scalability is undermined by contention and coherency costs. The lower the isolation level, the less locking (or multi-version transaction abortions), and the more scalable the application will get.
>
> But a lower isolation level allows more phenomena, and the data integrity responsibility is shifted from the database side to the application logic, which must ensure that it takes all measures to prevent or mitigate any such data anomaly.

##### 7.3.2.1 Dirty write

A dirty write happens when two concurrent transactions are allowed to modify the same row at the same time. All changes are applied against the actual database object structures, which means that the second transaction simply overwrites the first transaction pending change.

> If the database engine didn’t prevent dirty writes, guaranteeing rollbacks would not be possible. Because atomicity cannot be implemented in the absence of reliable rollbacks, all database systems must therefore prevent dirty writes.

##### 7.3.2.2 Dirty read

A dirty read happens when a transaction is allowed to read the uncommitted changes of some other concurrent transaction.

> Cases for using Read Uncommitted are seldom (non-strict reporting queries where dirty reads are acceptable), so Read Committed is usually the lowest practical isolation level.

##### 7.3.2.3 Non-repeatable read

If one transaction reads a database row without applying a shared lock on the newly fetched record, then a concurrent transaction might change this row before the first transaction has ended.

> Repeatable Read and Serializable prevent this anomaly by default. With Read Committed, it’s possible to avoid non-repeatable (fuzzy) reads if the shared locks are acquired explicitly (e.g. `SELECT FOR SHARE`).

##### 7.3.2.4 Phantom read

If a transaction makes a business decision based on a set of rows satisfying a given predicate, without predicate locking, a concurrent transaction might insert a record matching that particular predicate.

> Traditionally, the Serializable isolation prevented phantom reads through predicate locking. Other MVCC implementations can detect phantom rows by introspecting the transaction schedule and aborting any transaction whose serializability guarantees were violated.

##### 7.3.2.5 Read skew

Read skew is an anomaly that involves a constraint on more than one database tables.

For example, one transaction `A` which reads values from two tables with constraitns which need to be in sync in both tables `T1` and `T2`, after reading the first part of the data, in table `T1`, another (interleaving) transaction `B` updates the values in tables `T1` and `T2`, and now transaction `A` finishes reading the other part of the data in table `T2`. This last part of the data is not in sync with the version of the first part of the data that `A` already got from `T1`, but with the real (updated by `B`) data in `T1`.

> Like with non-repeatable reads, there are two ways to avoid this phenomenon:
>
> * the first transaction can acquire shared locks on every read, therefore preventing the second transaction from updating these records
> * the first transaction can be aborted upon validating the commit constraints (when using an MVCC implementation of the Repeatable Read or Serializable isolation levels)

##### 7.3.2.6 Write skew

Like read skew, this phenomenon involves disjoint writes over two different tables that are constrained to be updated as a unit.

> Like with non-repeatable reads, there are two ways to avoid this phenomenon:
>
> * the first transaction can acquire shared locks on both entries, therefore preventing the second transaction from updating these records
> * the database engine can detect that another transaction has changed these records, and so it can force the first transaction to roll back (under an MVCC implementation of Repeatable Read or Serializable).

##### 7.3.2.7 Lost update

This phenomenon happens when a transaction reads a row while another transaction modifies it prior to the first transaction to finish.

> Traditionally, Repeatable Read protected against lost updates since the shared locks could prevent a concurrent transaction from modifying an already fetched record. With MVCC, the second transaction is allowed to make the change, while the first transaction is aborted when the database engine detects the row version mismatch (during the first transaction commit).

#### 7.3.3 Isolation levels

SQL-92 introduced multiple isolation levels, and the database client has the option of balancing concurrency against data correctness.

| Isolation Level  | Dirty read | Non-repeatable read | Phantom read |
| ---------------- | -----------| --------------------|--------------|
| Read Uncommitted | Yes        | Yes                 | Yes          |
| Read Committed   | No         | Yes                 | Yes          |
| Repeatable Read  | No         | No                  | Yes          |
| Serializable     | No         | No                  | No           |

Without an explicit setting, the JDBC driver uses the default isolation level.

The default isolation level can be gotten using [`getDefaultTransactionIsolation()`](https://docs.oracle.com/javase/8/docs/api/java/sql/DatabaseMetaData.html#getDefaultTransactionIsolation--):

```java
int level = connection.getMetaData().getDefaultTransactionIsolation();
```

It can be changed using [`setTransactionIsolation(int level)`](https://docs.oracle.com/javase/8/docs/api/java/sql/Connection.html#setTransactionIsolation-int-):

```java
connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
```

> Even if ACID properties imply a serializable schedule, most relational database systems use a lower default isolation level instead:
>
> * Read Committed (Oracle, SQL Server, PostgreSQL)
> * Repeatable Read (MySQL)

### 7.4 Durability

Durability ensures that all committed transaction changes become permanent. It allows system recoverability, and, to some extent, it’s similar to the rolling back mechanism.

When a transaction is committed, the database persists all current changes in an append-only, sequential data structure commonly known as the redo log.

### 7.5 Read-only transactions

The JDBC Connection defines the [`setReadOnly(boolean readOnly)`](https://docs.oracle.com/javase/8/docs/api/java/sql/Connection.html#setReadOnly%28boolean%29) method which can be used to hint the driver to apply some database optimizations for the upcoming read-only transactions. This method shouldn’t be called in the middle of a transaction because the database system cannot turn a read-write transaction into a read-only one (a transaction must start as read-only from the very beginning).

#### 7.5.1 Read-only transaction routing

Setting up a database replication environment is useful for both high-availability (a Slave can replace a crashing Master) and traffic splitting. In a Master-Slave replication topology, the Master node accepts both read-write and read-only transactions, while Slave nodes only take read-only traffic.

If the JDBC driver doesn’t support Master-Slave routing, the application can do it using multiple `DataSource` instances. This design cannot rely on the read-only status of the underlying `Connection` since the routing must take place before a database connection is fetched.

If the transaction manager supports declarative read-only transactions, the routing decision can be taken based on the current transaction read-only preference. Otherwise, the routing must be done manually in each service layer component, and so a read-only transaction uses a read-only `DataSource` or a read-only JPA `PersistenceContext`.

### 7.6 Transaction boundaries

Every database statement executes in the context of a database transaction, even if the client doesn’t explicitly set transaction boundaries.

While there might be single statement transactions (usually a read-only query), when the unit of work consists of multiple SQL statements, the database should wrap them all in a single unit of work.

Auto-commit should be avoided as much as possible, and, even for single statement transactions, it’s good practice to mark the transaction boundaries explicitly.

Since transactions management is a cross-cutting concern, declarative transactions management should be preferred. They break the strong coupling between the data access logic and the transaction management code. Thus, transaction boundaries are marked with metadata (e.g. annotations) and a separate transaction manager abstraction is in charge of coordinating transaction logic.

#### 7.6.1 Distributed transactions

The difference between local and global transactions is that the former uses a single resource manager, while the latter operates on multiple heterogeneous resource managers.

All transactional resource adapters are registered by the global transaction manager, which decides when a resource is allowed to commit or rollback.

##### 7.6.1.1 Two-phase commit

In a two-phase commit (2PC), a resource manager takes all the necessary actions to prepare the transaction for the upcoming commit. Only if all resource managers successfully acknowledge the preparation step, the transaction manager will proceed with the commit phase. If one resource doesn’t acknowledge the prepare phase, the transaction manager proceeds to rolling back all current participants.

If all resource managers acknowledge the commit phase, the global transaction ends successfully. If one resource fails to commit (or times out), the transaction manager will have to retry this operation in a background thread until it succeeds or reports the incident for manual intervention.

#### 7.6.2 Declarative transactions

Transaction boundaries are usually associated with a Service layer, which uses one or more DAO to fulfil the business logic. The transaction *propagates* from one component to the other within the service-layer transaction boundaries.

The declarative transaction model is supported by both Java EE and Spring. Transaction boundaries are controlled through similar propagation strategies, which define how boundaries are inherited or disrupted at the borderline between the outermost component (in the current call stack) and the current one (waiting to be invoked).

### 7.7 Application-level transactions

In a highly concurrent environment, database transactions are bound to be as short as possible. Application-level transactions require application-level concurrency control mechanisms.

The application-level repeatable reads are not self-sufficient (this argument is true for database isolation levels as well). To prevent lost updates, a concurrency control mechanism becomes mandatory.

#### 7.7.1 Pessimistic and optimistic locking

Isolation levels entail implicit locking, whether it involves physical locks (like 2PL) or data anomaly detection (MVCC). To coordinate state changes, application-level concurrency control makes use of explicit locking, which comes in two flavors: pessimistic and optimistic locking.

##### 7.7.1.1 Pessimistic locking

Most database systems offer the possibility of manually requesting shared or exclusive locks. This concurrency control is said to be *pessimistic* because it assumes that conflicts are bound to happen, and so they must be prevented accordingly.

##### 7.7.1.2 Optimistic locking

Optimistic locking doesn’t incur any locking at all. It uses a totally different approach to managing conflicts than pessimistic locking.

MVCC is an optimistic concurrency control strategy since it assumes that contention is unlikely to happen, and so it doesn’t rely on locking for controlling access to shared resources. The optimistic concurrency mechanisms detect anomalies and resort to aborting transactions whose invariants no longer hold.

The optimistic locking concurrency algorithm looks like this:

* when a client reads a particular row, its `version` comes along with the other fields
* upon updating a row, the client filters the current record by the version it has previously loaded.
  - i.e.: `UPDATE product SET (quantity, version) = (4, 2) WHERE id = 1 AND version = 1`
* if the statement update count is zero, the version was incremented in the meanwhile, and the current transaction now operates on a stale record version.
