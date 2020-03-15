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

## Comments in PL/pgSQL

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

## Declaring variables in PL/pgSQL

Each variable is used in the lifetime of a block and must be declared within the `DECLARE` block starting with the `DECLARE` keyword as show in the previouse `getRecords` example function.

The general syntax is as follow:

```
name [ CONSTANT ] type [ COLLATE collation_name ] [ NOT NULL ] [ { DEFAULT | := } expression
```

## Declaring function parameters

Functions can accept and return values called function parameters or arguments. These parameters should be declared before usage.

Parameters thus passed to functions are labeled with the numeric identifiers `$1` and `$2`. We can also use an alias for a parameter name; both can then be later used to reference the parameter value.

Example 1:
```
CREATE OR REPLACE FUNCTION alias_explain(int)
RETURNS integer AS $$
DECLARE
  total ALIAS FOR $1;
BEGIN
  RETURN total*10;
END;
$$ LANGUAGE plpgsql;
```

Example 2:
```
CREATE OR REPLACE FUNCTION alias_explain(total int)
RETURNS integer AS $$
BEGIN
  RETURN total * 10;
END;
$$ LANGUAGE plpgsql;
```

The parameter types are `IN`, `OUT`, and `INOUT`. If not mentioned explicitly, function parameters are `IN` by default.

Example: 
```
CREATE OR REPLACE FUNCTION func_param(a int, IN b int, OUT plus int, OUT sub int) AS $$
BEGIN
  plus := a + b;
  sub := a - b;
END;
$$ LANGUAGE plpgsql;
```

## Declaring the `%TYPE` attribute

The `%TYPE` attribute is helpful to store values of a database object, usually a table column. This means declaring a variable with this attribute will store the value of the same data type it referenced. This is even more helpful if in future your column’s data type gets changed. It’s declared in the following manner:

`variable_name table_name.column_name%TYPE`

## Declaring the row-type and record type variables

A row-type variable declares a row with the structure of an existing user-defined table or view using the `table_name%ROWTYPE` notation; otherwise, it can be declared by giving a composite type’s name. The fields of the row can be accessed with the dot notation, for example, rowvariable.field.

## Statements and Expressions

### The assignment statement

Assignment means assigning a value to a variable. Its syntax is as follows: `target := expression;`

Here, `target` can be anything; it can be a variable, a column, a function parameter, or a row but not a constant.

Examples:

* **`SELECT`** `COUNT(*)` **`INTO`** `TOTAL FROM table_name;`
* **`plus := a + b;`**

### The call/return function

All PostgreSQL functions return a value, and to call a function is to simply run a `SELECT` statement query or an assignment statement. Examples:

* `SELECT function_identifier(arguments);`
* `variable_identifier := function_identifier(arguments);`
* `SELECT AVG(amount) FROM history;`

### The `RETURN` expression

As the function ends, the evaluated value of expression will be returned as per the specified return type in the `CREATE FUNCTION` command. TThe syntax for the RETURN command is as follows:
```
CREATE OR REPLACE FUNCTION function_identifier(arguments)
RETURNS TYPE AS
DECLARE
   -- declaration;
BEGIN
  -- statement;
RETURN { variable_name | value }
END;
LANGUAGE 'plpgsql';
```

### Exception handling statements

The RAISE statements can be used to raise errors and exceptions in the PL/pgSQL function as follows:

`RAISE NOTICE 'amount value is small.';`

### Compound statements

The syntax for conditional statements is as follows:
```
IF expression THEN
  -- Statements
ELSE
  -- Statements
END IF;
```

The syntax for looping statements is as follows:
```
LOOP
  -- Statements
END LOOP;
```

### Expressions

An expression can be a collection of one or more values, constants, variables, operators, and PostgreSQL functions that evaluate as per rules of a language, to a value that has a data type, which is one of the PostgreSQL base data types.

The following statement is an example of a Boolean expression; the part after the `WHERE` clause evaluates the matching expression:

`SELECT * FROM history WHERE amount = 1000;`

The following statement is an example of a numeric expression (one that involves mathematical calculation). Here, it is used a built-in aggregate function that calculates all rows of the `history` table.:

`SELECT COUNT(*) FROM history;`

Evaluation of expressions is actually done by the PostgreSQL server and not PL/pgSQL. All PL/pgSQL expressions are prepared only once during the lifetime of a PostgreSQL backend process.

### Conditional statements

