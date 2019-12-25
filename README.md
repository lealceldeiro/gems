# Disclaimer

This is not intented to be used as an official reference of any kind. It is only intended to be used as a memo for myself after reading the great book [Effective Java 3rd Edition](https://www.amazon.com/Effective-Java-Joshua-Bloch-ebook/dp/B078H61SCH). It is only made public because I think many people may find it useful as a summary, but, if you want to really learn about this, consider buying the linked book.

# Effective Java - Notes

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

### Item 3: Enforce the singleton property with a private constructor or an enum type

* Making a class a singleton can make it difficult to test its clients.
* A single-element enum type is often the best way to implement a singleton.

### Item 4: Enforce noninstantiability with a private constructor

* Attempting to enforce noninstantiability by making a class abstract does not work.
* A class can be made noninstantiable by including a private constructor.

### Item 5: Prefer dependency injection to hardwiring resources

* Static utility classes and singletons are inappropriate for classes whose behavior is parameterized by an underlying resource.
* A better alternative to satisfy the ability to support multiple instances of the class, each of which uses the resource desired by the client, is pass the resource into the constructor when creating a new instance.

### Item 6: Avoid creating unnecessary objects

* Autoboxing blurs but does not erase the distinction between primitive and boxed primitive types.
* Prefer primitives to boxed primitives, and watch out for unintentional autoboxing.

### Item 7: Eliminate obsolete object references

* Nulling out object references should be the exception rather than the norm.
* Whenever a class manages its own memory, the programmer should be alert for memory leaks.
* Another common source of memory leaks is caches.
* A third common source of memory leaks is listeners and other callbacks.

### Item 8: Avoid finalizers and cleaners

* Finalizers are unpredictable,often dangerous,and generally unnecessary.
* Cleaners are less dangerous than finalizers, but still unpredictable, slow, and generally unnecessary.
* Never do anything time-critical in a finalizer or cleaner.
* Never depend on a finalizer or cleaner to update persistent state.
* There is a severe performance penalty for using finalizers and cleaners.
* Finalizers have a serious security problem: they open your class up to finalizer attacks.
* Throwing an exception from a constructor should be sufficient to prevent an object from coming into existence; in the presence of finalizers, it is not.
* To protect nonfinal classes from finalizer attacks, write a final finalize method that does nothing.
* Have your class implement `AutoCloseable`

### Item 9: Prefer `try-with-resources` to` try-finally`

## Chapter 3. Methods Common to All Objects

### Item 10: Obey the general contract when overriding `equals`

* Once you’ve violated the `equals` contract, you simply don’t know how other objects will behave when confronted with your object.
* There is no way to extend an instantiable class and add a value component while preserving the `equals` contract, unless you’re willing to forgo the benefits of object-oriented abstraction.
* Do not write an equals method that depends on unreliable resources.
* When you are finished writing your equals method, ask yourself three questions: Is it symmetric? Is it transitive? Is it consistent? (create unit tests).
* Always override `hashCode` when you override `equals`.
* Don’t substitute another type for Object in the equals declaration.
