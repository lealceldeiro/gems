# Chapter 4: Triggers, Rules, and Views

##  Triggers

A trigger, in terms of databases, means a certain operation to be performed spontaneously for a specific database event. For example, this event can be `INSERT`, `UPDATE`, or `DELETE`. So, by defining a trigger, it is defined that certain operation(s) will be performed whenever that event occurs. An operation that a trigger refers to is called a trigger function.

The simple form of the [`CREATE TRIGGER`](https://www.postgresql.org/docs/12/sql-createtrigger.html) syntax is as follows:

```
CREATE TRIGGER trigger_name {BEFORE | AFTER | INSTEAD OF} {event [OR…]}
  ON table_name
  [FOR [EACH] {ROW | STATEMENT}]
  EXECUTE PROCEDURE trigger_function_name
```

PostgreSQL comes with two main types of triggers: row-level trigger and statementlevel trigger. These are specified with `FOR EACH ROW` (row-level triggers) and `FOR EACH STATEMENT` (statement-level triggers).

The triggers defined with `INSTEAD OF` are used for `INSERT`, `UPDATE`, or `DELETE` on the views. In the case of views, triggers fired `BEFORE` or `AFTER` an `INSERT`, `UPDATE`, or `DELETE` can only be defined at the statement level, whereas triggers that fire `INSTEAD OF` on `INSERT`, `UPDATE`, or `DELETE` will only be defined at the row level.

Variables provided to a trigger function:

* `NEW`: This variable is of the `RECORD` type and contains the new row to be stored for the `INSERT`/`UPDATE` command in row-level triggers.
* `OLD`: This variable is also of the `RECORD` type and stores the old row for the `DELETE`/`UPDATE` operation in row-level triggers.
* `TG_OP`: This will contain one of the strings that informs you for which operation the trigger is invoked; the value can be `INSERT`, `UPDATE`, `DELETE`, or `TRUNCATE`.
* `TG_TABLE_NAME`: This holds the name of the table for which the trigger is fired
`TG_WHEN`: This will contain the string with the value of `BEFORE`, `AFTER`, or `INSTEAD OF`, as per the trigger’s definition.

Example:
```
-- trigger function
CREATE OR REPLACE FUNCTION audit_func() RETURNS trigger AS $first_trigger$
  BEGIN
    INSERT INTO audit_table (id, insertion_time) VALUES (NEW.id, current_timestamp);
    RETURN NEW;
  END;
$first_trigger$ LANGUAGE plpgsql;

-- trigger
CREATE TRIGGER audit_trigger AFTER INSERT ON table FOR EACH ROW EXECUTE PROCEDURE audit_func();
```

## Views

A view is not a real table itself but can serve as a read-only view of the table you have associated with. Unlike tables, views do not exist on their own. They are helpful in the following ways:

* They are created on the table as a query to select all or selective columns, thus giving restricted or privileged access. Accessing actual data is not at all required.
* They are capable of joining multiple tables to represent data as a single table.
* They are not real, thus they only use storage space for definition and not for the actual data they collect.
* They always bring the updated data when accessed.

General syntax to [`CREATE` a `VIEW`](https://www.postgresql.org/docs/current/sql-createview.html):
```
CREATE [ OR REPLACE ] [ TEMP | TEMPORARY ] [ RECURSIVE ] VIEW name [ (column_name [, ...] ) ]
  [ WITH ( view_option_name [= view_option_value] [, ... ] ) ]
  AS query
  [ WITH [ CASCADED | LOCAL ] CHECK OPTION ]
```

Example:

`CREATE VIEW view_name AS SELECT * FROM table_name;`

To access the previously created view, it can be done like this:

`SELECT * FROM view_name`;

### Materialized views

Views can be materialized in a way that they can still be a logical representation of the data but when stored physically on the disk, at that moment they become **materialized views**.

Materialized views have proved their capacity to work when performance is required, that is, when there are more reads than writes. A materialized view data will be stored in a table that can be indexed quickly when joining and that has to be done as well when a refresh to the materialized views has to be performed.

Unlike regular views that bring the updated data, materialized views get populated depending on how they were created and
also it is needed a refresh mechanism to do this.

Example of [CREATE MATERIALIZED VIEW](https://www.postgresql.org/docs/current/sql-creatematerializedview.html):

`CREATE MATERIALIZED VIEW materialized_view_name AS SELECT * FROM table_name WITH NO DATA;`

Example of [REFRESH MATERIALIZED VIEW](https://www.postgresql.org/docs/current/sql-refreshmaterializedview.html)

`REFRESH MATERIALIZED VIEW materialized_view_name;`
