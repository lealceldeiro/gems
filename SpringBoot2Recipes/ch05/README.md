# Chapter 5: Spring WebFlux

# 5.1 Developing a Reactive Application with Spring WebFlux

**Problem**

How to develop a simple reactive web application with Spring WebFlux to learn the basic concepts and configurations of this framework.

**Solution**

The lowest component of Spring WebFlux is the `HttpHandler`, an interface with a single handle method.

```
public interface HttpHandler {
  Mono<Void> handle(ServerHttpRequest request, ServerHttpResponse response);
}
```
