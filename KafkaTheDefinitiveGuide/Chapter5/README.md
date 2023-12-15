# CHAPTER 5: Managing Apache Kafka Programmatically

Kafka's _AdminClient_ is asynchronous. It is useful for application developers who want to create topics on the fly and
validate that the topics they are using are configured correctly for their application. It is also useful for operators
and SREs who want to create tooling and automation around Kafka or need to recover from an incident.

### AdminClient Lifecycle: Creating, Configuring, and Closing

Example:

```java
Properties props = new Properties();
props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
AdminClient admin = AdminClient.create(props);
// Do something useful with AdminClient
admin.close(Duration.ofSeconds(30));
```

All `AdminClient` configuration can be found at the
[Apache Kafka official documentation](https://kafka.apache.org/documentation/#adminclientconfigs).

Some noteworthy ones are:

- `client.dns.lookup`
- `request.timeout.ms`

`AdminClient` result objects throw `ExecutionException` when Kafka responds with an error. This is because
`AdminClient` results are wrapped `Future` objects, and those wrap exceptions. The cause of `ExecutionException`
needs to be examined always to get the error that Kafka returned.