* `IF`-`THEN`
```
IF boolean-expression THEN
  -- Statements
END IF;
```
* `IF`-`THEN`-`ELSE`
```
IF boolean-expression THEN
  -- Statements
ELSE
  -- Statements;
END IF;
```
* `IF`-`THEN`-`ELSIF`
```
IF boolean-expression THEN
  -- Statements
ELSIF another-boolean-expression THEN
  -- Another Statements
ELSE
  -- this means none of the previous conditions succeded
END IF;
```
* Simple `CASE`: helps executing conditions on equality of operands. The `searchexpression` is evaluated first and matched with each expression in the `WHEN` clause. When a match is found, associated statements will be executed and control will be transferred to the next statement after `END CASE`. If no match is found amongst the `WHEN` clauses, the `ELSE` block will be executed. If no match is found and `ELSE` is also absent, it will raise a `CASE_NOT_FOUND` exception.
```
CASE search-expression
  WHEN expression THEN
    -- Statements
  WHEN another-expression THEN
    -- Another Statements
END CASE;
```

* Searched `CASE`: The searched `CASE` statement executes a condition based on the result of the booleanexpression. This is quite similar to `IF`-`THEN`-`ELSIF`. The evaluation of the expression continues until it finds a match and then subsequent statements are executed. Control is then transferred to the next statement after `END CASE`.
```
CASE
  WHEN boolean-expression THEN
    -- Statements
  WHEN another-boolean-expression THEN
    -- Another Statements
END CASE;
```

### Loops

* The simple loop: The simple loops are composed of an unconditional loop that ends only with an `EXIT` statement (which can be used with all loop constructs)
```
LOOP
  -- Statements
END LOOP;
```
The syntax for `EXIT` is `EXIT WHEN boolean-expression;`

If no label is given to an `EXIT` command, the innermost loop is terminated. i.e.:
```
LOOP
  result = result-1;
  IF result > 0 THEN
    -- exits loop
    EXIT;
  END IF;
END LOOP;
```
or
```
LOOP
  result = result -1
  EXIT WHEN result > 0;
END LOOP;
```
* The `WHILE` loop: The WHILE loop will loop until the `boolean-expression` becomes `false`. The expression is evaluated first before executing the associated commands. The syntax is as follows:
```
WHILE boolean-expression
LOOP
  -- Statements
END LOOP;
```
* The `FOR` loop: It iterates over a range of integer values. An iterative integer is declared here and doesn’t need to be declared in the `DECLARE` block. The life scope of this variable remains within the `FOR` loop and ends after the loop exits. By default, it iterates with a step of `1`, unless specified in the `BY` clause. Iteration ranges in the upper and lower ranges are defined as two expressions. If the `REVERSE` clause is given, then the iterated value will not step up but will be subtracted. Example
```
CREATE OR REPLACE FUNCTION get_grade(subjects integer) RETURNS integer AS $$
  DECLARE
    grade integer := 2;
  BEGIN
    FOR i IN 1..10
    LOOP
      grade := grade * subjects;
    END LOOP;
  RETURN grade;
END;
$$ LANGUAGE plpgsql;

SELECT get_grade(5);
 get_grade
-----------
      2048
(1 row)
```
And using `REVERSE`:
```
CREATE OR REPLACE FUNCTION get_grade(subjects integer) RETURNS integer AS $$
  DECLARE
    grade integer := 2;
  BEGIN
    FOR i IN REVERSE 10..1 BY 2
    LOOP
      grade := grade * subjects;
    END LOOP;
  RETURN grade;
END;
$$ LANGUAGE plpgsql;

SELECT get_grade(2);
 get_grade
-----------
        64
(1 row)
```
Also, iteration over a dynamic query (that is unknown at the time of writing the function and processed on runtime) can be done using the `EXECUTE` keyword in the following manner:
```
CREATE OR REPLACE FUNCTION count_in_query(query VARCHAR) RETURNS integer AS $$
  DECLARE
    count integer := 0;
    table_records RECORD;
  BEGIN
    FOR table_records IN EXECUTE query
    LOOP
      count := count + 1;
    END LOOP;
    RETURN count;
  END;
$$ LANGUAGE 'plpgsql';

SELECT count_in_query('SELECT * FROM table_name');
 count_in_query
----------------
              7
(1 row)
```

## [Exception handling](https://www.postgresql.org/docs/current/plpgsql-errors-and-messages.html)

Implementing `RAISE` statements takes care of error handling in an efficient manner with the options of `NOTICE`, `DEBUG`, and `EXCEPTION`. By defining the level of issues it should raise, it sends information to be displayed and logs it in PostgreSQL logs. The syntax for a `RAISE` statement is as follows:

`RAISE NOTICE|DEBUG|EXCEPTION 'the message string';`

## Native support for other procedural languages

PL/pgSQL is not the only procedural language available for PostgreSQL. It has native support for Python, Perl and Tcl as well. It doesn’t stop here. However, it allows the usage of other procedural languages, for example, PL/Java and PL/Ruby. PostgreSQL uses bison (a parser generator) in the parsing process, thus enabling it to allow support for various open source languages.
