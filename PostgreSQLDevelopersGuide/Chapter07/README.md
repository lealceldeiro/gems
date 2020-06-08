# Chapter 7: Query Optimization

## [`EXPLAIN`](https://www.postgresql.org/docs/current/sql-explain.html)

The `EXPLAIN` command is utilized to exhibit the execution plan of the query. It shows how tables will be scanned and the estimated cost of the query. `EXPLAIN` has the ability to show how PostgreSQL will genuinely execute the query internally.

There are three types of scans: sequential scans, index scans, and bitmap scans.

There are two costs involved in culling the scan: start up cost and execution cost. Both costs are estimated costs, predicated on unit of disk page fetch.

### `ANALYZE`

`EXPLAIN` with the `ANALYZE` option shows the actual runtime of the query.

### Pretty formats

Formatted output can be in TEXT, XML, JSON, or YAML. Here is an example of a formatted output in JSON:

`EXPLAIN (FORMAT JSON) SELECT * FROM table_name;`

```
            QUERY PLAN
----------------------------------------
  [                                     +
    {                                   +
      "Plan": {                         +
        "Node Type": "Seq Scan",        +
        "Relation Name": "table_name",  +
        "Alias": "history",             +
        "Startup Cost": 0.00,           +
        "Total Cost": 1934580.00,       +
        "Plan Rows": 100000000,         +
        "Plan Width": 46                +
      },                                +
      "Planning Time": 0.101            +
    }                                   +
  ]                                     +
(2 rows)
```

## Cost parameters

The costs in PostgreSQL are measured in arbitrary units determined by the planner’s cost parameters usually measured in units of disk page fetches. The cost computation is the estimated cost loosely associated with authentic real-world numbers.

The different type of cost parameters are:

Parameter             |   Default value   |
----------------------|--------------------
seq_page_cost         |   1.00            |
random_page_cost      |   4.00            |
cpu_tuple_cost        |   0.01            |
cpu_operator_cost     |   0.0025          |
cpu_index_tuple_cost  |   0.005           |

For example to check these parameters the following query can be ran:

```sql
SELECT current_setting('seq_page_cost'), current_setting('random_page_cost'), current_setting('cpu_tuple_cost'),
       current_setting('cpu_operator_cost'), current_setting('cpu_index_tuple_cost');
```

Parent nodes have the cumulative cost of all of its children.

## Sequential scans

The sequential scan scans the whole table sequentially to retrieve the required rows from the table. The planner selects the sequential scan when a query is retrieving large number of rows from the table and the number of appropriate indexes found.

