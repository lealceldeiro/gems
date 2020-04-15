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

## 8.3 Sending E-mail

Spring Boot will automatically configure the ability to send mail when mail properties and the java mail library are detected on the classpath.

**Problem**

How to send e-mail from a Spring Boot application.

**Solution**

Spring’s e-mail support makes it easier to send e-mail by providing an abstract and implementation-independent API for sending e-mail. The core interface of Spring’s e-mail support is `MailSender`. The `JavaMailSender` interface is a subinterface of `MailSender` that includes specialized JavaMail features such as Multipurpose Internet Mail Extensions (MIME message) support. To send an e-mail message with HTML content, inline images, or attachments, you have to send it as a MIME message. Spring Boot will automatically configure the `JavaMailSender` when the `javax.mail` classes are found on the classpath and when the appropriate `spring.mail` properties have been set.

## 8.4 Register a JMX MBean

**Problem**
How to register an object in a Spring Boot application as a JMX MBean, to have the ability to look at services that are running and manipulate their state at runtime. This allows to perform tasks like rerun batch jobs, invoke methods, and change
configuration metadata.

## Solution

Spring Boot by default enables the Spring JMX support and will detect the `@ManagedResource` annotated beans and register them with the JMX server.
