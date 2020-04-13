# Chapter 7: Data Access

## 7.1 Configuring a DataSource

**Problem**

How to access a database from a Spring Boot application.

**Solution**

Use the `spring.datasource.url`, `spring.datasource.username` and `spring.datasource.password` properties to let Spring Boot configure a DataSource.

## 7.2 Use JdbcTemplate

**Problem**

How to use `JdbcTemplate` or `NamedParameterJdbcTemplate` to have a better JDBC experience.

**Solution**

Use the automatically configured `JdbcTemplate` or `NamedParameterJdbcTemplate` to execute the queries and handle the results.
