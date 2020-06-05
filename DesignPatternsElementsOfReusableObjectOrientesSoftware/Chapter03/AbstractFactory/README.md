# Abstract Factory

## Intent

Provide an interface for creating families of related or dependent objects without specifying their concrete classes.

## Also Known As

Kit

## Applicability

* a system should be independent of how its products are created, composed, and represented
* a system should be configured with one of multiple families of products
* a family of related product objects is designed to be used together, and you need to enforce this constraint
* you want to provide a class library of products, and you want to reveal just their interfaces, not their implementations

## Structure

![Image of the structure for the Abstract Factory Patter](../image/abstract_factory.png "Structure for the Abstract Factory Patter")

## Participants

* **`AbstractFactory`**: declares an interface for operations that create abstract product objects
* **`ConcreteFactory`**: implements the operations to create concrete product objects
* **`AbstractProduct`**: declares an interface for a type of product object
* **`ConcreteProduct`**: defines a product object to be created by the corresponding concrete factory and implements the AbstractProduct interface
* **`Client`**: uses only interfaces declared by AbstractFactory and AbstractProduct classes

## Collaborations

* Normally a single instance of a `ConcreteFactory` class is created at run-time. This concrete factory creates product objects having a particular implementation. To create different product objects, clients should use a different concrete factory.
* `AbstractFactory` defers creation of product objects to its `ConcreteFactory` subclass

## Consequences

* It isolates concrete classes
* It makes exchanging product families easy
* It promotes consistency among products
* Supporting new kinds of products is difficult.

## Related Patterns

`AbstractFactory` classes are often implemented with factory methods (*FactoryMethod* pattern), but they can also be implemented using the *Prototype* pattern.

A concrete factory is often a singleton (*Singleton* pattern).

## Example
