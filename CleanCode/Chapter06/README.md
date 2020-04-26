# Chapter 6: Objects and Data Structures

## Data Abstraction

Hiding implementation is not just a matter of putting a layer of functions between the variables. Hiding implementation is about abstractions! A class does not simply push its variables out through getters and setters. Rather it exposes abstract interfaces that allow its users to manipulate the _essence_ of the data, without having to know its implementation.

This is not merely accomplished by using interfaces and/or getters and setters. Serious thought needs to be put into the best way to represent the data that an object contains. The worst option is to blithely add getters and setters.

## Data/Object Anti-Symmetry

Objects hide their data behind abstractions and expose functions that operate on that data. Data structure expose their data and have no meaningful functions.

Procedural code (code using data structures) makes it easy to add new functions without changing the existing data structures. OO code, on the other hand, makes it easy to add new classes without changing existing functions.

Procedural code makes it hard to add new data structures because all the functions must change. OO code makes it hard to add new functions because all the classes must change (not so true in some cases, such as Java 8, where default methods can be declared in interfaces and may be suitable in some cases).

## The Law of Demeter

A method `f` of a class `C` should only call the methods of these:

* `C`
* An object created by `f`
* An object passed as an argument to `f`
* An object held in an instance variable of `C`

The method should not invoke methods on objects that are returned by any of the allowed functions. In other words, talk to friends, not to strangers.

### Train Wrecks
