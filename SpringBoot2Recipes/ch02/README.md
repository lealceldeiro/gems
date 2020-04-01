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
