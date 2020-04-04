# Chapter 3: Spring MVC

Spring Boot will automatically configure a web application when it finds the classes on the classpath. It will also start an embedded server (by default it will launch an embedded Tomcat).

## 3.1 Getting Started with Spring MVC

**Problem**

How to use Spring Boot to power a Spring MVC application?

**Solution**

Spring Boot will do auto-configuration for the components needed for Spring MVC. To enable this, Spring Boot needs to be able to detect the Spring MVC classes on its classpath. For this, the spring-boot-starter-web needs to be added as a dependency.

## 3.2 Exposing REST Resources with Spring MVC

**Problem**

How to use Spring MVC to expose REST based resources `@WebMvcTest`.

**Solution**

A JSON library is needed to do the JSON marshalling (although XML and other formats could be used as well, as content negotiation 3 is part of REST).
