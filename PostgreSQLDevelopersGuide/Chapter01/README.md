# Chapter 1: Getting Started with PostgreSQL

## Installation

In a unix environment

* Do `sudo apt install postgresql` (see [reference](https://help.ubuntu.com/lts/serverguide/postgresql.html))
* To set the passoword for the user `postgresql` do (see [reference](https://docs.boundlessgeo.com/suite/1.1.1/dataadmin/pgGettingStarted/firstconnect.html))
  - `sudo -u postgres psql postgres`
  - `\password postgres`
  - Enter the passoword and confirm it
  - `\q`
* To accept connections do
  -  Open `/etc/postgresql/<version>/main/pg_hba.conf (Ubuntu) or /var/lib/pgsql/<version>/data/pg_hba.conf (Red Hat) in a text editor.
  - Modify the line that describes local socket connections and add the required information to accept local (or remote if needed) connections. Like this
   ```
    local    all    postgres    md5
    host     all    postgres    10.0.0.0/0    md5    // where each represents host, database, user, ip address, and authentication method
   ```

## Writing queries using `psql`

Login into the default database using `psql -U postgres -W` (**`-U`** indicates the username and **`-W`** will prompt for password.

Create a database using `CREATE DATABASE <database_name>;`.

Connect to the database using `\c <database_name>`.

After successfully connecting to the DB it possible to perform some Data Definition Language (DDL) and Data Manipulation Language (DML) operations.

Inside the multiple databases that can exist in PostgreSQL, there can be multiple extensions and schemas. Inside each schema, there can be database objects such as tables, views, sequences, procedures, and functions.

To create a schema the following statement can be used: `CREATE SCHEMA <schema_name>;`.

### [`CREATE TABLE`](https://www.postgresql.org/docs/current/tutorial-table.html)

`CREATE TABLE table_name(id INTEGER NOT NULL, name TEXT NOT NULL, CONSTRAINT "PRIM_KEY" PRIMARY KEY (id));` creates a table with name `table_name` with `id` integer and `name` text and the `id` is the primary key.

### [`CREATE SEQUENCE`](https://www.postgresql.org/docs/current/sql-createsequence.html)

`CREATE SEQUENCE sequence_name;` creates a sequence with name `sequence_name`.

This sequence can be used now (i.e.) in a table creation as follow:

`CREATE TABLE table_name (table_id INTEGER NOT NULL DEFAULT nextval('sequence_name'));`

### [`ALTER TABLE`](https://www.postgresql.org/docs/current/sql-altertable.html)

Using the `ALTER TABLE` command, we can add, remove, or rename table columns. i.e.:

`ALTER TABLE table_name ADD COLUMN phone_no INTEGER;` adds the `phone_no` column to a previously created table `table_name`.

At this point if the table is described using `\d table_name` the following information is show:
```
Column          | Type                   | Nullable
----------------+------------------------+-----------
id              | integer                | not null
name            | text                   | not null
phone_no        | integer                |
Indexes:
    "prim_key" PRIMARY KEY, btree (id)
```

`ALTER TABLE table_name DROP COLUMN phone_no;` removes the `phone_no` column from a previously created table `table_name`.

At this point if the table is described using `\d table_name` the following information is show:
```
Column          | Type                   | Nullable
----------------+------------------------+-----------
id              | integer                | not null
name            | text                   | not null
Indexes:
    "prim_key" PRIMARY KEY, btree (id)
```

### [`TRUNCATE`](https://www.postgresql.org/docs/current/sql-truncate.html)

The `TRUNCATE` command is used to remove **all rows** from a table **without** providing any **criteria**. i.e.:

`TRUNCATE TABLE table_name;`

### [`INSERT`](https://www.postgresql.org/docs/current/sql-insert.html)

The `INSERT` command can be used to insert some data into some table, i.e.:

`INSERT INTO table_name (id, name) values (31, 'super man')` will insert a new row in the table `table_name` with `id` equals to _31_ and `name` _super man_.

### [`UPDATE`](https://www.postgresql.org/docs/current/sql-update.html)

The `UPDATE` command will update some data into some table(s). This may include some conditional to update the table. i.e., the following will change the `name` value to _spider man_ for those rows which have `id` equals to _31_, in this case it is only one, and the value in the `name` column will change from _super man_ to _spider man_:

`UPDATE table_name SET name='spider man' WHERE id=31;`

### [`DELETE`](https://www.postgresql.org/docs/current/sql-delete.html)

In the case of the `DELETE` command, the user has to provide the delete criteria using the `WHERE` clause. i.e., the following clause will delete all rows which have _spider man_ as a value in the `name` column:

`DELETE FROM table_name WHERE name = 'spider man';`

The difference between `TRUNCATE`, `DELETE` and `DROP` is that the `DELETE` command is used to drop a row from a table, whereas the `DROP` command is used to drop a complete table. The `TRUNCATE` command is used to empty the whole table.

## Supported [data types](https://www.postgresql.org/docs/current/datatype.html)

PostgreSQL has a rich set of native data types available to users. Users can add new types to PostgreSQL using the [`CREATE TYPE`](https://www.postgresql.org/docs/current/sql-createtype.html) command.


| Name        					                  | Aliases  				      | Description                                       |
| --------------------------------------- | --------------------- | ------------------------------------------------- |
| bigint      					                  | int8     				      | signed eight-byte integer                         |
| bigserial   					                  | serial8  				      | autoincrementing eight-byte integer               |
| bit [ (_`n`_) ] 				                |      					        | fixed-length bit string                           |
| bit varying [ (_`n`_) ] 		            | varbit [ (_`n`_) ] 	  | variable-length bit string		                    |
| boolean						                      | bool					        | logical Boolean (true/false)		                  |
| box							                        | 						          | rectangular box on a plane		                    |
| bytea							                      | 						          | binary data (“byte array”)		                    |
| character [ (_`n`_) ]			              | char [ (_`n`_) ]		  | fixed-length character string		                  |
| character varying [ (_`n`_) ]	          | varchar [ (_`n`_) ]	  | variable-length character string	                |
| cidr							                      | 						          | IPv4 or IPv6 network address		                  |
| circle						                      | 						          | circle on a plane					                        |
| date							                      | 						          | calendar date (year, month, day)	                |
| double precision				                | float8				        | double precision floating-point number (8 bytes)  |
| inet							                      |						            | IPv4 or IPv6 host address			                    |
| integer						                      | `int`, `int4`			    | signed four-byte integer			                    |
| interval [ _`fields`_ ] [ (_`p`_) ]     |					              | time span							                            |
| json							                      |						            | textual JSON data					                        |
| jsonb							                      | 						          | binary JSON data, decomposed		                  |
| line							                      | 						          | infinite line on a plane			                    |
| lseg							                      | 						          | line segment on a plane			                      |
| macaddr						                      |						            | MAC (Media Access Control) address                |
| macaddr8						                    | 						          | MAC (Media Access Control) address (EUI-64 format)|
| money							                      | 						          | currency amount					                          |
| numeric [ (p, s) ]                      | decimal [ (p, s) ]    | exact numeric of selectable precision             |
| path                                    |                       | geometric path on a plane                         |
| pg_lsn                                  |                       | PostgreSQL Log Sequence Number                    |
| point                                   |                       | geometric point on a plane                        |
| polygon                                 |                       | closed geometric path on a plane                  |
| real                                    | float4                | single precision floating-point number (4 bytes)  |
| smallint                                | int2                  | signed two-byte integer                           |
| smallserial                             | serial2               | autoincrementing two-byte integer                 |
| serial                                  | serial4               | autoincrementing four-byte integer                | 
| text                                    |                       | variable-length character string                  |
| time [ (p) ] [ without time zone ]      |                       | time of day (no time zone)                        |
| time [ (p) ] with time zone             | timetz                | time of day, including time zone                  |
| timestamp [ (p) ] [ without time zone ] |                       | date and time (no time zone)                      |
| timestamp [ (p) ] with time zone        | timestamptz           | date and time, including time zone                |
| tsquery                                 |                       |	text search query                                 |
| tsvector                                |                       | text search document                              |
| txid_snapshot                           |                       | user-level transaction ID snapshot                |
| uuid                                    |                       | universally unique identifier                     |
| xml                                     |                       | XML data                                          |

## Operators

### [Logical Operators](https://www.postgresql.org/docs/current/functions-logical.html)

The usual logical operators are available: `AND`, `OR`, `NOT`.

SQL uses a three-valued logic system with `true`, `false`, and `null`, which represents “unknown”. Observe the following truth tables:

| _a_   | _b_   | _a_ `AND` _b_ | _a_ `OR` _b_ |
| ----- | ----- | ------------- | ------------ |
| TRUE  | TRUE  |     TRUE      |    TRUE      |
| TRUE  | FALSE |     FALSE     |    TRUE      |
| TRUE  | NULL  |     NULL      |    TRUE      |
| FALSE | FALSE |     FALSE     |    FALSE     |
| FALSE | NULL  |     FALSE     |    NULL      |
| NULL  | NULL  |     NULL      |    NULL      |

| _a_   | `NOT` _a_ |
| ----- | --------- |
| TRUE  |   FALSE   |
| FALSE |   TRUE    |
| NULL  |   NULL    |

The operators `AND` and `OR` are commutative, that is, you can switch the left and right operand without affecting the result.

### [Comparison operators](https://www.postgresql.org/docs/current/functions-comparison.html)

| Operator   | Description                |
| ---------- | -------------------------- |
|     <      |  less than                 |
|     >      |  greater than              |
|     <=     |  less than or equal to     |
|     >=     |  greater than or equal to  |
|     =      |  equal                     |
|  <> or !=  |  not equal                 |

The `!=` operator is converted to `<>` in the parser stage. It is not possible to implement `!=` and `<>` operators that do different things.

### [Mathematical operators](https://www.postgresql.org/docs/current/functions-math.html)

| Operator | Description                                        | Example   | Result  |
| -------- | -------------------------------------------------- | --------- | ------- |
|     +    |  addition                                          | 	2 + 3   |   5     |
|     -    |  subtraction                                       |   2 - 3   |   -1    |
|     *    |  multiplication                                    |   2 * 3   |   6     |
|     /    |  division (integer division truncates the result)  |   4 / 2   |   2     |
|     %    |  modulo (remainder)                                |   5 % 4   |   1     |
|     ^    |  exponentiation (associates left to right)         | 2.0 ^ 3.0 |   8     |
|    \|/   |  square root                                       | \|/ 25.0  |   5     |
|  \|\|/   |  cube  root                                        |\|\|/ 27.0 |   3     |
|    !     |  factorial                                         | 5 !       |   120   |
|    !!    |  factorial (prefix operator)                       | !! 5      |   120   |
|    @     |  absolute value                                    | @ -5.0    |   5     |
|    &     |  bitwise AND                                       | 91 & 15   |   11    |
|    \|    |  bitwise OR                                        | 32 \| 3    |   35    |
|    #     |  bitwise XOR                                       | 17 # 5    |   20    |
|    ~     |  bitwise NOT                                       | ~1        |   -2    |
|    <<    |  bitwise shift left                                | 1 << 4    |   16    |
|    >>    |  bitwise shift right                               | 8 >> 2    |   2     |

The bitwise operators work only on integral data types, whereas the others are available for all numeric data types. The bitwise operators [are also available](https://www.postgresql.org/docs/current/functions-bitstring.html#FUNCTIONS-BIT-STRING-OP-TABLE) for the bit string types `bit` and `bit varying`.

## Constraints

### Unique constraints

A **unique** constraint is a constraint that at the time of an insertion operation makes sure that data present in a column (or a group of columns) is unique with regard to all rows already present in the table. An example of creation of tables using unique constraints is:

`CREATE TABLE table_name (id INTEGER UNIQUE, name TEXT);`

or

`CREATE TABLE table_name (id INTEGER, name TEXT, UNIQUE(id));`

and for more than one column, it can be done like this:

`CREATE TABLE table_name (id INTEGER, name TEXT, UNIQUE(id, name));`

### Not-null constraints

A **not-null** constraint makes sure that a column must have some values and a value is not left as `null`. An example of creation of tables using unique constraints is:

`CREATE TABLE table_name (id INTEGER NOT NULL, name TEXT);`

and for more than one column, it can be done in the same way:

`CREATE TABLE table_name (id INTEGER NOT NULL, name TEXT NOT NULL);`

### Exclusion constraints

An **exclusion** constraint is used when comparing two rows on nominative columns or expressions using the nominative operators. The result of the comparison will be `false` or `null`.

Consider the following example in which the conflicting tuple is given the `AND` operation together:

`CREATE TABLE movies (title TEXT, copies INTEGER);`

Then, using the `ALTER TABLE` command, it is created an exclusion constraint and the conditions for a conflicting tuple are `AND` together.

`ALTER TABLE movies ADD EXCLUDE (title WITH=, copies WITH=);`

Now in order for two records to conflict, there could something like:

`record1.title = record2.title AND record1.copies = record2.copies`

### Primary key constraints

A **primary key** constraints is a combination of **not-null** constraints and **unique** constraints, which means that for a column to fulfill the primary key constraints limitation, it should be unique as well as not null. An example of creation of tables using primary key constraints is:

`CREATE TABLE table_name (id INTEGER PRIMARY KEY, name TEXT);`

### Foreign key constraints

**Foreign key** constraints state that the value in a column must be the same as the value present in another table’s row. This is for the sake of maintaining the referential integrity between two interlinked tables. Taking into account the following example, two tables are created, and the column (`id_a`) of one table (`table_A`) is used in the second table (`table_B`) as a foreign key constraint:

```sql
CREATE TABLE table_A (id_a INTEGER PRIMARY KEY, name TEXT);
CREATE TABLE table_B (id_b INTEGER PRIMARY KEY, amount FLOAT8, table_A_id INTEGER REFERENCES table_A (id_a));
```

In a table more than one foreign key can be present.

### Check constraints

A check constraint lets you define a condition that a column must fulfill a Boolean expression. i.e.: in the following table it is checked a constraints on `amount` to make sure that it must be greater than `0`.

`CREATE TABLE table_name (id INTEGER PRIMARY KEY, amount NUMERIC CHECK (amount > 0));`

Also, a more user-friendly name can be given to constraints, i.e.:

`CREATE TABLE table_name (id INTEGER PRIMARY KEY, amount NUMERIC CONSTRAINT positive_amount CHECK (amount > 0));`

## Privileges

Multiple privileges are present for every object that is created. By default, the owner (or a superuser) of an object has all the privileges on it. In PostgreSQL, the following types of privileges are present:

* `SELECT`
* `INSERT`
* `UPDATE`
* `DELETE`
* `TRUNCATE`
* `REFERENCES`
* `TRIGGER`
* `CREATE`
* `CONNECT`
* `TEMPORARY`
* `EXECUTE`
* `USAGE`

There are different privileges associated with different objects. For instance, the `EXECUTE` privilege is associated with procedure. The `GRANT` command is used to grant any privilege to any user. Similarly, to take back privileges, the `REVOKE` command is used.

