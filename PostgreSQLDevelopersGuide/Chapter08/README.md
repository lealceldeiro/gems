# Chapter 8. Dealing with Large Objects

Objects that require huge storage size and can’t be supported by simple available data types such as `int` and `varchar` are usually referred to as Large Objects (LOs) or Binary Large OBjects (BLOBs).

## Why large objects?

Based on the structure or type of data, object types can be categorized as:

* Simple: related to data that can be organized in simple tables and data types
* Complex: deals with requirements such as that of user-defined data types
* Semi-structured: forms of data types such as XML and JSON fit into this category
* Unstructured: other formats such as images, audios, and videos cannot be stored in the previous mentioned data types because they cannot be broken into smaller logical structures for interpretation by standard means. So they need to be stored as unstructured data and require a different mechanism to handle them.

## PostgreSQL large objects

PostgreSQL provides two ways to store large objects:

* Implementation of the `BYTEA` data type: It is similar to `VARCHAR` and text character strings, yet it has a few distinctive features as well. It can store raw or unstructured data, but character strings do not. It also allows storing of null values. `VARCHAR` does not permit storing zero octets, other octets, or sequences of octet values that are not valid as per database character set encoding. While using `BYTEA`, actual raw bytes can be manipulated, but in the case of character strings, processing is dependent on locale setting.
* Implementation of large object storage

`BYTEA` when compared with large object storage comes with a big difference of storage size; each `BYTEA` entry permits storage of 1 GB whereas large objects allow up to 4 TB. The large object feature provides functions that helps in manipulating external data in a much easier way that could be quite complex when doing the same for `BYTEA`.

### Large objects

* Large objects, unlike `BYTEA`, are not a data type but an entry in a system table.
* All large objects are stored in the `pg_largeobject` system table.
* Each large object also has a corresponding entry in the `pg_largeobject_metadata` system table.
* Large objects are broken up into chunks of default size and further stored as rows in the database.
* These chunks in rows are B-tree indexed; hence, this ensures fast searches during read/write operations.
* From PostgreSQL 9.3 onwards, the maximum size of a large object in a table can be 4 TB.
* Large objects are not stored in user tables; rather, a value of the Object Identifier (OID) type is stored. This OID value is used to access the large object by referencing the OID value that points to a large object present on the `pg_largeobject` system table.
* PostgreSQL provides the read/write Application Program Interface (API) that offers client- and server-side functions. Using this API, operations such as create, modify, and delete can performed on large objects. OIDs are used in this function as a reference to access large objects, for example, to transfer the contents of any file to the database or to extract an object from the database into a file.
* From PostgreSQL 9.0 onwards, large objects now have an associated owner and a set of access permissions. Retrieving data using these functions gives us the same binary data added previously. Examples of the functions are `lo_create()`, `lo_unlink()`, `lo_import()`, and `lo_export()`.
* PostgreSQL provides the `ALTER LARGE TABLE` feature to change the definition of a large object. Its only functionality is to assign a new owner.

Functions for large objects _must_ be called in a transaction block, so when _autocommit_ is off, the `BEGIN` command must be issued explicitly.

For example, to see the list of functions available to access large objects, the following query can be executed:

```sql
SELECT
  n.nspname as "Schema",
  p.proname as "Name",
  pg_catalog.pg_get_function_result(p.oid) as "Result data type",
  pg_catalog.pg_get_function_arguments(p.oid) as "Argument data types"
FROM
  pg_catalog.pg_proc p
    LEFT JOIN
      pg_catalog.pg_namespace n ON n.oid = p.pronamespace
WHERE
  p.proname ~ '^(lo_.*)$'
    AND
      pg_catalog.pg_function_is_visible(p.oid)
ORDER BY 1, 2, 4;
```

That will give somethign like:

```
Schema      | Name          | Result data type  | Argument data types
------------+---------------+-------------------+-----------------
pg_catalog  | lo_close      | integer           | integer
pg_catalog  | lo_create     | oid               | integer
pg_catalog  | lo_create     | oid               | oid
pg_catalog  | lo_export     | integer           | oid, text
pg_catalog  | lo_from_bytea | oid               | oid, bytea
pg_catalog  | lo_get        | bytea             | oid
pg_catalog  | lo_get        | bytea             | oid, bigint, integer
pg_catalog  | lo_import     | oid               | text
pg_catalog  | lo_import     | oid               | text, oid
pg_catalog  | lo_lseek      | integer           | integer, integer, integer
pg_catalog  | lo_lseek64    | bigint            | integer, bigint, integer
pg_catalog  | lo_open       | integer           | oid, integer
pg_catalog  | lo_put        | void              | oid, bigint, bytea
pg_catalog  | lo_tell       | integer           | integer
pg_catalog  | lo_tell64     | bigint            | integer
pg_catalog  | lo_truncate   | integer           | integer, integer
pg_catalog  | lo_truncate64 | integer           | integer, bigint
pg_catalog  | lo_unlink     | integer           | oid
```

More details about manipulating large objects through the libpq client interface library can be see in the [official documentation](https://www.postgresql.org/docs/current/lo-interfaces.html)
