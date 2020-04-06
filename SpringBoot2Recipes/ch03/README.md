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

A JSON library is needed to do the JSON marshalling (although XML and other formats could be used as well, as content negotiation 3 is part of REST). The `spring-boot-starter-web` dependency already includes the needed Jackson libraries by default.

## 3.3 Using Thymeleaf with Spring Boot

**Problem**

How to use Thymeleaf to render the pages of the application?

**Solution**

The dependency for Thymeleaf needs to be added and a regular `@Controller` has to be created to determine the view and fill the model.

## 3.4 Handling Exceptions

**Problem**

How to customize the default white label error page shown by Spring Boot.

**Solution**

An additional `error.html` must be added as a customized error page, or specific error pages for specific HTTP error codes (i.e., `404.html` and `500.html`).

## 3.5 Internationalizing an Application

**Problem**

When developing an internationalized web application, the web pages have to be displayed in a user’s preferred locale. It's never wanted to create different versions of the same page for different locales.

**Solution**

To avoid creating different versions of a page for different locales, the web pages should be made independent of the locale by externalizing locale-sensitive text messages. Spring is able to resolve text messages by using a message source, which has to implement the `MessageSource` interface. In the page templates it can be used either special tags or do lookups for the messages.

## 3.6 Resolving User Locales

**Problem**

In order for a web application to support internationalization, each user’s preferred locale must be identified and display contents according to this locale.

**Solution**

In a Spring MVC application, a user’s locale is identified by a locale resolver, which has to implement the `LocaleResolver` interface. Spring MVC comes with several `LocaleResolver` implementations for you to resolve locales by different criteria. Alternatively, an own custom locale resolver may be created by implementing that interface.

Spring Boot allows to set the `spring.mvc.locale-resolver` property. This can be set to `ACCEPT` (the default) or `FIXED`. The first will create an `AcceptHeaderLocaleResolver`; the latter, a `FixedLocaleResolver`. It can be also defined a locale resolver by registering a bean of type `LocaleResolver` in the web application context. **The bean name of the locale resolver must set to `localeResolver` so it can be autodetected**.

## 3.7 Selecting and Configuring the Embedded Server

**Problem**

How to use Jetty as an embedded container instead of the default Tomcat container.

**Solution**

The Tomcat runtime must be excluded and the Jetty runtime must be included. Spring Boot will automatically detect if Tomcat, Jetty, or Undertow is on the classpath and configure the container accordingly.

## 3.8 Configuring SSL for the Servlet Container

**Problem**

How to make the application to be accessible through HTTPS next (or instead of ) HTTP.

**Solution**

A certificate has to be placed in a keystore, and using the `server.ssl` namespace, the keystore must be configured. Spring Boot will then automatically configure the server to be accessible through HTTPS only.
