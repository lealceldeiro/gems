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
