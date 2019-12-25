# Disclaimer

This is not intented to be used as an official reference of any kind. It is only intended to be used as a memo for myself after reading the great book [Effective Java 3rd Edition](https://www.amazon.com/Effective-Java-Joshua-Bloch-ebook/dp/B078H61SCH). It is only made public because I think many people may find it useful as a summary, but, if you want to really learn about this, consider buying the linked book.

# Effective Java Notes

## Chapter 2. Creating and Destroying Objects

### Item 1: Consider static factory methods instead of constructors

#### Advantages

* Unlike constructors,static factory methods have names.
* Unlike constructors, static factory methods are not required to create a new object each time they’re invoked.
* Unlike constructors, they can return an object of any subtype of their return type.
* The class of the returned object by the static factories can vary from call to call as a function of the input parameters.
* The class of the returned object need not exist when the class containing the method is written.

#### Disadvantages

* Classes without public or protected constructors cannot be subclassed.
* Static factories are hard for programmers to find.
