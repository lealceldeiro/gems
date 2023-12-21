# CHAPTER 6: Kafka Internals

Kafka uses ZooKeeper’s ephemeral node feature to elect a controller and to notify the controller when nodes join and
leave the cluster. The controller is responsible for electing leaders among the partitions and replicas whenever it
notices nodes join and leave the cluster. The controller uses the epoch number to prevent a “split brain” scenario
where two nodes believe each is the current controller.

From the list of replicas for a partition displayed by the _kafka-topics.sh_ tool, the first replica in the list is
always the preferred leader.

This is true no matter who is the current leader and even if the replicas were reassigned to different brokers using
the replica reassignment tool.

When replicas are manually reassigned, the first replica specified will be the preferred one.

Common types of client requests:

- Produce requests: Sent by producers and contain messages the clients write to Kafka brokers
- Fetch requests: Sent by consumers and follower replicas when they read messages from Kafka brokers
- Admin requests: Sent by admin clients when performing metadata operations such as creating and deleting topics

New brokers know how to handle old requests, but old brokers don't know how to handle new requests. Now, in release
`0.10.0`, `ApiVersionRequest` was added, which allows clients to ask the broker which versions of each request are
supported and to use the correct version accordingly. Clients that use this new capability correctly will be able
to talk to older brokers by using a version of the protocol that is supported by the broker they are connecting to.
