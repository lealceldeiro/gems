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

### Item 11: Always override `hashCode` when you override `equals`

* You must override `hashCode` in every class that overrides `equals`.
* When you fail to override `hashCode` after overriding `equals` you violate the provision that equal objects must have equal hash codes.
* Do not be tempted to exclude significant fields from the hash code computation to improve performance.
* Don’t provide a detailed specification for the value returned by `hashCode`, so clients can’t reasonably depend on it; this gives you the flexibility to change it.

### Item 12: Always override `toString`

* Providing a good `toString` implementation makes your class much more pleasant to use and makes systems using the class easier to debug.
* When practical, the `toString` method should return all of the interesting information contained in the object.
* Whether or not you decide to specify the format of the returned value by the `toString`, you should clearly document your intentions.
* Provide programmatic access to the information contained in the value returned by `toString`.

### Item 13: Override `clone` judiciously

* In practice, a class implementing `Cloneable` is expected to provide a properly functioning public `clone` method.
* Immutable classes should never provide a `clone` method.
* In effect, the `clone` method functions as a constructor; you must ensure that it does no harm to the original object and that it properly establishes invariants on the `clone`.
* `someArray.clone()` is the preferred idiom to duplicate an array (`someArray` in this case). In fact, arrays are the sole compelling use of the clone facility.
* The `Cloneable` architecture is incompatible with normal use of final fields referring to mutable objects.
* Public `clone` methods should omit the throws clause.
* A better approach to object copying is to provide a *copy constructor* or *copy factory*.

### Item 14: Consider implementing `Comparable`

* Use of the relational operators `<` and `>` in compareTo methods is verbose and error-prone and no longer recommended.

## Chapter 4. Classes and Interfaces

### Item 15: Minimize the accessibility of classes and members

* Make each class or member as inaccessible as possible.
* Instance fields of public classes should rarely be public
* Classes with public mutable fields are not generally thread-safe.
* It is wrong for a class to have a `public` `static` `final` array field, or an accessor that returns such a field.

### Item 16: In public classes, use accessor methods, not public fields

* If a class is accessible outside its package, provide accessor methods.
* If a class is package-private or is a private nested class, there is nothing inherently wrong with exposing its data fields.

### Item 17: Minimize mutability

* Immutable objects are simple.
* Immutable objects are inherently thread-safe; they require no synchronization.
* Immutable objects can be shared freely.
* Not only can you share immutable objects, but they can share their internals.
* Immutable objects make great building blocks for other objects.
* Immutable objects provide failure atomicity for free.
* The major disadvantage of immutable classes is that they require a separate object for each distinct value.
* Classes should be immutable unless there’s a very good reason to make them mutable.
* If a class cannot be made immutable, limit its mutability as much as possible.
* Declare every field private final unless there’s a good reason to do otherwise.
* Constructors should create fully initialized objects with all of their invariants established.
