# Chapter 2: Basics

## Configure a Bean

**Problem**

Use an own class as a bean in a Spring Boot application.

**Solution**

Depending on the specific needs, it can be done by either leveraging `@ComponentScan` to automatically detect the class and have an instance created; using it together with `@Autowired` and `@Value` to get the dependencies or properties injected; or a method can be annotated with `@Bean` to have more control over the construction of the bean being created.
