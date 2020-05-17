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
