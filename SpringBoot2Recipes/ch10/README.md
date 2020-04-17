# Chapter 10: Spring Boot Actuator

Spring Boot actuator exposes health and metrics from the application to interested parties. This can be over JMX, HTTP, or exported to an external system.

The health endpoints tell something about the health of the application and/or the system it is running on. It will detect if the database is up, report the diskspace, etc. The metrics endpoints expose usage and performance statistics like number of request, the longest request, the fastest, the utilization of your connection pool, etc.

## 10.1 Enable and Configure Spring Boot Actuator

**Problem**

How to enable health and metrics in the application so that the status of the application can be monitored.

**Solution**

Add a dependency for the `spring-boot-starter-actuator` to and the health and metrics will be enabled and exposed for the application. Additional configuration can be done through properties in the `management` namespace.

The `management.server` properties are only effective when using an embedded server; when deploying to an external server these properties don’t apply anymore.
