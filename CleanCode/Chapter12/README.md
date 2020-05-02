# Chapter 12: Emergence

## Getting Clean via Emergent Design

According to Kent, a design is “simple” if it follows these rules (in order of importance):

* Runs all the tests
* Contains no duplication
* Expresses the intent of the programmer
* Minimizes the number of classes and methods

## Simple Design Rule 1: Runs All the Tests

A system that is comprehensively tested and passes all of its tests all of the time is a testable system.

Systems that aren’t testable aren’t verifiable. Arguably, a system that cannot be verified should never be deployed.

Making our systems testable pushes us toward a design where our classes are small and single purpose, because it’s just easier to test classes that conform to the Single Responsability Principle.

The more tests we write, the more we’ll continue to push toward things that are simpler to test. So making sure our system is fully testable helps us create better designs.

In summary, following a simple and obvious rule that says we need to have tests and run them continuously impacts our system’s adherence to the primary OO goals of low coupling and high cohesion. Writing tests leads to better designs.

## Simple Design Rules 2–4: Refactoring

We keep our code and classes clean by incrementally refactoring the code. For each few lines of code we add, we pause and reflect on the new design.

This is also where we apply the final three rules of simple design: Eliminate duplication, ensure expressiveness, and minimize the number of classes and methods.

## No Duplication

Duplication is the primary enemy of a well-designed system. It represents additional work, additional risk, and additional unnecessary complexity.

## Expressive

The majority of the cost of a software project is in long-term maintenance. In order to minimize the potential for defects as we introduce change, it’s critical for us to be able to understand what a system does.

Code should clearly express the intent of its author. The clearer the author can make the code, the less time others will have to spend understanding it. This will reduce defects and shrink the cost of maintenance.

## Minimal Classes and Methods

High class and method counts are sometimes the result of pointless dogmatism. Consider, for example, a coding standard that insists on creating an interface for each and every class. Or consider developers who insist that fields and behavior must always be separated into data classes and behavior classes. Such dogma should be resisted and a more pragmatic approach adopted.
