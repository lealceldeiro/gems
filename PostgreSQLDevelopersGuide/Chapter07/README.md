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
  [                                    +
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

```
SELECT current_setting('seq_page_cost'), current_setting('random_page_cost'), current_setting('cpu_tuple_cost'), current_setting('cpu_operator_cost'), current_setting('cpu_index_tuple_cost');
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
