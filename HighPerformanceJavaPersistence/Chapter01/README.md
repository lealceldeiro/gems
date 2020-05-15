# Chapter I: Introduction

## 1. Preface

### 1.1 The database server and the connectivity layer

All data access frameworks rely on JDBC (Java Database Connectivity) API for communicating to a database server.

### 1.2 The application data access layer

### 1.2.2 The native query builder framework

JPA (Java Persistence API) and Hibernate were never meant to substitute SQL.

While JPA makes it possible to abstract DML statements and common entity retrieval queries, when it comes to reading and processing data, nothing can beat native SQL.

JPQL (Java Persistence Querying Language) abstracts the common SQL syntax by subtracting database specific querying features, so it lacks support for Window Functions, Common Table Expressions, Derived tables or PIVOT.

As opposed to JPA, jOOQ (Java Object Oriented Query) embraces database specific query features, and it provides a type-safe query builder which can protect the application against SQL injection attacks even for dynamic native queries.

