# Factory Method

## Intent

Define an interface for creating an object, but let subclasses decide which class to instantiate. Factory Method lets a class defer instantiation to subclasses.

## Also Known As

Virtual Constructor

## Applicability

* a class can't anticipate the class of objects it must create
* a class wants its subclasses to specify the objects it creates
* classes delegate responsibility to one of several helper subclasses, and you want to localize the knowledge of which helper subclass is the delegate

## Structure

![Image of the structure for the Factory Method Pattern](./image/factory_method.png "Structure for the Factory Method Pattern")

## Participants

* **`Product`**: defines the interface of objects the factory method creates
* **`ConcreteProduct`**: implements the `Product` interface
* **`Creator`**:
  - declares the factory method, which returns an object of type `Product`. `Creator` may also define a default implementation of the factory method that returns a default `ConcreteProduct` object
  - may call the factory method to create a `Product` object
* **`ConcreteCreator`**: overrides the factory method to return an instance of a `ConcreteProduct`

## Collaborations

`Creator` relies on its subclasses to define the factory method so that it returns an instance of the appropriate `ConcreteProduct`

## Consequences

*Factory methods* eliminate the need to bind application-specific classes into your code. The code only deals with the `Product` interface; therefore it can work with any user-defined `ConcreteProduct` classes.

A potential disadvantage of factory methods is that clients might have to subclass the `Creator` class just to create a particular `ConcreteProduct` object. Subclassing is fine when the client has to subclass the `Creator` class anyway, but otherwise the client now must deal with another point of evolution.

Provides hooks for subclasses.

Connects parallel class hierarchies.

## Related Patterns

*Abstract Factory* is often implemented with *factory methods*.

*Factory methods* are usually called within *Template Methods*.
