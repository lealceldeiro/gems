# Chapter 1: Getting Started with PostgreSQL

## Installation

In a unix environment

* Do `sudo apt install postgresql` (see [Documentation](https://help.ubuntu.com/lts/serverguide/postgresql.html))
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

## Creating tables

To create a table for example, with `id` and `name` and by specifying that the `id` is the primary key it can be done like this:

```
CREATE TABLE table_name(id INTEGER NOT NULL, name TEXT NOT NULL, CONSTRAINT "PRIM_KEY" PRIMARY KEY (id));
```
