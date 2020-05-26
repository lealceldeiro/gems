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

### 9.1 JPA connection management

In a Java EE container, all database connections are managed by the application server which provides connection pooling, monitoring and JTA capabilities.

While for a Java EE application it’s perfectly fine to rely on the application server for providing a full-featured DataSource reference, stand-alone applications are usually configured using dependency injection rather than JNDI.

### 9.2 Hibernate connection providers

Hibernate needs to operate both in Java EE and stand-alone environments, and the database connectivity configuration can be done either declaratively or programmatically.

#### 9.2.1 `DriverManagerConnectionProvider`

Hibernate picks this provider when being given JPA 2.0 connection properties or the Hibernate-specific configuration counterpart.

> Although it fetches database connections through the underlying DriverManager, this provider tries to avoid the connection acquisition overhead by using a trivial pooling implementation. The Hibernate documentation doesn’t recommend using the DriverManagerConnectionProvider in a production setup.

#### 9.2.2 `C3P0ConnectionProvider`

[C3p0](https://www.mchange.com/projects/c3p0/) is a mature connection pooling solution that has proven itself in many production environments, and, using the underlying JDBC connection properties, Hibernate can replace the built-in connection pool with a c3p0 DataSource. To activate this provider, the application developer must supply at least one configuration property starting with the `hibernate.c3p0` prefix.

#### 9.2.3 `HikariConnectionProvider`

HikariCP is one of the fastest Java connection pool, and, although not natively supported by Hibernate, it also comes with its own `ConnectionProvider` implementation. By specifying the `hibernate.connection.provider_class` property, the application developer can override the default connection provider mechanism:

```
<property name="hibernate.connection.provider_class"
          value="com.zaxxer.hikari.hibernate.HikariConnectionProvider"
/>
```

[HikariCP](https://github.com/brettwooldridge/HikariCP) doesn’t recognize the JPA or Hibernate-specific connection properties. The `HikariConnectionProvider` requires framework-specific properties.

#### 9.2.4 `DatasourceConnectionProvider`

This provider is chosen when the JPA configuration file defines a `non-jta-data-source` or a `jta-data-source` element, or when supplying a `hibernate.connection.datasource` configuration property.

#### 9.2.5 Connection release modes

The connection release strategy is controlled through the `hibernate.connection.release_mode` property (which can take the following values: `after_transaction`, `after_statement`, `auto`).

> The `after_transaction` connection release mode is more efficient than the default JTA `after_statement` strategy, and so it should be used if the JTA transaction resource management logic doesn’t interfere with this connection releasing strategy.

### 9.3 Monitoring connections

#### 9.3.1 Hibernate statistics

Hibernate has a built-in statistics collector which gathers notifications related to database connections, `Session` transactions and even second-level caching usage. The `StatisticsImplementor` interface defines the contract for intercepting various Hibernate internal events.

The statistics mechanism is disabled by default. To enable the statistics gathering mechanism, the following property must be configured first: `<property name="hibernate.generate_statistics" value="true"/>`.

Once statistics are being collected, in order to print them into the current application log, the following logger configuration must be set up:

```
<logger name="org.hibernate.engine.internal.StatisticalLoggingSessionEventListener" level="info" />
```

It’s better to use a mature framework such as Dropwizard Metrics instead of building a custom implementation from scratch.

> For a high-performance data access layer, statistics and metrics becomes mandatory requirements. The Hibernate statistics mechanism is a very powerful tool, allowing the development team to get a better insight into Hibernate inner workings.

### 9.4 Statement logging

When a business logic is implemented, the Definition of Done should include a review of all the associated data access layer operations. Following this rule can save a lot of hassle when the enterprise system is deployed into production.

The most straight-forward way of logging SQL statements along with their runtime bind parameter values is to use an external DataSource proxy. Because the proxy intercepts all statement executions, the bind parameter values can be introspected and printed as well.

#### 9.4.3 Logging parameters

Either the JDBC Driver or the DataSource must be proxied to intercept statement executions and log them along with the actual parameter values. Besides statement logging, a JDBC proxy can provide other cross-cutting features like long-running query detection or custom statement execution listeners.

## 10. Mapping Types and Identifiers

Although it’s common practice to map all database columns, this is not a strict requirement. Sometimes it’s more practical to use a root entity and several sub-entities, so each business case fetches just as much info as needed (while still benefiting from entity state management).

Identifiers are mandatory for entity elements, and an embeddable type is forbidden to have an identity of its own. Knowing the database table and the column that uniquely identifies any given row, Hibernate can correlate database rows with Domain Model entities.

The Domain Model can share state between multiple entities either by using inheritance or composition. Embeddable types can reuse state through composition.

### 10.1 Types

#### 10.1.1 Primitive types

Only non-nullable database columns can be mapped to Java primitives (boolean, byte, short, char, int, long, float, double). For mapping nullable columns, it’s better to use the primitive wrappers instead (Boolean, Byte, Short, Char, Integer, Long, Float, Double).

#### 10.1.2 String types

A Java `String` can consume as much memory as the Java Heap has available. On the other hand, database systems define both limited-size types (`VARCHAR` and `NVARCHAR`) and unlimited ones (`TEXT`, `NTEXT`, `BLOB` and `NCLOB`).

#### 10.1.3 Date and Time types

Handling time is tricky because of various time zones, leap seconds and day-light saving conventions. Storing timestamps in UTC (Coordinated Universal Time) and doing time zone transformations in the data layer is common practice.

#### 10.1.5 Binary types

For binary types, most database systems offer multiple storage choices (e.g. `RAW`, `VARBINARY`, `BYTEA`, `BLOB`, `CLOB`). In Java, the data access layer can use an array of byte(s), a JDBC `Blob` or `Clob`, or even a `Serializable` type, if the Java object was marshaled prior to being saved to the database.

### 10.2 Identifiers

Requiring less space and being more index-friendly, numerical sequences are preferred over UUID keys.

#### 10.2.1 UUID identifiers

The UUID key can either be generated by the application using the `java.util.UUID` class or it can be assigned by the database system.

If the database system doesn’t have a built-in UUID type, a `BINARY(16)` column type is preferred. Although a `CHAR(32)` column could also store the UUID textual representation, the additional space overhead makes it a less favorable pick.

## 11. Relationships

### 11.1 Relationship types

* `@ManyToOne` represents the child-side (where the foreign key resides) in a database one-to-many table relationship
* `@OneToMany` is associated with the parent-side of a one-to-many table relationship
* `@ElementCollection` defines a one-to-many association between an entity and multiple value types (basic or embeddable)
* `@OneToOne` is used for both the child-side and the parent-side in a one-to-one table relationship
* `@ManyToMany` mirrors a many-to-many table relationship

When handling large data sets, it’s good practice to limit the result set size, both for UI (to increase responsiveness) or batch processing tasks (to avoid long running transactions). Just because JPA offers supports collection mapping, it doesn’t mean they are mandatory for every domain model mapping. Until there’s a clear understanding of the number of child records (or if there’s even a need to fetch child entities entirely), it’s better to post pone the collection mapping decision. For high-performance systems, a data access query is often a much more flexible alternative anyway.

### 11.2 `@ManyToOne`

When using a `@ManyToOne` association, the underlying foreign key is controlled by the child-side, no matter the association is unidirectional or bidirectional.

Because the @ManyToOne association controls the foreign key directly, the automatically generated DML statements are very efficient.

Actually, the best performing JPA associations always rely on the child-side to translate the JPA state to the foreign key column value.

### 11.3 `@OneToMany`

While the `@ManyToOne` association is the most natural mapping of the one-to-many table relationship, the `@OneToMany` association can also mirror this database relationship, but only when being used as a bidirectional mapping.

A unidirectional `@OneToMany` association uses an additional junction table, which no longer fits the one-to-many table relationship semantics.

#### 11.3.1 Bidirectional `@OneToMany`

Even if the child-side is in charge of synchronizing the entity state changes with and the database foreign key column value, a bidirectional association must always have both the parent-side and the child-side in sync.

To synchronize both ends, it’s practical to provide parent-side helper methods that add/remove child entities.

```
@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
private List<PostComment> comments = new ArrayList<>();
// ...
public void addComment(PostComment comment) {
  comments.add(comment);
  comment.setPost(this);
}

public void removeComment(PostComment comment) {
  comments.remove(comment);
  comment.setPost(null);
}
```

One of the major advantages of using a bidirectional association is that entity state transitions can be cascaded from the parent entity to its children. In the following example, when persisting the parent Post entity, all the PostComment child entities are persisted as well.

```
Post post = new Post("First post");
entityManager.persist(post);

PostComment comment1 = new PostComment("My first review");
post.addComment(comment1);

PostComment comment2 = new PostComment("My second review");
post.addComment(comment2);

entityManager.persist(post);

INSERT INTO post (title, id) VALUES ('First post', 1)
INSERT INTO post_comment (post_id, review, id) VALUES (1, 'My first review', 2)
INSERT INTO post_comment (post_id, review, id) VALUES (1, 'My second review', 3)
```

When removing a comment from the parent-side collection, the orphan removal attribute will instruct Hibernate to generate a delete DML statement on the targeted child entity:

```
post.removeComment(comment1);

DELETE FROM post_comment WHERE id = 2
```

> **Equality-based entity removal**
>
> The helper method for the child entity removal relies on the underlying child object equality for matching the collection entry that needs to be removed.
>
> If the application developer doesn’t choose to override the default `equals` and `hashCode` methods, the `java.lang.Object` identity-based equality is going to be used. The problem with this approach is that the application developer must supply a child entity object reference that’s contained in the current child collection.
>
> Otherwise, the `equals` and the `hashCode` methods must be overridden to express equality in terms of a unique business key. In case the child entity has a `@NaturalId` or a unique property/properties set, the `equals` and the `hashCode` methods can be implemented on top of that.

The bidirectional `@OneToMany` association generates efficient DML statements because the `@ManyToOne` mapping is in charge of the table relationship. Because it simplifies data access operations as well, the bidirectional `@OneToMany` association is worth considering when the size of the child records is relatively low.

#### 11.3.2 Unidirectional `@OneToMany`

In spite its simplicity, the unidirectional `@OneToMany` association is less efficient than the unidirectional `@ManyToOne` mapping or the bidirectional `@OneToMany` association.

The unidirectional `@OneToMany` association doesn’t map to a one-to-many table relationship. Because there is no `@ManyToOne` side to control this relationship, Hibernate uses a separate junction table to manage the association between a parent row and its child records.

Some drawbacks of this apporach are:

* Joining three tables is less efficient than joining just two
* Because there are two foreign keys, there needs to be two indexes (instead of one), so the indexes memory footprint increases
* A unidirectional association requires additional inserts for the junction table records

> The unidirectional `@OneToMany` relationship is less efficient both for reading data, as for adding or removing child entries.
