# Chapter 3: Working with Indexes

## What is an index?

An index is a quick access path to a single row of a table in the database. A database index is similar to a book index where any specific information can be located by looking at the index page to avoid the full search of the book, which is an exhaustive operation. Similarly, a database index is created to minimize table traversal and maximize performance.

When PostgreSQL executes a query, it must choose an execution strategy and the [`EXPLAIN`](https://www.postgresql.org/docs/current/sql-explain.html) query command can be used to check which scan will be used for the query.

## How to create an index?

