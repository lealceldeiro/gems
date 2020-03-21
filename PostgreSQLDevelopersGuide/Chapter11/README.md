# Chapter 11: Foreign Data Wrapper

A foreign data wrapper is a template to write the module to access foreign data. This is based on the SQL/MED (SQL/Management of External Data) standard. The SQL/MED was added to the SQL standard in 2003. This is a standard to access remote objects from SQL.

The complete list of data wrapper can be fount at [Foreign data wrappers](https://wiki.postgresql.org/wiki/Foreign_data_wrappers).

## Creating foreign data wrappers

PostgreSQL uses the following [syntax](https://www.postgresql.org/docs/current/sql-createforeigndatawrapper.html) for creating foreign data wrappers:

```
CREATE FOREIGN DATA WRAPPER name
  [ HANDLER handler_function | NO HANDLER ]
  [ VALIDATOR validator_function | NO VALIDATOR ]
  [ OPTIONS ( option 'value' [, ... ] ) ]
```

where

* `name`: This is the name of the foreign data wrapper to be created.
* `handler_function`: It is called to retrieve the execution function. The return type of the function must be `fdw_handler`.
* `validator_function`: The foreign data wrapper has functionality where we can provide a generic option while creating the server, user mapping, and foreign data wrapper. The validator_function is called to validate these generic options. If there is no validator function specified, then the option will not be checked.

A foreign data wrapper without `handler_function` cannot be accessed and only a superuser can create foreign data wrappers.

## The basic components of foreign data wrappers

### A C file

There is a need for a C programming source code file that will contain the implementation of handler_function and validator_function. The C programming source should define the handler and validator function as follows:

```
extern Datum my_fdw_handler (PG_FUNCTION_ARGS);
extern Datum my_fdw_validator (PG_FUNCTION_ARGS);
PG_FUNCTION_INFO_V1 (my_fdw_handler);
PG_FUNCTION_INFO_V1 (my_fdw_validator);
```

The `handler_function` function, which is `my_fdw_handler` in the given example, should return the `FdwRoutine` structure pointer. The `FdwRoutine` structure contains the pointer of implementation call-back functions. The foreign data wrapper calls the functions using these `FdwRoutine` function pointers. The foreign data wrapper machinery will not call any function whose pointer is `null`.

### A Makefile to compile the foreign data wrapper

A _Makefile_ to compile the source code in the C file to generate a library is also needed.

For example, to compile the previously shown C file here is an example of _Makefile_:

```
# Makefile
MODULE_big = my_fdw
OBJS = my_fdw.o
EXTENSION = my_fdw
DATA = my_fdw--1.0.sql
REGRESS = my_fdw
MY_CONFIG = my_config
subdir = contrib/my_fdw
top_builddir = ../..
include $(top_builddir)/src/Makefile.global
include $(top_srcdir)/contrib/contrib-global.mk
```

where

* `MODULE_big`: This is the name of the module
* `OBJS`: This is the C source code file for compilation
* `DATA`: This is the name of SQL file.
* `REGRESS`: This is the regression file name

A SQL file is needed to map the C handler and validate function to the SQL function. It not only maps the C function with the SQL function, but also creates a foreign data wrapper using the `CREATE FOREIGN DATA WRAPPER` command in the following manner:

```
-- File name=dummy_fdw--1.0.sql

CREATE FUNCTION my_fdw_handler() RETURNS fdw_handler AS 'MODULE_PATHNAME' LANGUAGE C STRICT;
CREATE FUNCTION my_fdw_validator(text[], oid) RETURNS void AS 'MODULE_PATHNAME' LANGUAGE C STRICT;
CREATE FOREIGN DATA WRAPPER my_fdw HANDLER my_fdw_handler VALIDATOR my_fdw_validator;
```

### A control file to manage version and module path

A module or extension control file contains the wrapper version and module library path as shown:

```
#my_fdw.control
comment = 'Foreign data wrapper for querying a server'
default_version = '1.0'
module_pathname = '$
relocatable = true
```

Let’s consider the various parameters mentioned in the preceding snippet:

* `comment`: This gives the comments for the foreign data wrapper
* `default_version`: This gives the version number of extension
* `Module_pathname`: This gives the foreign data wrapper library path

## Loading foreign data wrappers

Foreign data wrappers are extensions and can be loaded using [`CREATE EXTENSION`](https://www.postgresql.org/docs/current/sql-createextension.html). The syntax for loading foreign data wrappers is as follows:

`CREATE EXTENION my_fdw;`

### Creating a server

After loading the extension, it is needed to create a foreign server that typically consists of connection information. Normally, the connection information consists of a remote machine hostname or IP address and target system port number. The user information may be specified in user mapping.

```
CREATE SERVER server_name
  [ TYPE 'server_type' ] [ VERSION 'server_version' ]
  FOREIGN DATA WRAPPER fdw_name
  [ OPTIONS ( option 'value' [, ... ] ) ]
```

Let’s consider the various parameters mentioned in the preceding [syntax](https://www.postgresql.org/docs/current/sql-createserver.html):

* `server_name`: This is the name of the server. This will be referred while creating the foreign tables.
* `server_type`: This is the type of the server and is optional.
* `server_version`: This is the server version information and is optional.
* `fdw_name`: This is the name of the foreign data wrapper.
* `OPTIONS`: This is the optional server specific information, normally contains the connection information. These options are validated in dummy_fdw validator.

The following statement creates a server named `my_server` of the `my_fdw` foreign data wrapper:

`CREATE SERVER my_server FOREIGN DATA WRAPPER my_fdw;`

### Creating a user mapping

The connection information consists of two parts; one is the target address, for example, host name, IP address ,and port; and the second part is the user information. The `CREATE SERVER` statement covers the target address part and `CREATE USER MAPPING` covers the user information part. The `CREATE USER MAPPING` statement maps the PostgreSQL user to the foreign server user. The syntax for [`CREATE USER MAPPING`](https://www.postgresql.org/docs/current/sql-createusermapping.html) is as follows:

```
CREATE USER MAPPING FOR
  { user_name | USER | CURRENT_USER | PUBLIC }
  SERVER server_name
  [ OPTIONS ( option 'value' [ , ... ] ) ]
```

Here,

* `user_name`: This is the name of the existing PostgreSQL user that needs to be mapped. The `CURRENT_USER` and `USER` parameter means the current logged in user, and `PUBLIC` is used when no user specific mapping is required.
* `server_name`: This is the name of the server for which user mapping is required.
* `OPTIONS`: These are the foreign data wrapper dependent options. Normally it contains the remote or foreign server username and password.

For example:

```
CREATE USER MAPPING FOR postgres SERVER my_server OPTIONS(username 'foo', password 'bar');
```

### Creating a foreign table

After creating the server and user mapping, the next step is to create a foreign table. The [syntax for creating a foreign table](https://www.postgresql.org/docs/current/sql-createforeigntable.html) is as follows:

```
CREATE FOREIGN TABLE [ IF NOT EXISTS ] table_name
  ( [column_namedata_type
  [ OPTIONS ( option 'value' [, ... ] ) ]
  [ COLLATE collation ]
  [ column_constraint [ ... ] ] [, ... ] ] )
  SERVER server_name [ OPTIONS ( option 'value' [, ... ] ) ]
  WHERE column_constraint is:
  [ CONSTRAINT constraint_name ]
  { NOT NULL | NULL | DEFAULT default_expr }
```

Let’s consider the various parameters mentioned in the preceding syntax:

* `table_name`: This is the name of the table.
* `column_name`: This is the name of the column.
* `data_type`: This gives the data type.
* `DEFAULT` default_expr: This is the the `DEFAULT` clause.
* `server_name`: This is the name of the foreign server.
* `OPTIONS`: This is the foreign data wrapper specific table options. It normally contains the remote table name.

Here is a simple example to create a user mapping for the `postgres` user:

```
CREATE FOREIGN TABLE my_foreign_table (id INTEGER, name TEXT) SERVER my_server OPTIONS(table_name 'my_remote_table');
```

## Using foreign data wrappers

After creating the foreign table, we can perform DML on the table just like a normal table like the following statement:

`SELECT * FROM my_foreign_table;`

### Working with `postgres_fdw`

PostgreSQL provides a template to create OUR own foreign data wrapper. But there are only two officially supported foreign data wrappers: `postgres_fdw` and `file_fdw`. The `postgres_fdw` is a foreign data wrapper that is used to retrieve and manipulate the remote PostgreSQL’s server. The `postgres_fdw` data wrapper can be used by performing the following steps:

1. Load the extension using `CREATE EXTENSION`:

`CREATE EXTENSION postgres_fdw;`

2. Create the server using `CREATE SERVER`:

```
CREATE SERVER postgres_server FOREIGN DATA WRAPPER postgres_fdw OPTIONS (host, '127.0.0.1', port '5432', dbname 'postgres');
```

3. Create user mapping using `CREATE USER MAPPING`:

`CREATE USER MAPPING FOR PUBLIC SERVER postgres_server;`

4. Create a foreign table using `CREATE FOREIGN TABLE`:

`CREATE FOREIGN TABLE dummy_table (foo INTEGER, bar TEXT) SERVER postgres_server OPTIONS (table_name 'remote_dummy_table');`

5. Insert data into the foreign table:

`INSERT INTO dummy_table VALUES (1, 'foo');`

6. Select data from the foreign data wrapper:

`SELECT * FROM dummy_table;`

### Working with `file_fdw`

This is used to access the files in the server file system and it can be used by performing the following steps:

1. Load extension using `CREATE EXTENSION`:

`CREATE EXTENSION file_fdw;`

2. Create the server using `CREATE SERVER`:

`CREATE SERVER file_svr FOREIGN DATA WRAPPER file_fdw;`

3. Create a foreign table using `CREATE FOREIGN TABLE`:

```
CREATE FOREIGN TABLE logfile (log_id INTEGER, log_detail TEXT, log_date date) SERVER file_svr OPTIONS (filename, 'log.txt', delimiter ',');
```

The `log.txt` file contains one record per line and every field is separated by a comma; in other words, log.txt is a Comma Separated File (CSF).
