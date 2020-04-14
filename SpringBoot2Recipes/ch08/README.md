# Chapter 8: Java Enterprise Services

## 8.1 Spring Asynchronous Processing

**Problem**

How to asynchronously invoke a method with a long-running task?

**Solution**

Spring has support to configure a `TaskExecutor` and the ability to asynchronously execute methods annotated with `@Async`. This can be done in a transparent way without the normal setup for doing asynchronous execution. Spring Boot, however, will not automatically detect the need for asynchronous method execution. This support has to be enabled with the `@EnableAsync` configuration annotation.
