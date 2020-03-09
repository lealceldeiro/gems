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

### PostgreSQL’s supported [data types](https://www.postgresql.org/docs/current/datatype.html)

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


### PostgreSQL’s operators and usage
