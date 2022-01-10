# Building Microservices: Designing Fine-Grained Systems

Main notes taken from the book [Building Microservices: Designing Fine-Grained Systems](https://www.amazon.com/Building-Microservices-Designing-Fine-Grained-Systems/dp/1491950358)

Errata: https://www.oreilly.com/catalog/errata.csp?isbn=9781491950357

## Chapter 2

Here are, what may be seen as the core responsibilities of the software architect:

### Vision

Ensure there is a clearly communicated technical vision for the system that will help the system meet the requirements of the customers and the organization.

### Empathy

Understand the impact of the decisions taken on the customers and colleagues

### Collaboration

Engage with as many of the peers and colleagues as possible to help define, refine, and execute the vision

### Adaptability

Make sure that the technical vision changes as the customers or organization requires it

### Autonomy

Find the right balance between standardizing and enabling autonomy for the teams

### Governance

Ensure that the system being implemented fits the technical vision

## Chapter 3

### Loose Coupling in Terms of Microservices

When services are loosely coupled, a change to one service should not require a change to another. The whole point of a microservice is being able to make a change to one service and deploy it, without needing to change any other part of the system.

### High Cohesion

We want related behavior to sit together, and unrelated behavior to sit elsewhere.

### Bounded Context

The idea behind the term *bounded context* is that any given domain consists of multiple bounded contexts, and residing within each are things (*models*) that do not need to be communicated outside as well as things that are shared externally with other bounded contexts. Each bounded context has an explicit interface, where it decides what models to share with other contexts. It's a specific responsibility enforced by explicit boundaries.

Bounded contexts can then be modeled within the codebase as modules, with shared and hidden models.

These modular boundaries then become excellent candidates for microservices. In general, microservices should cleanly align to bounded contexts.

### Premature Decomposition

Prematurely decomposing a system into microservices can be costly, especially if developers/architects are new to the domain. In many ways, having an existing codebase wanted to be decomposed into microservices is much easier than trying to go to microservices from the beginning.

### Business Capabilities

When starting to think about the bounded contexts that exist in the organization, developers should be thinking not in terms of data that is shared, but about the capabilities those contexts provide the rest of the domain.

When considering the boundaries of the microservices, first we need to think in terms of the larger, coarser-grained contexts, and then subdivide along these nested contexts when we’re looking for the benefits of splitting out these seams.

### Communication in Terms of Business Concepts

If our systems are decomposed along the bounded contexts that represent our domain, the changes we want to make are more likely to be isolated to one, single microservice boundary. This reduces the number of places we need to make a change, and allows us to deploy that change quickly.

### The Technical Boundary

Making decisions to model service boundaries along technical seams isn’t always wrong. However, it should be the secondary driver for finding these seams, not the primary one.

## Chapter 4

### Integration

Getting integration right may be the single most important aspect of the technology associated with microservices. When it's well done the microservices retain their autonomy, allowing us to change and release them independent of the whole.

### Choosing an Integration Technology

#### Avoid Breaking Changes

While picking a technology, it should ensures this happens as rarely as possible. For example, if a microservice adds new fields to a piece of data it sends out, existing consumers shouldn’t be impacted.

#### Keep APIs Technology-Agnostic

It is very important to ensure that the APIs used for communication between microservices are kept technology-agnostic. This means avoiding integration technology that dictates what technology stacks we can use to implement the microservices.

#### Service Simple for Consumers

It should be made easy for consumers to use the service:

  - Clients should be allowed full freedom in their technology choice
  - Providing a client library can ease adoption (sometimes this can come at the cost of increased coupling)

#### Hide Internal Implementation Detail

Consumers shouldn't be bound to internal implementation. This leads to increased coupling. So any technology that pushes to expose internal representation detail should be avoided.

### Synchronous Versus Asynchronous

These two different modes of communication can enable two different idiomatic styles of collaboration: *request*/*response* or *event-based*.

### Versioning

The best way to reduce the impact of making breaking changes is to avoid making them in the first place.

Another key to deferring a breaking change is to encourage good behavior in the clients, and avoid them binding too tightly to the services in the first place.

It’s crucial to make sure changes that will break consumers are detected as soon as possible.

#### Semantic Versioning

With semantic versioning, each version number is in the form `MAJOR.MINOR.PATCH`. When the `MAJOR` number increments, it means that backward incompatible changes have been made. When `MINOR` increments, new functionality has been added that should be backward compatible. Finally, a change to `PATCH` states that bug fixes have been made to existing functionality.

#### Coexist Different Endpoints

If introducing a breaking interface change cannot be avoided, the next step is to limit the impact. What must be avoided is to force consumers to upgrade in lock-step with the updated service, as maintaining the ability to release microservices independently of each other is the target goal.

One approach to handle this is to make both the old and new interfaces coexist in the same running service. So if a breaking change is released, then a new version of the service that exposes both the old and new versions of the endpoint must be deployed.

This allows the new microservice to be usable as soon as possible, along with the new interface, but give time for consumers to move over. Once all of the consumers are no longer using the old endpoint, it can be removed along with any associated code.

#### Multiple Concurrent Service Versions

Another versioning solution often cited is to have different versions of the service live at once, and for older consumers to route their traffic to the newer version.

### UI

User interfaces should be thought as compositional layers—places where there are together the various strands of the offered capabilities.

#### Constraints

Constraints are the different forms in which the users interact with the system. On a desktop web application, for example, the constraints considered may be what browser visitors are using, or their resolution.

### As a Summary

These are the choices that are most likely to ensure the microservices remain as decoupled as possible from their other collaborators:

  - Avoid database integration at all costs.
  - Understand the trade-offs between REST and RPC, but strongly consider REST as a good starting point for request/response integration.
  - Prefer choreography over orchestration.
  - Avoid breaking changes and the need to version by understanding Postel’s Law and using [tolerant readers](https://martinfowler.com/bliki/TolerantReader.html).
  - Think of user interfaces as compositional layers.

## Chapter 5

[SchemaSpy](http://schemaspy.sourceforge.net/) (which can generate graphical representations of the relationships between tables) can be used to see the database-level constraints, for example to be able to tell if the database enforces a foreign key relationship from one table to an item in another table.

## Chapter 6

Deploying a monolithic application is a fairly straightforward process. Microservices, with their interdependence, are completely different.

If the deployment process is not approached properly, it’s one of those areas where the complexity makes the microservices architecture not worthy.

### Continuous Integration (CI)

CI has a number of benefits:

  - There's some level of fast feedback as to the quality of the code.
  - It allows the creation of the binary artifacts to be automated.
  - All the code required to build the artifact is itself version controlled, so the artifact can be re-created if needed.
  - Also, there's some level of traceability from a deployed artifact back to the code.
  - Depending on the capabilities of the CI tool itself, the tests that were run on the code and artifact can be seen too.

### Continuous Delivery (CD)

CD is the approach whereby teams get constant feedback on the production readiness of each and every check-in, and furthermore treat each and every check-in as a release candidate.


### As a Summary

The focus should be on maintaining the ability to release one service independently from another, and making sure that whatever technology is selected, it supports this.

It should be prefered to have a single repository per microservice, but more importantly there should be one CI build per microservice if they're going to be deployed separately.

If possible, there should be a single-service per host/container.

A culture of automation is key to managing everything.

Being able to use a platform like AWS will give you huge benefits when it comes to automation.

## Chapter 7

### Types of Tests

![Types of Tests Diagram](https://user-images.githubusercontent.com/15990580/117636560-9c8c2c00-b189-11eb-88b9-19fc3bc0c2cd.png)


### As a Summary

- Optimize for fast feedback, and separate types of tests accordingly.
- Avoid the need for end-to-end tests wherever possible by using consumer-driven contracts (a useful tool may be [Pact](https://github.com/pact-foundation)).
- Use consumer-driven contracts to provide focus points for conversations between teams.
- Try to understand the trade-off between putting more efforts into testing and detecting issues faster in production (optimizing for *Mean Time Between Failures* versus *Mean Time To Recover*).


## Chapter 8

For each service:

- Inbound response time should be tracked at a bare minimum. Once that's done, a follow with error rates must be done and then application-level metrics should be implemented.
- The health of all downstream responses should be tracked at a bare minimum, including the response time of downstream calls, and at best tracking error rates. Libraries like Hystrix can help here.
- How and where metrics are collected should be standardized.
- Logs should be stored in a standard location and in a standard format if possible. Aggregation is a pain if every service uses a different layout!
- The underlying operating system should be monitored so rogue processes can be tracked down and capacity planning be done.

For the system:

- Host-level metrics like CPU together with application-level metrics should be aggregated.
- Metric storage tool should allow for aggregation at a system or service level, and drill down to individual hosts.
- Metric storage tool should allow to maintain data long enough to understand trends in the system.
- A single, queryable tool for aggregating and storing logs should be available.
- Standardizing on the use of correlation IDs should be strongly considered.
- What requires a call to action should be understood, and alerting and dashboards should be structureed accordingly.


## Chapter 9

Having a system decomposed into finer-grained services gives us many more options as to how to solve a problem. Not only can having microservices potentially reduce the impact of any given breach, but it also gives us more ability to trade off the overhead of more complex and secure approaches where data is sensitive, and a lighter-weight approach when the risks are lower.

Once the threat levels of different parts of the system are understood, a sense of when to consider security during transit, at rest, or not at all, should start to be gotten.

The importance of defense in depth should be well understood, operating systems should be kept patched, and it's never a good idea to implement an own cryptography! - standard (well tested and reviewed ones) should be used instead.

A general overview of security for browser-based applications can be reviewed at [Open Web Application Security Project (OWASP)](https://owasp.org/).

## Chpater 12

### Principles of Microservices

- Small autonomus services
  * Modeled Around Business Concepts
    + Bounded contexts should be used to define potential domain boundaries
  * Culture of Automation
    + Automated testing is essential
    + Deploy the same way everywhere
    + Continuous delivery
    + Environment definitions help to specify the differences from one environment to another
  * Hide Internal Implementation Details
    + Services should hide their databases
    + Technology-agnostic APIs should be chosen when possible
  * Decentralize All the Things
    + Teams own their services
    + Teams should be aligned to the organization
  * Independently Deployable
  * Isolate Failure
    + Timeouts are set appropriately
    + When and how to use bulkheads and circuit breakers
  * Highly Observable
    + Semantic monitoring
    + Aggregate logs and stats
    + Correlation IDs
