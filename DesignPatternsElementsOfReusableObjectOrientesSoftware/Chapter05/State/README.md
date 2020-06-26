# State

## Intent

Allow an object to alter its behavior when its internal state changes. The object will appear to change its class.

## Also Known As

Objects for States

## Applicability

* An object's behavior depends on its state, and it must change its behavior at run-time depending on that state.
* Operations have large, multipart conditional statements that depend on the object's state. This state is usually represented by one or more enumerated constants. Often, several operations will contain this same conditional structure. The *State* pattern puts each branch of the conditional in a separate class. This lets you treat the object's state as an object in its own right that can vary independently from other objects.


## Structure

![Image of the structure for the State Pattern](./image/memento.png "Structure for the State Pattern")

## Participants
* **`Context`**
  - defines the interface of interest to clients
  - maintains an instance of a `ConcreteState` subclass that defines the current state
* **`State`**: defines an interface for encapsulating the behavior associated with a particular state of the `Context`
* **`ConcreteState` subclasses**: each subclass implements a behavior associated with a state of the `Context`

## Collaborations

* `Context` delegates state-specific requests to the current `ConcreteState` object
* A context may pass itself as an argument to the `State` object handling the request. This lets the `State` object access the context if necessary
* `Context` is the primary interface for clients. Clients can configure a context with `State` objects. Once a context is configured, its clients don't have to deal with the `State` objects directly
* Either `Context` or the `ConcreteState` subclasses can decide which state succeeds another and under what circumstances

## Consequences

* It localizes state-specific behavior and partitions behavior for different states
* It makes state transitions explicit
* State objects can be shared

## Related Patterns

* The *Flyweight* pattern explains when and how *State* objects can be shared
* *State* objects are often *Singletons*

![Class Diagram for State](./image/code_class_design.png "Class Diagram for State pattern example")

```java
```
