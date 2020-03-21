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
