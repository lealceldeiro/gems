# Chapter 8: Java Enterprise Services

## 8.1 Spring Asynchronous Processing

**Problem**

How to asynchronously invoke a method with a long-running task?

**Solution**

Spring has support to configure a `TaskExecutor` and the ability to asynchronously execute methods annotated with `@Async`. This can be done in a transparent way without the normal setup for doing asynchronous execution. Spring Boot, however, will not automatically detect the need for asynchronous method execution. This support has to be enabled with the `@EnableAsync` configuration annotation.

## 8.2 Spring Task Scheduling

**Problem**

How to schedule a method invocation in a consistent manner, using either a cron expression, an interval, or a rate.

**Solution**

Spring has support to configure `TaskExecutor`s and `TaskScheduler`s. This capability, coupled with the ability to schedule method execution using the `@Scheduled` annotation, makes Spring scheduling support work with a minimum of fuss: all that is need are a method, an annotation, and to have switched on the scanner for annotations. Spring Boot will not automatically detect the need for scheduling; it has to be enabled explicitely by using the `@EnableScheduling` annotation.
