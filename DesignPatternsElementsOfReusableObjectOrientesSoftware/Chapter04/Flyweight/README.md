# Flyweight

## Intent

Use sharing to support large numbers of fine-grained objects efficiently.

## Applicability

The Flyweight pattern's effectiveness depends heavily on how and where it's used. Apply the Flyweight pattern when all of the following are true:

* An application uses a large number of objects
* Storage costs are high because of the sheer quantity of objects
* Most object state can be made extrinsic
* Many groups of objects may be replaced by relatively few shared objects once extrinsic state is removed
* The application doesn't depend on object identity. Since flyweight objects may be shared, identity tests will return true for conceptually distinct objects

## Structure

![Image of the structure for the Flyweight Pattern](./image/flyweight_structure.png "Structure for the Flyweight Pattern")

THe following diagram show how flyweights are shared:

![Image of how flyweights are shared](./image/flyweight_shared.png "How flyweights are shared")

## Participants

* **`Flyweight`**: declares an interface through which flyweights can receive and act on extrinsic state
* **`ConcreteFlyweight`**: implements the `Flyweight` interface and adds storage for intrinsic state, if any. A `ConcreteFlyweight` object must be sharable. Any state it stores must be intrinsic; that is, it must be independent of the `ConcreteFlyweight` object's context.
* **`UnsharedConcreteFlyweight`**: not all `Flyweight` subclasses need to be shared. The `Flyweight` interface *enables* sharing; it doesn't enforce it. It's common for `UnsharedConcreteFlyweight` objects to have `ConcreteFlyweight` objects as children at some level in the flyweight object structure
* **`FlyweightFactory`**:
  - creates and manages flyweight objects
  - ensures that flyweights are shared properly. When a client requests a flyweight, the `FlyweightFactory` object supplies an existing instance or creates one, if none exists
* **`Client`**:
  - maintains a reference to flyweight
  - computes or stores the extrinsic state of flyweight(s)

## Collaborations

* State that a flyweight needs to function must be characterized as either intrinsic or extrinsic. Intrinsic state is stored in the `ConcreteFlyweight` object; extrinsic state is stored or computed by `Client` objects. `Client`s pass this state to the flyweight when they invoke its operations.
* Clients should not instantiate `ConcreteFlyweights` directly. Clients must obtain `ConcreteFlyweight` objects exclusively from the `FlyweightFactory` object to ensure they are shared properly.

## Consequences

Flyweights may introduce run-time costs associated with transferring, finding, and/or computing extrinsic state, especially if it was formerly stored as intrinsic state. However, such costs are offset by space savings, which increase as more flyweights are shared.

Storage savings are a function of several factors:

* the reduction in the total number of instances that comes from sharing
* the amount of intrinsic state per object
* whether extrinsic state is computed or stored

The *Flyweight* pattern is often combined with the *Composite* pattern to represent a hierarchical structure as a graph with shared leaf nodes. A consequence of sharing is that flyweight leaf nodes cannot store a pointer to their parent. Rather, the parent pointer is passed to the flyweight as part of its extrinsic state. This has a major impact on how the objects in the hierarchy communicate with each other.
