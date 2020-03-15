# Chapter 3: Working with Indexes

## What is an index?

An index is a quick access path to a single row of a table in the database. A database index is similar to a book index where any specific information can be located by looking at the index page to avoid the full search of the book, which is an exhaustive operation. Similarly, a database index is created to minimize table traversal and maximize performance.

When PostgreSQL executes a query, it must choose an execution strategy and the [`EXPLAIN`](https://www.postgresql.org/docs/current/sql-explain.html) query command can be used to check which scan will be used for the query.

## How to create an index?

The [CREATE INDEX](https://www.postgresql.org/docs/current/sql-createindex.html) command is used to create an index. i.e., the following creates an index named `item_idx` on the table `item` using the `item_id` column:

`CREATE INDEX item_idx ON item (item_id);`

Its information can be checked now using `\di item_idx;`

An index requires additional disk space, so careful attention must be paid when creating an index. Also, `insert`/`delete`/`update` incurs more processing to maintain the index data, and the type of index has a different impact on performance. For instance, B-tree indexes require tree rebalancing, which is quite heavy in terms of computational cost.

## How to drop an index?

The [DROP INDEX](https://www.postgresql.org/docs/current/sql-dropindex.html) command is used to drop an existing index. Its basic syntax is `DROP INDEX index_name;`

Dropping an index will not affect the rows of the table, but careful attention must be paid since it will affect the performance of the database.

## Types of indexes

### Single-column index

The single-column index is utilized when a table represents mostly a single category of data, or queries span around only a single category in the table. Its syntax is `CREATE INDEX index_name ON table_name (column_name);`

### Multicolumn index

The multicolumn index is needed in cases where there are tables in a database that involve multiple categories of data. Its syntax is `CREATE INDEX index_name ON table_name (colum_1_name, column_2_name);`


### Partial index

The partial index is an index that applies only on the rows that complete the designated condition. Most of the time, the subset of the table is used in the queries. In this case, creating an index on the whole table is a waste of space and time consuming. We should consider creating an index on the subset of the table in this case. The rudimental reason to have a partial index is better performance by using the least amount of disk space as compared to creating an index on a whole table.

It can be created by specifying the `WHERE` condition during index creation as follows:

```
CREATE INDEX index_name ON table_name (column_name) WHERE (condition);
```

Example:
```
 CREATE INDEX item_partial_index ON item (item_id) WHERE (item_id < 106);
```

### Unique index

A unique index can be created on any column; it not only creates an index, but also enforces uniqueness of the column. This is the most important index in a database design as it ensures data integrity and provides performance enhancements. There are multiple ways to create a unique index: using the CREATE UNIQUE INDEX command, by creating a unique constraint on the table, or by creating a primary key. Example

`CREATE UNIQUE INDEX item_unique_idx ON item (item_id);`

Here is an example of an implicit creation of a unique index by creating a primary key on a table:

`CREATE TABLE item (item_id INTEGER PRIMARY KEY, item_name TEXT);`

Here is an example of an implicit creation of a unique index by defining unique constraints:

`ALTER TABLE item ADD CONSTRAINT primary_key UNIQUE (item_name);`

### Explicitly creating an index using the `CREATE INDEX` command

Only B-tree, GiST, and GIN indexes support the unique index. This index can be created using the following statement:

`CREATE UNIQUE INDEX idx_unique_id ON item (item_id);`

### Expression index

In some cases, there is a requirement to add an expression on one or more columns of the table while executing a query. For example, if it is required to search for a case-insensitive `item_name` in the `item` table, then the normal way of doing this is:

`SELECT * FROM item WHERE UPPER(item_name) LIKE 'COFFEE';`

The preceding query will scan each row or table and convert `item_name` to uppercase and compare it with `COFFEE`; this is really expensive. The following is the command to create an expression index on the item_name column:

`CREATE INDEX item_expression_index ON item (UPPER(item_name));`

An expression index is only used when the exact expression is used in a query as in the definition.

### Implicit index

An index that is created automatically by the database is called an implicit index. The primary key or unique constraint implicitly creates an index on that column.

### Concurrent index

Building an index locks the table from writing or inserting anything in the table. During the creation process, a process table can be read without an issue, but write operations block till the end of the index building process. Since an index
creation on a table is a very expensive operation, and on a sizeably huge table, it can take hours to build an index, it can cause difficulty in regards to performing any write operations. To solve this issue, PostgreSQL has the concurrent index, which is useful when it is needed to add indexes in a live database.

The concurrent index is slower than the normal index because it completes index building in two parts.

The syntax of a concurrent index is `CREATE INDEX CONCURRENTLY index_name ON table_name using btree(column_name);`
