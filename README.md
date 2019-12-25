# Disclaimer

This is not intented to be used as an official reference of any kind. It is only intended to be used as a memo for myself after reading the great book [Effective Java 3rd Edition](https://www.amazon.com/Effective-Java-Joshua-Bloch-ebook/dp/B078H61SCH). It is only made public because I think many people may find it useful as a summary, but, if you want to really learn about this, consider buying the linked book.

# Effective Java Notes

## Chapter 2. Creating and Destroying Objects

### Item 1: Consider static factory methods instead of constructors

* One advantage of static factory methods is that, unlike constructors, they have names.
* A second advantage of static factory methods is that, unlike constructors, they are not required to create a new object each time they’re invoked.
* A third advantage of static factory methods is that, unlike constructors, they can return an object of any subtype of their return type.
* A fourth advantage of static factories is that the class of the returned object can vary from call to call as a function of the input parameters.
* A fifth advantage of static factories is that the class of the returned object need not exist when the class containing the method is written.
* The main limitation of providing only static factory methods is that classes without public or protected constructors cannot be subclassed.
* A second shortcoming of static factory methods is that they are hard for programmers to find.

### Item 2: Consider a builder when faced with many constructor parameters

* The telescoping constructor pattern works, but it is hard to write client code when there are many parameters, and harder still to read it.
* A JavaBean may be in an inconsistent state partway through its construction.
* The JavaBeans pattern precludes the possibility of making a class immutable.
* The Builder pattern simulates named optional parameters.
* The Builder pattern is well suited to class hierarchies.
* The Builder pattern is a good choice when designing classes whose constructors or static factories would have more than a handful of parameters.
