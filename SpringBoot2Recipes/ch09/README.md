# Chapter 9: Messaging

## 9.1 Configure JMS

**Problem**

How to use JMS in a Spring Boot application and how to connect to the JMS broker.

**Solution**

Spring Boot supports auto-configuration for [ActiveMQ](https://activemq.apache.org/) and [Artemis](https://activemq.apache.org/components/artemis/). Adding one of those JMS Providers together with setting some properties in, respectively, the `spring.activemq` and `spring.artemis` namespace is everything needed.

## 9.2 Send Messages Using JMS

**Problem**

How to send messaes to other systems over JMS.

**Solution**

Use the Spring Boot-provided `JmsTemplate` to send and (optionally) convert messages.

## 9.3 Receive Messages Using JMS

**Problem**

How to read messages from a JMS destination so that you can handle them in a Spring Boot application.

**Solution**

Create a class and annotate methods with `@JmsListener` to bind it to a destination and handle incoming messages.

## 9.4 Configure RabbitMQ

**Problem**
How to use AMQP messaging in a Spring Boot application and need to connect to the RabbitMQ broker.

**Solution**

Configure the appropriate `spring.rabbitmq` properties (minimal `spring.rabbitmq.host`) to connect to the exchange and be able to send and receive messages.

## 9.5 Send Messages Using RabbitMQ

**Problem**

How to send a message to a RabbitMQ broker so that the message can be delivered to the receiver.

**Solution**

Using the `RabbitTemplate`, messages can be sent to an exchange and provide a routing key.

## 9.6 Receive Messages Using RabbitMQ

**Problem**

How to receive messages from a RabbitMQ.

**Solution**

Annotating a method with `@RabbitListener` will bind it to a queue and let it receive messages.
