# Chapter 9: Messaging

## 9.1 Configure JMS

**Problem**

How to use JMS in a Spring Boot application and how to connect to the JMS broker.

**Solution**

Spring Boot supports auto-configuration for [ActiveMQ](https://activemq.apache.org/) and [Artemis](https://activemq.apache.org/components/artemis/). Adding one of those JMS Providers together with setting some properties in, respectively, the `spring.activemq` and `spring.artemis` namespace is everything needed.
