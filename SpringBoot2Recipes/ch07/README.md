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

## 7.3 Use JPA

**Problem**

How to use JPA in your Spring Boot application.

**Solution**

Spring Boot automatically detects the presence of Hibernate, and the needed JPA classes will use that information to configure the EntityManagerFactory.

## 7.4 Use Plain Hibernate

**Problem**

You have some code that uses the plain Hibernate API, Session, and/or SessionFactory that you want to use with Spring Boot.

**Solution**

Use the `EntityManager` or `EntityManagerFactory` to obtain the plain Hibernate objects like `Session` or `SessionFactory`.

## 7.5 Spring Data MongoDB

**Problem**

How to use MongoDB in your Spring Boot application.

**Solution**

Add the Mongo Driver as a dependency and use the `spring.data.mongodb` properties to let Spring Boot set up the MongoTemplate to the correct MongoDB.
