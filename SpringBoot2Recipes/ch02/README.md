# Chapter 2: Basics

## 2.1 Configure a Bean

**Problem**

Use an own class as a bean in a Spring Boot application.

**Solution**

Depending on the specific needs, it can be done by either leveraging `@ComponentScan` to automatically detect the class and have an instance created; using it together with `@Autowired` and `@Value` to get the dependencies or properties injected; or a method can be annotated with `@Bean` to have more control over the construction of the bean being created.

### Using `@Component`

When it is needed to scan packages not covered by the default component scanning, then it is needed to add a `@ComponentScan` annotation to the `@SpringBootApplication` annotated class so that those packages will be scanned as well. These packages are scanned in **addition** to the default scanning applied through the `@SpringBootApplication` annotation.

## 2.2 Externalize Properties

**Problem** 

It is wanted to use properties to configure the application for different environments or executions.

**Solution**

By default, Spring Boot supports getting properties from numerous locations. By default, it will load a file named `application.properties`, and use the environment variables and Java System properties. When running from the command line, it will also take command line arguments into consideration. There are more locations that are taken into account depending on the type of application and availability of capabilities (like JNDI, for instance).

## 2.3 Testing

**Problem**

How to write a test for a component or part of a Spring Boot application?

**Solution**

Spring Boot extended the range of features of the Spring Test framework. It added support for mocking and spying on beans as well as provided auto-configuration for web tests. However, it also introduced easy ways of testing slices of your application by only bootstrapping that which is needed (through the use of `@WebMvCTest` or `@JdbcTest` for instance).

## 2.4 Configure Logging

**Problem**

How to configure log levels for certain loggers?

**Solution**

The logging framework and configuration can be configured with Spring Boot. It ships with a default configuration for the supported log providers (Logback, Log4j 2, and Java Util Logging). Next to the default configuration, it also adds support for configuring the logging levels through the regular `application.properties` as well as specifying patterns and where to, optionally, write log files to.

## 2.5 Reusing Existing Configuration

**Problem**

Given an existing, non-Spring Boot, application or module how to reuse the configuration with Spring Boot.

**Solution**

To import an existing configuration, the `@Import` or `@ImportResource` annotation must be added to the `@Configuration` or `@SpringBootApplication` annotated class to import the configuration.
