# Chapter 12: Extensions

Extensions allow to expand certain PostgreSQL functionalities by including multiple SQL objects, like new operators, new index operator classes, and new data types along with new functions.

When creating an extension, a user will need to have the following files:

* A SQL script file, which must have some SQL commands for creating objects
* A control file, which specifies a few basic properties of the extension
* If the extension includes C code, then there will be a C shared library file

After getting all these files in one place, the user can create the extension by simply using the SQL command [`CREATE EXTENSION`](https://www.postgresql.org/docs/current/sql-createextension.html).

## Features of an extension

The user can load all objects in the database with the single `CREATE EXTENSION` command and can drop all the objects with the [`DROP EXTENSION`](https://www.postgresql.org/docs/current/sql-dropextension.html) command.

When the object is enclosed inside the extension, PostgreSQL will not allow any user to drop an individual object contained in an extension; the only way to achieve this goal is by dropping the whole extension.

The user can utilize the [`ALTER EXTENSION`](https://www.postgresql.org/docs/current/sql-alterextension.html) command to apply some changes over an extension (i.e.:when a second version of an extension integrates one new function and changes the body of another function compared to the first version, then the extension owner can provide an updated script that makes just those two changes) and track which version of the extension is authentically installed in a given database.

Extensions can have different types of PostgreSQL objects like functions and tables, but the database, table space and roles cannot be part of the extension.

Extensions are valid only within the database in which they are created.

A table can be a member of an extension, but indexes on tables are not considered members of the extension.

Schemas can belong to extensions, but extensions cannot belong to schemas because an extension does not subsist within any schema.

Some adventages of extensions are extensively repeatability and extension update.

## How to check available extensions

To check for the available extension, the following command can be used:

`SELECT name, version FROM pg_available_extension_versions;`

Beside the list of the available extensions, the installed ones using can be retrieved using the `\dx` command.

Some more extensions and information available can be found at https://www.postgresql.org/docs/current/contrib.html
