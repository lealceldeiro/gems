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

```
statement.addBatch("INSERT INTO post (title, version, id) VALUES ('Post no. 1', 0, 1)");
statement.addBatch("INSERT INTO post_comment (post_id, review, version, id) VALUES (1, 'Post comment 1.1', 0, 1)");
int[] updateCounts = statement.executeBatch();
```

The numbers of database rows affected by each statement is included in the return value of the `executeBatch()` method.

### 4.2 Batching PreparedStatements

For dynamic statements, JDBC offers the `PreparedStatement` interface for binding parameters in a safely manner. The driver must validate the provided parameter at runtime, therefore discarding unexpected input values.

Here, the batch update can group multiple parameter values belonging to the same prepared statement. Example:

```
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

```
PreparedStatement postStatement = connection.prepareStatement(
    "INSERT INTO post (title, version) VALUES (?, ?)",
    Statement.RETURN_GENERATED_KEYS
);
```

One alternative is to hint the driver about the column index holding the auto-generated key column:

```
PreparedStatement postStatement = connection.prepareStatement(
    "INSERT INTO post (title, version) VALUES (?, ?)",
    new int[] {1}
);
```

Or, the column name can also be used to instruct the driver about the auto-generated key column:

```
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
