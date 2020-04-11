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
