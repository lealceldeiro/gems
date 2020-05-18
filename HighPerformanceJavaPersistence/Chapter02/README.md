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
