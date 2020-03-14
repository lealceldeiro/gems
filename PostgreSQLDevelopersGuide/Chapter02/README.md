# Chapter 2: The Procedural Language

Simply put, Procedural Language/PostgreSQL (PL/pgSQL) are blocks of code that are stored in the DBMS. PL/pgSQL is to PostgreSQL what PL/SQL is to Oracle (or what Transact/SQL is to Microsoft SQL Server).

Procedural languages can be used to write functions, and in PL/pgSQL, a function is a labeled sequence of statements written inside an SQL expression. By doing so, actually, the server is extended. These server extensions are also known as stored procedures.

## Structure of PL/pgSQL

PL/pgSQL is a block-structured and case-insensitive language which comprises statements inside the same set of the `DECLARE/BEGIN` and `END` statements:

```
DECLARE
  declarations
BEGIN
  statements
END;
```

To [create a function](https://www.postgresql.org/docs/12/sql-createfunction.html) the following syntax is used:

`CREATE OR REPLACE FUNCTION function_name (arguments) RETURNS type AS`

Example: The `getRecords()` function in the following example will simply return the total number
of records present in the `movies`.

```
CREATE OR REPLACE FUNCTION getRecords()
RETURNS INTEGER AS $$
DECLARE
  total INTEGER;
BEGIN
  SELECT COUNT(*) INTO total FROM movies;
  RETURN total;
END;
$$ LANGUAGE plpgsql;
```

### Comments in PL/pgSQL

Single-line comments are declared by starting with two dashes and with no endcharacter as follow:

`-- single line comment style`

And multiline comments, are declared as follow:

```
/*
multi line
comment
style
*/
```

Example function using both comment styles:

```
CREATE OR REPLACE FUNCTION concat (text, text)
--
--This function will concatenate the two strings.
--
/* Pipe characters are used to
 * concatenate
 * the strings
 */
RETURNS text AS $$
BEGIN
  RETURN $1 || ' ' || $2;
END;
$$ LANGUAGE plpgsql;
```

### Declaring variables in PL/pgSQL

Each variable is used in the lifetime of a block and must be declared within the `DECLARE` block starting with the `DECLARE` keyword as show in the previouse `getRecords` example function.

The general syntax is as follow:

```
name [ CONSTANT ] type [ COLLATE collation_name ] [ NOT NULL ] [ { DEFAULT | := } expression
```

### Declaring function parameters
