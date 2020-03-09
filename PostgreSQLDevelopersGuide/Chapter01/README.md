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

### [INSERT](https://www.postgresql.org/docs/current/sql-insert.html)

The `INSERT` command can be used to insert some data into some table, i.e.:

`INSERT INTO table_name (id, name) values (31, 'super man')` will insert a new row in the table `table_name` with `id` equals to _31_ and `name` _super man_.

### [UPDATE](https://www.postgresql.org/docs/current/sql-update.html)

The `UPDATE` command will update some data into some table(s). This may include some conditional to update the table. i.e., the following will change the `name` value to _spider man_ for those rows which have `id` equals to _31_, in this case it is only one, and the value in the `name` column will change from _super man_ to _spider man_:

`UPDATE table_name SET name='spider man' WHERE id=31;`

### [DELETE](https://www.postgresql.org/docs/current/sql-delete.html)

In the case of the `DELETE` command, the user has to provide the delete criteria using the `WHERE` clause. i.e., the following clause will delete all rows which have _spider man_ as a value in the `name` column:

`DELETE FROM table_name WHERE name = 'spider man';`

The difference between `TRUNCATE`, `DELETE` and `DROP` is that the `DELETE` command is used to drop a row from a table, whereas the `DROP` command is used to drop a complete table. The `TRUNCATE` command is used to empty the whole table.

### PostgreSQL’s supported data types
