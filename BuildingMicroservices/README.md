# Building Microservices: Designing Fine-Grained Systems

Main notes taken from the book [Building Microservices: Designing Fine-Grained Systems](https://www.amazon.com/Building-Microservices-Designing-Fine-Grained-Systems/dp/1491950358)

Errata: https://www.oreilly.com/catalog/errata.csp?isbn=9781491950357

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

### Loose Coupling in Terms of Microservices

When services are loosely coupled, a change to one service should not require a change to another. The whole point of a microservice is being able to make a change to one service and deploy it, without needing to change any other part of the system.

### High Cohesion

We want related behavior to sit together, and unrelated behavior to sit elsewhere.

### Bounded Context

The idea behind the term *bounded context* is that any given domain consists of multiple bounded contexts, and residing within each are things (*models*) that do not need to be communicated outside as well as things that are shared externally with other bounded contexts. Each bounded context has an explicit interface, where it decides what models to share with other contexts. It's a specific responsibility enforced by explicit boundaries.

Bounded contexts can then be modeled within the codebase as modules, with shared and hidden models.

These modular boundaries then become excellent candidates for microservices. In general, microservices should cleanly align to bounded contexts.

### Premature Decomposition

Prematurely decomposing a system into microservices can be costly, especially if developers/architects are new to the domain. In many ways, having an exist‐
ing codebase wanted to be decomposed into microservices is much easier than trying to go to microservices from the beginning.

### Business Capabilities

When starting to think about the bounded contexts that exist in the organization, developers should be thinking not in terms of data that is shared, but about the capabilities those contexts provide the rest of the domain.

When considering the boundaries of the microservices, first we need to think in terms of the larger, coarser-grained contexts, and then subdivide along these nested contexts when we’re looking for the benefits of splitting out these seams.

### Communication in Terms of Business Concepts

If our systems are decomposed along the bounded contexts that represent our domain, the changes we want to make are more likely to be isolated to one, single microservice boundary. This reduces the number of places we need to make a change, and allows us to deploy that change quickly.

### The Technical Boundary

Making decisions to model service boundaries along technical seams isn’t always wrong. However, it should be the secondary driver for finding these seams, not the primary one.
