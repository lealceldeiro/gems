# CHAPTER 10: Cross-Cluster Data Mirroring

Apache Kafka's built-in cross-cluster replicator is called MirrorMaker.

## Use Cases of Cross-Cluster Mirroring

- Regional and central clusters
- High availability (HA) and disaster recovery (DR)
- Regulatory compliance
- Cloud migrations
- Aggregation of data from edge clusters

## Multicluster Architectures

Some principles that should guide these architectures:

- No less than one cluster per datacenter.
- Replicate each event exactly once (barring retries due to errors) between each pair of datacenters.
- When possible, consume from a remote datacenter rather than produce to a remote datacenter.

### Hub-and-Spoke Architecture

The main benefit of this architecture is that data is always produced to the local datacenter and events from each
datacenter are only mirrored once—to the central datacenter.

The main drawback of this architecture is the direct result of its benefits and simplicity. Processors in one regional
datacenter can’t access data in another.

### Active-Active Architecture

One benefit of this architecture is the ability to serve users from a nearby datacenter, which typically has performance
benefits, without sacrificing functionality due to limited availability of data.

Another benefit is redundancy and resilience. Since every datacenter has all the functionality, if one datacenter is
unavailable, you can direct users to a remaining datacenter. This type of failover only requires network redirects of
users, typically the easiest and most transparent type of failover.

The main drawback of this architecture is the challenge in avoiding conflicts when data is read and updated
asynchronously in multiple locations.

### Active-Standby Architecture

The benefits of this setup are simplicity in setup and the fact that it can be used in pretty much any use case.

The disadvantages are waste of a good cluster and the fact that failover between Kafka clusters is, in fact, much harder
than it looks.

### Stretch Clusters

One advantage of this architecture is in the synchronous replication—some types of business simply require that their
DR site is always 100% synchronized with the primary site.

Other advantage is that both datacenters and all brokers in the cluster are used. There is no waste like in
_active-standby_ architectures.

This architecture is limited in the type of disasters it protects against. It only protects from datacenter failures,
not any kind of application or Kafka failures. The operational complexity is also limited. This architecture demands
physical infrastructure that not all companies can provide.

## Apache Kafka's MirrorMaker

MirrorMaker is highly configurable. In addition to the cluster settings to define the topology, Kafka Connect, and
connector settings, every configuration property of the underlying producer, consumers, and admin client used by
MirrorMaker can be customized.

Example: start MirrorMaker with the configuration options specified in the properties file:

```shell
bin/connect-mirror-maker.sh etc/kafka/connect-mirror-maker.properties
```

Any number of these processes can be started to form a dedicated MirrorMaker cluster that is scalable and
fault-tolerant. The processes mirroring to the same cluster will find each other and balance load between them
automatically. Usually when running MirrorMaker in a production environment, we'd want to run MirrorMaker as a service,
running in the background with `nohup` and redirecting its console output to a log file. The tool also has `-daemon`
as a command-line option that should do that for us.

Production deployment systems like Ansible, Puppet, Chef, and Salt are often used to automate deployment and manage
the many configuration options. MirrorMaker may also be run inside a Docker container. MirrorMaker is completely
stateless and doesn't require any disk storage (all the data and state are stored in Kafka itself).

When deploying MirrorMaker in production, it is important to monitor it as follows:

- Kafka Connect monitoring
- MirrorMaker metrics monitoring
- Lag monitoring
- Producer and consumer metrics monitoring
- Canary
