# Chapter 10: Classes

## Class Organization

Following the standard Java convention, a class should begin with a list of variables. Public static constants, if any, should come first. Then private static variables, followed by private instance variables.

Public functions should follow the list of variables. We like to put the private utilities called by a public function right after the public function itself.

### Encapsulation

We like to keep our variables and utility functions private, but we’re not fanatic about it.

## Classes Should Be Small!

The size of a class is counted in _responsibilities_

The name of a class should describe what responsibilities it fulfills.

We should also be able to write a brief description of the class in about 25 words, without using the words “if,” “and,” “or,” or “but”.

### The Single Responsibility Principle

The Single Responsibility Principle (SRP) states that a class or module should have one, and only one, reason to change.

Trying to identify responsibilities (reasons to change) often helps us recognize and create better abstractions in our code.

SRP is one of the more important concept in OO design. It’s also one of the simpler concepts to understand and adhere to. Yet oddly, SRP is often the most abused class design principle.

Every sizable system will contain a large amount of logic and complexity. The primary goal in managing such complexity is to organize it so that a developer knows where to look to find things and need only understand the directly affected complexity at any given time.

Each small class encapsulates a single responsibility, has a single reason to change, and collaborates with a few others to achieve the desired system behaviors.

### Cohesion

Classes should have a small number of instance variables. Each of the methods of a class should manipulate one or more of those variables. In general the more variables a method manipulates the more cohesive that method is to its class. A class in which each variable is used by each method is maximally cohesive.

When cohesion is high, it means that the methods and variables of the class are co-dependent and hang together as a logical whole.

### Maintaining Cohesion Results in Many Small Classes

When classes lose cohesion, they should be split!

## Organizing for Change

We want to structure our systems so that we muck with as little as possible when we update them with new or changed features. In an ideal system, we incorporate new features by extending the system, not by making modifications to existing code.

### Isolating from Change

A client class depending upon concrete details is at risk when those details change. We can introduce interfaces and abstract classes to help isolate the impact of those details.

If a system is decoupled enough to be tested, it will also be more flexible and promote more reuse. The lack of coupling means that the elements of our system are better isolated from each other and from change. This isolation makes it easier to understand each element of the system.

By minimizing coupling in this way, our classes adhere to another class design principle known as the Dependency Inversion Principle.
