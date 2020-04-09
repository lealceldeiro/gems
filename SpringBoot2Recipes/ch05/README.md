# Chapter 5: Spring WebFlux

## 5.1 Developing a Reactive Application with Spring WebFlux

**Problem**

How to develop a simple reactive web application with Spring WebFlux to learn the basic concepts and configurations of this framework.

**Solution**

The lowest component of Spring WebFlux is the `HttpHandler`, an interface with a single handle method.

```
public interface HttpHandler {
  Mono<Void> handle(ServerHttpRequest request, ServerHttpResponse response);
}
```

There are two ways of doing an integration test for a controller. The first approach is to simply write a test that creates an instance of the controller, call the handler methods, and do expectations on the results. The second is to use the `@WebFluxTest` annotation to create the test. The latter will start a minimal application context containing the web infrastructure and MockMvc can be used to test the controller. This last approach sits between a plain unit test and a full-blown integration test.

## 5.2 Publishing and Consuming with Reactive Rest Services

**Problem**

How to write a reactive REST endpoint that will produce JSON.

**Solution**

Just as with a regular `@RestController`, a regular object or list of objects can be returned and those will be sent to the client. To make them reactive, it is only required to wrap those return values in their reactive counterparts: a `Mono` or a `Flux`.

## 53 Use Thymeleaf as a Template Engine

**Problem**

How to render a view in a WebFlux based application?

**Solution**

Use Thymeleaf to create a view and reactively return the view name and fill the model.
