# Chapter 6: Spring Security

## 6.1 Enable Security in Your Spring Boot Application

**Problem**

How to enable security in a Spring Boot-based application.

**Solution**

Add the `spring-boot-starter-security` as a dependency to have security automatically configured and set up for the application.

## 6.2 Logging into Web Applications

**Problem**

A secure application requires its users to log in before they can access certain secure functions. This is especially important for applications running on the open Internet, because hackers can easily reach them. Most applications have to provide a way for users to input their credentials to log in.

**Solution**

Spring Security supports multiple ways for users to log into a web application. It supports form-based login by providing a default web page that contains a login form. Also a custom web page can be provided as the login page. In addition, Spring Security supports HTTP Basic authentication by processing the Basic authentication credentials presented in HTTP request headers. HTTP Basic authentication can also be used for authenticating requests made with remoting protocols and web services.

Some parts of the application may allow for anonymous access (e.g., access to the welcome page). Spring Security provides an anonymous login service that can assign a principal and grant authorities to an anonymous user, so that an anonymous user can be handled like a normal user when defining security policies.

Spring Security also supports remember-me login, which is able to remember a user’s identity across multiple browser sessions so that a user needn’t log in again after logging in for the first time.

## 6.3 Authenticating Users

**Problem**

When a user attempts to log into the application to access its secure resources, the user’s principal must be authenticated  and grant authorities to this user.

**Solution**

In Spring Security, authentication is performed by one or more `AuthenticationProviders`, connected as a chain. If any of these providers authenticates a user successfully, that user will be able to log into the application. If any provider
reports that the user is disabled or locked or that the credential is incorrect, or if no provider can authenticate the user, then the user will be unable to log into this application.

Spring Security supports multiple ways of authenticating users and includes built-in provider implementations for them. These providers can be easily configured with the built-in XML elements. Most common authentication providers authenticate users against a user repository storing user details (e.g., in an application’s memory, a relational database, or an LDAP repository).

## 6.4 Making Access Control Decisions

**Problem**

In the authentication process, an application will grant a successfully authenticated user a set of authorities. When this user attempts to access a resource in the application, the application has to decide whether the resource is accessible with the granted authorities or other characteristics.

**Solution**

The decision on whether a user is allowed to access a resource in an application is called an access control decision. It is made based on the user’s authentication status, and the resource’s nature and access attributes.

## 6.5 Adding Security to a WebFlux Application

**Problem**

How to secure an application built with Spring Web Flux by using Spring Security.

**Solution**

When adding Spring Security as a dependency to a WebFlux-based application, Spring Boot will automatically enable security. It will add an `@EnableWebFluxSecurity` annotated configuration class to the application. The `@EnableWebFluxSecurity` annotation then imports the default Spring Security `WebFluxSecurityConfiguration`.
