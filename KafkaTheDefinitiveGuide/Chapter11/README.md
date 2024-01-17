# CHAPTER 11: Securing Kafka

## Some security procedures applied in Kafka to establish and maintain confidentiality/integrity/availability of data

- Authentication establishes your identity and determines _who_ you are.
- Authorization determines _what_ you are allowed to do.
- Encryption protects your data from eavesdropping and tampering.
- Auditing tracks what you have done or have attempted to do.
- Quotas control how much of the resources you can utilize.

## A secure deployment must guarantee

- Client authenticity
- Server authenticity
- Data privacy
- Data integrity
- Access control
- Auditability
- Availability

## Security Protocols

Each Kafka security protocol combines a transport layer (PLAINTEXT or SSL) with an optional authentication layer (SSL or
SASL):

- `PLAINTEXT`
- `SSL`
- `SASL_PLAINTEXT`
- `SASL_SSL`

Example: Configure SSL for the inter-broker and internal listeners, and SASL_SSL for the external listener:

```properties
listeners=EXTERNAL://:9092,INTERNAL://10.0.0.2:9093,BROKER://10.0.0.2:9094
advertised.listeners=EXTERNAL://broker1.example.com:9092,INTERNAL://broker1.local:9093,BROKER://broker1.local:9094
listener.security.protocol.map=EXTERNAL:SASL_SSL,INTERNAL:SSL,BROKER:SSL
inter.broker.listener.name=BROKER
```

Then clients are configured with a security protocol and bootstrap servers that determine the broker listener. Metadata
returned to clients contains only the endpoints corresponding to the same listener as the bootstrap servers:

```properties
security.protocol=SASL_SSL
bootstrap.servers=broker1.example.com:9092,broker2.example.com:9092
```

### SASL

Kafka brokers support the following SASL mechanisms out of the box:

- GSSAPI
- PLAIN
- SCRAM-SHA-256 and SCRAM-SHA-512
- OAUTHBEARER

Kafka uses the Java Authentication and Authorization Service (JAAS) for configuring SASL.

### SASL/GSSAPI

Generic Security Service Application Program Interface (GSS-API) is a framework for providing security services to
applications using different authentication mechanisms.

### SASL/SCRAM

SCRAM applies a one-way cryptographic hash function on the password combined with a random salt to avoid the actual
password being transmitted over the wire or stored in a database.

Kafka provides safeguards by supporting only the strong hashing algorithms SHA-256 and SHA-512 and avoiding
weaker algorithms like SHA-1. This is combined with a high default iteration count of 4,096 and unique random salts for
every stored key to limit the impact if ZooKeeper security is compromised.

### SASL/OAUTHBEARER

Kafka supports SASL/OAUTHBEARER for client authentication, enabling integration with third-party OAuth servers. The
built-in implementation of OAUTHBEARER uses unsecured JSON Web Tokens (JWTs) and is not suitable for production use.
Custom callbacks can be added to integrate with standard OAuth servers to provide secure authentication using the
OAUTHBEARER mechanism in production deployments.

Because the built-in implementation of SASL/OAUTHBEARER in Kafka does not validate tokens, it only requires the
login module to be specified in the JAAS configuration. If the listener is used for inter-broker communication, details
of the token used for client connections initiated by brokers must also be provided.

The option `unsecuredLoginStringClaim_sub` is the subject claim that determines the KafkaPrincipal for the connection by
default. Example:

```properties
sasl.enabled.mechanisms=OAUTHBEARER
sasl.mechanism.inter.broker.protocol=OAUTHBEARER
listener.name.external.oauthbearer.sasl.jaas.config=\
org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule \
required unsecuredLoginStringClaim_sub="kafka";
```

Clients must be configured with the subject claim option `unsecuredLoginStringClaim_sub`. Other claims and token
lifetime may also be configured:

```properties
sasl.mechanism=OAUTHBEARER
sasl.jaas.config=\
org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule \
required unsecuredLoginStringClaim_sub="John";
```

To integrate Kafka with third-party OAuth servers for using bearer tokens in production, Kafka clients must be
configured with `sasl.login.callback.handler.class` to acquire tokens from the OAuth server using the long-term password
or a refresh token.

If OAUTHBEARER is used for inter-broker communication, brokers must also be configured with a login callback handler to
acquire tokens for client connections created by the broker for inter-broker communication.

To create and validate delegation tokens, all brokers must be configured with the same master key using the
configuration option `delegation.token.master.key`.

Delegation tokens also are suitable for production use only in deployments where ZooKeeper is secure.

## Reauthentication

Kafka brokers support reauthentication for connections authenticated using SASL using the configuration option
`connections.max.reauth.ms`.

## Authorization

Kafka brokers manage access control using a customizable authorizer.

Kafka has a built-in authorizer, `AclAuthorizer`, that can be enabled by configuring the authorizer class name as
follows:

```properties
authorizer.class.name=kafka.security.authorizer.AclAuthorizer
```

Super users are granted access for all operations on all resources without any restrictions and cannot be denied access
using Deny ACLs.

## Security Considerations

Since AclAuthorizer stores ACLs in ZooKeeper, access to ZooKeeper should be restricted. Deployments without a secure
ZooKeeper can implement custom authorizers to store ACLs in a secure external database.

Restricting user access using the principle of least privilege can limit exposure if a user is compromised.

ACLs should be removed immediately when a user principal is no longer in use, for instance, when a person leaves the
organization.

Long-running applications can be configured with service credentials rather than credentials associated with a specific
user to avoid any disruption when employees leave the organization.

## Auditing

Kafka brokers can be configured to generate comprehensive _log4j_ logs for auditing and debugging. The logging level as
well as the appenders used for logging and their configuration options can be specified in `log4j.properties`.

## Securing the platform

Kafka supports externalizing passwords in a secure store.

Customizable configuration providers can be configured for Kafka brokers and clients to retrieve passwords from a secure
third-party password store.

Passwords may also be stored in encrypted form in configuration files with custom configuration providers that perform
decryption.

Sensitive broker configuration options can also be stored encrypted in ZooKeeper using the Kafka configs tool without
using custom providers.