Any scan can be enabled or disabled using the [`SET`](https://www.postgresql.org/docs/current/sql-set.html) command.

For example, the followinf command enables a sequential scan: `SET enable_seqscan = TRUE/FALSE;`

## Index scans

An index is a way to efficiently retrieve specific rows from database. The planner chooses an index scan if any index satisfies the WHERE condition. It is faster than the normal table scan because it does not traverse the whole set of column of rows. Normally, an index is created on tables with lesser number of columns. In index scans, PostgreSQL picks only one tuple’s pointer and accesses the tuple/row from the table.

An index based on all columns of table has no performance benefit.

### Index-only scans

If all the columns of a query are part of the index, then the planner selects index-only scans. In this case, the tuple on the page is visible, so tuples are picked from an index instead of a heap, which is a really big performance boost.

If an index becomes inefficient or blotted, then the index must be rebuilt using the `REINDEX` command.

## Bitmap scans

The bitmap scan fetches all the tuple-pointers from the disks and fetches the tuple using the bitmap data structure. A bitmap scan is useful only when small numbers of rows are needed.

## Common Table Expressions

To add readability and simplification to much larger and complex queries, PostgreSQL has a rich feature known as Common Table Expressions (CTE). Using the `WITH` keyword in query, there is a way to break down the query and attain efficiency along with readability.

What it does is that it can be used like an in-line view and even allows recursion to the SQL statements. Though for recursion, the `WITH RECURSIVE` keywords must be used, instead of simply using `WITH` for your queries.

## Joins

### Nested loop joins

A nested loop join is basically a nested FOR loop in which the relation on the right is scanned once for every row found in the relation on the left. Though this is easy to implement, it can be time consuming and useful when the finding qualifying rows operation is cheap.

### Merge joins

A merge join or a sort-merge join works on the principle that before the join starts, each relation is first sorted on the join attributes. Scanning is performed in parallel and matching rows are thus combined. Implementation can become complex with duplicate values, so if the left one has duplicate values, then the right table can be rescanned more than once.

### Hash joins

Unlike a merge join, a hash join doesn’t sort its input. Rather, it first creates a hash table from each row of the right table using its join attributes as hash keys and then scans the left table to find the corresponding matching rows.

### Hash semi and anti joins

A semi join can be exercised when the optimizer has to make sure that a key value exists on one side of the join. On the other hand, an anti join looks particularly for entries where the key value doesn’t exist.

Usually, these two types of joins are used when executing the `EXISTS` and `NOT EXISTS` expressions.

### Join ordering

When the number of joins increases, so does the complexity associated in controlling it in query tuning and the optimizer. Though the query optimizer can choose plans to execute these joins in several orders with identical results, yet the most inexpensive one will beutilized. However, performing these searches for best plans can be time consuming and error prone for decisions. The optimizer can be coerced to utilize the order which is consider the optimal one to join the tables, thus reducing the orchestrating time when performing a series of explicit join operations. In this way, other plans will not be considered. This can be a subsidiary, considering if the query-construction time is huge for a complex join or when the order selected by the optimizer is worthless.

## Hints

PostgreSQL does not have optimizer hints, but there is another way to provide allusions to the optimizer. We can enable or disable features according to our requirement; if we don’t want to utilize the sequential scan, then we can disable it and the planner will use the next scan for that query. We can get the benefit of hints by enabling and disabling the scans according to our need.

The following features can be enabled or disabled:

* enable_bitmapscan
* enable_hashjoin
* enable_indexscan
* enable_mergejoin
* enable_seqscan
* enable_tidscanenable_hashagg
* enable_indexonlyscan
* enable_material
* enable_nestloop
* enable_sort

## Configuration parameters to optimize queries

PostgreSQL has configuration parameters that can be configured permanently or session predicated. The `postgresql.conf` file is utilized to configure most of the configuration parameters.

Some of these parameters that actually have an impact in performance are:

* `work_mem`: The disk I/O is the dominant cost factor in queries; if case queries involve a large number of complex sorts, then it can increment the disk access. If the system has lots of recollection, then the database should perform the in-recollection sorting to reduce the disk read. The `work_mem` configuration parameter is utilized to determine when the sorting will be performed in a recollection or the disk sort will be utilized. If we have lots of recollection, then the `work_mem` parameter should be set to the optimal value so that every sort can be performed in-recollection. This parameter is per connection for each sort which makes it authentically hard to set its value. The default value is 4 MB. This can be seen using the following statement: `SHOW work_mem;`
* `maintenance_workmem`: The memory used for maintenance work can be configured by utilizing the `maintenance_workmem` parameter. The default value is 16 MB. Increasing the value can increment the performance of the maintenance jobs such as `CREATE INDEX`, `VACUUM`, and `REINDEX`. The following statement gives the value of `maintenance_work_mem`: `SHOW maintenance_work_mem;`
* `effective_cache_size`: This parameter is used to set the estimated memory available for the operating system cache. The following statement shows the resulting value of `effective_cache_size`: `SHOW effective_cache_size;`
* `checkpoint_segments`: This parameter is used to control the checkpoint. It designates a checkpoint after the checkpoint_segments log file is filled, which is customarily 16 MB in size. The default value is 3. Increasing the value reduces the disk I/O, which is definitely a performance boost, and this also increases the crash recovery time. The following statement shows the default value of `checkpoint_segments`: `SHOW checkpoint_segments;`
* `checkpoint_timeout`: This parameter is used as well to control the checkpoints, that is, a checkpoint occurs when `checkpoint_timeout` log seconds have passed. The default value is 5 minutes. The following statement shows the default value of `checkpoint_timeout`: `SHOW checkpoint_timeout;`
* `shared_buffers`: The shared_buffers parameter is used to limit the memory dedicated to caching of data in PostgreSQL. The default value is 128 MB. The following statement shows the default value of `shared_buffers`: `SHOW shared_buffers;`
