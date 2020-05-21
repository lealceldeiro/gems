# Chapter III: JPA and Hibernate

## 8. Why JPA and Hibernate matter

JDBC suffers from the following shortcomings:

* the API is undoubtedly verbose, even for trivial tasks
* batching is not transparent from the data access layer perspective, requiring a specific API than its non-batched statement counterpart
* lack of built-in support for explicit locking and optimistic concurrency control
* for local transactions, the data access is tangled with transaction management semantics.
* fetching joined relations requires additional processing to transform the ResultSet into Domain Models or DTO (Data Transfer Object) graphs

### 8.1 The impedance mismatch

In a relational database, data is stored in tables and the relational algebra defines how data associations are formed

The Domain Model encapsulates the business logic specifications and captures both data structures and the behavior that governs business requirements.

The ORM design pattern helps bridging these two different data representations and close the technological gap between them. Every database row is associated with a Domain Model object (Entity in JPA terminology), and so the ORM tool can translate the entity state transitions into DML statements.

### 8.2 JPA and Hibernate

JPA is only a *specification*. It describes the interfaces that the client operates with and the standard object-relational mapping metadata (Java annotations or XML descriptors). JPA also explains how these specifications are ought to be implemented by the JPA providers.

Hibernate *implements* the JPA specification, but it also *retains its native API* for both backward compatibility and to accommodate non-standard features.

Hibernate comes with the following non-JPA compliant features:

* extended identifier generators, implementing a HiLo optimizer that’s interoperable with other database clients
* transparent prepared statement batching
* customizable CRUD (`@SQLInsert`, `@SQLUpdate`, `@SQLDelete`) statements
* static or dynamic collection filters (e.g. `@FilterDef`, `@Filter`, `@Where`)
* entity filters (e.g. `@Where`)
* mapping properties to SQL fragments (e.g. `@Formula`)
* immutable entities (e.g. `@Immutable`)
* more flush modes (e.g. `FlushMode.MANUAL`, `FlushMode.ALWAYS`)
* querying the second-level cache by the natural key of a given entity
* entity-level cache concurrency strategies (e.g. `Cache(usage = CacheConcurrencyStrategy.READ_WRITE)`)
* versioned bulk updates through HQL
* exclude fields from optimistic locking check (e.g. `@OptimisticLock(excluded = true)`)
* version-less optimistic locking (e.g. `OptimisticLockType.ALL`, `OptimisticLockType.DIRTY`)
* support for skipping (without waiting) pessimistic lock requests

> If JPA is the interface, Hibernate is one implementation and implementation details always matter from a performance perspective.

### 8.3 Schema ownership

The schema ownership goes to the database and the data access layer must assist the Domain Model to communicate with the underlying data.

### 8.4 Write-based optimizations

The JPA [EntityManager](https://docs.oracle.com/javaee/7/api/javax/persistence/EntityManager.html#persist-java.lang.Object-) and the [Hibernate Session](https://docs.jboss.org/hibernate/stable/orm/javadocs/org/hibernate/Session.html) interfaces are gateways towards the underlying Persistence Context, and they define all the entity state transition operations.

> **SQL injection prevention**
>
> By managing the SQL statement generation, the JPA tool can assist in minimizing the risk of SQL injection attacks. The less the chance of manipulating SQL String statements, the safer the application can get. The risk is not completely eliminated because the application developer can still recur to concatenating SQL or JPQL fragments, so rigour is advised.
>
> Hibernate uses PreparedStatement(s) exclusively, so not only it protect against SQL injection, but the data access layer can better take advantage of server-side and client-side statement caching as well.

> **Auto-generated DML statements**
>
> Because the JPA provider auto-generates insert and update statements, the data access layer can easily accommodate database table structure modifications. By updating the entity model schema, Hibernate can automatically adjust the modifying statements accordingly.
>
> The entity fetching process is automatically managed by the JPA implementation, which autogenerates the select statements of the associated database tables. This way, JPA can free the application developer from maintaining entity selection queries as well.
>
> Hibernate allows customizing all the CRUD statements, in which case the application developer is responsible for maintaining the associated DML statements.
>
> Although it takes care of the entity selection process, most enterprise systems need to take advantage of the underlying database querying capabilities. For this reason, whenever the database schema changes, all the native SQL queries need to be updated manually.

> **Write-behind cache**
>
> The Persistence Context acts as a transactional write-behind cache, deferring entity state flushing up until the last possible moment.
> 
> Because every modifying DML statement requires locking (to prevent dirty writes), the write behind cache can reduce the database lock acquisition interval, therefore increasing concurrency.

> **Transparent statement batching**
>
> Batch updates can be enabled transparently, even after the data access logic has been implemented.
>
> With just one configuration, Hibernate can execute all prepared statements in batches.

> **Application-level concurrency control**
>
> The JPA optimistic locking mechanism allows preventing lost updates because it imposes a happens before event ordering.
>
> In multi-request conversations, optimistic locking requires maintaining old entity snapshots, and JPA makes it possible through *Extended Persistence Contexts* or detached entities.
>
> JPA also supports a pessimistic locking query abstraction, which comes in handy when using lower-level transaction isolation modes.
>
> Hibernate has a native pessimistic locking API, which brings support for timing out lock acquisition requests or skipping already acquired locks.

### 8.5 Read-based optimizations

The database cannot be abstracted out of this context, and pretending that entities can be manipulated just like any other plain objects is very detrimental to application performance. When it comes to reading data, the impedance mismatch becomes even more apparent, and, for performance reasons, it’s mandatory to keep in mind the SQL statements associated with every fetching operation.

Each business use case has different data access requirements, and one policy cannot anticipate all possible use cases, so the fetching strategy should always be set up on a query basis.

Although it is very convenient to fetch entities along with all their associated relationships, it’s better to take into consideration the performance impact as well.

In reality, not all use cases require loading entities anyway, and not all read operations need to be served by the same fetching mechanism. Sometimes a custom projection (selecting only a few columns from an entity) is much more suitable, and the data access logic can even take advantage of database specific SQL constructs that might not be supported by the JPA query abstraction.

As a rule of thumb, fetching entities is suitable when the logical transaction requires modifying them, even if that will only happen in a successive web request.

The Persistence Context is also known as the first-level cache, and so it cannot be shared by multiple concurrent transactions.

the second-level cache is associated with an EntityManagerFactory , and all Persistence Contexts have access to it. The second-level cache can store entities as well as entity associations and even entity query results.

Because JPA doesn’t make it mandatory, each provider takes a different approach to caching. 

Although the second-level cache can mitigate the entity fetching performance issues, it requires a distributed caching implementation, which might not elude the networking penalties anyway.

### 8.6 Wrap-up

Bridging two highly-specific technologies is always a difficult problem to solve. When the enterprise system is built on top of an object-oriented language, the object-relational impedance mismatch becomes inevitable. The ORM pattern aims to close this gap although it cannot completely abstract it out.

A high-performance enterprise application must resonate with the underlying database system, and the ORM tool must not disrupt this relationship.

## 9. Connection Management and Monitoring
