# Chapter 11: Packaging

## 11.1 Create an Executable Archive

By default, Spring Boot creates a JAR or WAR and that can be run with `java -jar application-name.jar`. However, it might be desired to run the application as part of the startup of a server (currently tested and supported for Debian and Ubuntu-based systems). For this the Maven or Gradle plugins can be used to create an executable jar.

**Problem**

How to get an executable JAR so that it can be installed as service on the server environment.

**Solution**

The Spring Boot Maven and Gradle plugins both have the option to make the created artifact executable. When doing so, the archive also becomes/behaves like a Unix shell script to start/stop a service.

## 11.2 Create a WAR for Deployment

**Problem**

Instead of creating a JAR file, how to create a WAR file for deployment to a Servlet Container or JEE Container.

**Solution**

Change the packaging of the application from JAR to WAR and let the Spring Boot application extend the `SpringBootServletInitializer` so that it can bootstrap itself as a regular application.
