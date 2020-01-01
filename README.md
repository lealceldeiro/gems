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

### Item 18: Favor composition over inheritance

* Unlike method invocation, inheritance violates encapsulation.

### Item 19: Design and document for inheritance or else prohibit it

* The class must document its self-use of overridable methods.
* A class may have to provide hooks into its internal workings in the form of judiciously chosen protected methods.
* The only way to test a class designed for inheritance is to write subclasses.
* You must test your class by writing subclasses before you release it.
* Constructors must not invoke overridable methods (directly or indirectly).
* Neither `clone` nor `readObject` may invoke an overridable method, directly or indirectly.
* Designing a class for inheritance requires great effort and places substantial limitations on the class.
* The best solution to this problem is to prohibit subclassing in classes that are not designed and documented to be safely subclassed.

### Item 20: Prefer interfaces to abstract classes

* Existing classes can easily be retrofitted to implement a new interface.
* Interfaces are ideal for defining mixins.
* Interfaces allow for the construction of nonhierarchical type frameworks.
* Good documentation is absolutely essential in a skeletal implementation.

### Item 21: Design interfaces for posterity

* It is not always possible to write a default method that maintains all invariants of every conceivable implementation.
* In the presence of default methods, existing implementations of an interface may compile without error or warning but fail at runtime.
* It is still of the utmost importance to design interfaces with great care.
* While it may be possible to correct some interface flaws after an interface is released, you cannot count on it.

### Item 22: Use interfaces only to define types

* The constant interface pattern is a poor use of interfaces.

### Item 23: Prefer class hierarchies to tagged classes

* Tagged classes are verbose, error-prone, and inefficient.
* A tagged class is just a pallid imitation of a class hierarchy.

### Item 24: Favor static member classes over nonstatic

* If you declare a member class that does not require access to an enclosing instance, always put the `static` modifier in its declaration.

### Item 25: Limit source files to a single top-level class

* Never put multiple top-level classes or interfaces in a single source file.

## Chapter 5. Generics

### Item 26: Don’t use raw types

* If you use raw types, you lose all the safety and expressiveness benefits of generics.
* You lose type safety if you use a raw type such as `List`, but not if you use a parameterized type such as `List<Object>`.
* You can’t put any element (other than `null`) into a `Collection<?>`.
* You must use raw types in class literals.
* This is the preferred way to use the `instanceof` operator with generic types:
```
  if (o instanceof Set) {
  // Raw type
  Set<?> s = (Set<?>) o;
  // Wildcard type
  ...
}
```
### Item 27: Eliminate unchecked warnings

* Eliminate every unchecked warning that you can.
* If you can’t eliminate a warning, but you can prove that the code that provoked the warning is typesafe, then (and only then) suppress the warning with an `@SuppressWarnings("unchecked")` annotation.
* Always use the `SuppressWarnings` annotation on the smallest scope possible.
* Every time you use a `@SuppressWarnings("unchecked")` annotation, add a comment saying why it is safe to do so.

### Item 28: Prefer lists to arrays

### Item 29: Favor generic types

### Item 30: Favor generic methods

* The type parameter list, which declares the type parameters, goes between a method’s modifiers and its return type.
```
  // Generic method
  public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
    Set<E> result = new HashSet<>(s1);
    result.addAll(s2);
    return result;
  }
```
### Item 31: Use bounded wildcards to increase API flexibility

* For maximum flexibility, use wildcard types on input parameters that represent producers or consumers.
* **PECS** stands for producer-`extends`, consumer-`super`.
* Do not use bounded wildcard types as return types.
* If the user of a class has to think about wildcard types, there is probably something wrong with its API.
* If a type parameter appears only once in a method declaration, replace it with a wildcard. i.e, from the following two declarations, the second, is the preferrend one:
```
public static <E> void swap(List<E> list, int i, int j) {  // (1) not preferred, it's more complex for the API clients
  list.set(i, list.set(j, list.get(i)));
}

public static void swap(List<?> list, int i, int j) {      // (2) preferred, it's simpler to use by the API clients
  swapHelper(list, i, j);
}
// Private helper method for wildcard capture
private static <E> void swapHelper(List<E> list, int i, int j) {
  list.set(i, list.set(j, list.get(i)));
}
```
### Item 32: Combine generics and varargs judiciously

* It is unsafe to store a value in a generic varargs array parameter.
* The `SafeVarargs` annotation constitutes a promise by the author of a method that it is typesafe.
* It is unsafe to give another method access to a generic varargs parameter array.
* Use `@SafeVarargs` on every method with a varargs parameter of a generic or parameterized type.

### Item 33: Consider typesafe heterogeneous containers

## Chapter 6. Enums and Annotations

### Item 34: Use enums instead of `int` constants

* To associate data with enum constants, declare instance fields and write a constructor that takes the data and stores it in the fields.
* If you override the `toString` method in an `enum` type, consider writing a `fromString` method to translate the custom string representation back to the corresponding enum.
* Switches on enums are good for augmenting enum types with constant-specific behavior.
* Use enums any time you need a set of constants whose members are known at compile time.
* It is not necessary that the set of constants in an enum type stay fixed for all time.

### Item 35: Use instance fields instead of ordinals

* Never derive a value associated with an enum from its ordinal; store it in an instance field instead.

### Item 36: Use `EnumSet` instead of bit fields

* Just because an enumerated type will be used in sets, there is no reason to represent it with bit fields.

### Item 37: Use `EnumMap` instead of ordinal indexing

* It is rarely appropriate to use ordinals to index into arrays: use `EnumMap` instead.

### Item 38: Emulate extensible enums with interfaces

* While you cannot write an extensible enum type, you can emulate it by writing an interface to accompany a basic enum type that implements the interface.

### Item 39: Prefer annotations to naming patterns

* There is simply no reason to use naming patterns when you can use annotations instead.
* all programmers should use the predefined annotation types that Java provides.

### Item 40: Consistently use the `Override` annotation

* Use the `Override` annotation on every method declaration that you believe to override a superclass declaration.

### Item 41: Use marker interfaces to define types

* Marker interfaces define a type that is implemented by instances of the marked class; marker annotations do not.
* Another advantage of marker interfaces over marker annotations is that they can be targeted more precisely.
* The chief advantage of marker annotations over marker interfaces is that they are part of the larger annotation facility.
* If you find yourself writing a marker annotation type whose target is `ElementType.TYPE`, take the time to figure out whether it really should be an annotation type or whether a marker interface would be more appropriate.

## Chapter 7. Lambdas and Streams

### Item 42: Prefer lambdas to anonymous classes

* Omit the types of all lambda parameters unless their presence makes your program clearer.
* Lambdas lack names and documentation; if a computation isn’t self-explanatory, or exceeds a few lines, don’t put it in a lambda.
* You should rarely, if ever, serialize a lambda.
* Don’t use anonymous classes for function objects unless you have to create instances of types that aren’t functional interfaces.

### Item 43: Prefer method references to lambdas

* Where method references are shorter and clearer, use them; where they aren’t, stick with lambdas.

### Item 44: Favor the use of standard functional interfaces

* If one of the standard functional interfaces does the job, you should generally use it in preference to a
purpose-built functional interface.
* Don’t be tempted to use basic functional interfaces with boxed primitives instead of primitive functional interfaces.
* You should seriously consider writing a purpose-built functional interface in preference to using a standard one if:
  - It will be commonly used and could benefit from a descriptive name.
  - It has a strong contract associated with it.
  - It would benefit from custom default methods.
* Always annotate your functional interfaces with the `@FunctionalInterface` annotation.

### Item 45: Use streams judiciously

* Overusing streams makes programs hard to read and maintain.
* In the absence of explicit types, careful naming of lambda parameters is essential to the readability of stream pipelines.
* Using helper methods is even more important for readability in stream pipelines than in iterative code.
* Refrain from using streams to process `char` values.
* Refactor existing code to use streams and use them in new code only where it makes sense to do so.
* If you’re not sure whether a task is better served by streams or iteration, try both and see which works better.

### Item 46: Prefer side-effect-free functions in streams

* The `forEach` operation should be used only to report the result of a stream computation, not to perform the computation.
* It is customary and wise to statically import all members of `Collectors` because it makes stream pipelines more readable.

### Item 47: Prefer Collection to Stream as a return type

* `Collection` or an appropriate subtype is generally the best return type for a public, sequence-returning method.
* Do not store a large sequence in memory just to return it as a collection.

### Item 48: Use caution when making streams parallel

* Parallelizing a pipeline is unlikely to increase its performance if the source is from `Stream.iterate`, or the intermediate operation `limit` is used.
* Do not parallelize stream pipelines indiscriminately.
* Performance gains from parallelism are best on streams over `ArrayList`, `HashMap`, `HashSet`, and `ConcurrentHashMap`
instances; `arrays`; `int` ranges; and `long` ranges.
* Not only can parallelizing a stream lead to poor performance, including liveness failures; it can lead to incorrect results and unpredictable behavior (safety failures).
* Under the right circumstances, it is possible to achieve near-linear speedup in the number of processor cores simply by adding a parallel call to a stream pipeline.

## Chapter 8. Methods

### Item 49: Check parameters for validity

* The `Objects.requireNonNull` method, added in Java 7, is flexible and convenient, so there’s no reason to perform `null` checks manually anymore.

### Item 50: Make defensive copies when needed

* You must program defensively, with the assumption that clients of your class will do their best to destroy its
invariants.
* Date is obsolete and should no longer be used in new code.
* It is essential to make a *defensive copy* of each mutable parameter to the constructor.
* Defensive copies are made before checking the validity of the parameters, and the validity check is performed on the copies rather than on the originals.
* Do not use the clone method to make a defensive copy of a parameter whose type is subclassable by untrusted parties.
* Return defensive copies of mutable internal fields.

### Item 51: Design method signatures carefully

* Choose method names carefully.
* Don’t go overboard in providing convenience methods.
* Avoid long parameter lists.
* Long sequences of identically typed parameters are especially harmful.
* For parameter types, favor interfaces over classes.
* Prefer two-element enum types to boolean parameters.

### Item 52: Use overloading judiciously

* The choice of which overloading to invoke is made at compile time.
* Selection among overloaded methods is static, while selection among overridden methods is dynamic.
* Avoid confusing uses of overloading.
* A safe, conservative policy (to fulfil previous point) is never to export two overloadings with the same number of parameters.
* You can always give methods different names instead of overloading them.
* Do not overload methods to take different functional interfaces in the same argument position.

### Item 53: Use varargs judiciously

### Item 54: Return empty collections or arrays, not nulls

* Never return `null` in place of an empty array or collection.

### Item 55: Return optionals judiciously

* Never return a `null` value from an `Optional`-returning method: it defeats the entire purpose of the facility.
* Optionals are similar in spirit to checked exceptions.
* Container types, including collections, maps, streams, arrays, and optionals should not be wrapped in optionals.
* You should declare a method to return `Optional<T>` if it might not be able to return a result and clients will have to perform special processing if no result is returned.
* You should never return an optional of a boxed primitive type.

### Item 56: Write doc comments for all exposed API elements

* To document your API properly, you must precede every exported class, interface, constructor, method, and field declaration with a doc comment.
* The doc comment for a method should describe succinctly the contract between the method and its client.
* Doc comments should be readable both in the source code and in the generated documentation.
* No two members or constructors in a class or interface should have the same summary description.
* When documenting a generic type or method, be sure to document all type parameters.
* When documenting an enum type, be sure to document the constants.
* When documenting an annotation type, be sure to document any members.
* Whether or not a class or static method is thread-safe, you should document its thread-safety level.
* Read the web pages generated by the Javadoc utility.

## Chapter 9. General Programming

### Item 57: Minimize the scope of local variables

* The most powerful technique for minimizing the scope of a local variable is to declare it where it is first used.
* Nearly every local variable declaration should contain an initializer.
* Prefer for loops to while loops.
* Keep methods small and focused.

### Item 58: Prefer `for`-each loops to traditional `for` loops

### Item 59: Know and use the libraries

* By using a standard library, you take advantage of the knowledge of the experts who wrote it and the experience of those who used it before you.
* The random number generator of choice is now `ThreadLocalRandom` (For fork join pools and parallel streams, use `SplittableRandom`).
* Numerous features are added to the libraries in every major release, and it pays to keep abreast of these additions.
* Every programmer should be familiar with the basics of **`java.lang`**, **`java.util`**, and **`java.io`**, and their subpackages.
* The *collections* framework and the *streams* library should be part of every programmer’s basic toolkit, as should parts of the concurrency utilities in **`java.util.concurrent`**.

### Item 60: Avoid `float` and `double` if exact answers are required.

* The `float` and `double` types are particularly ill-suited for monetary calculations.
* Use `BigDecimal`, `int`, or `long` for monetary calculations.

### Item 61: Prefer primitive types to boxed primitives

* Applying the `==` operator to boxed primitives is almost always wrong.
* When you mix primitives and boxed primitives in an operation, the boxed primitive is auto-unboxed.
* *Autoboxing* reduces the verbosity, but not the danger, of using boxed primitives.
* When your program does *unboxing*, it can throw a `NullPointerException`.

### Item 62: Avoid strings where other types are more appropriate

* Strings are poor substitutes for other value types.
* Strings are poor substitutes for enum types.
* Strings are poor substitutes for aggregate types.
* Strings are poor substitutes for capabilities.

### Item 63: Beware the performance of string concatenation

* Using the string concatenation operator repeatedly to concatenate `n` strings requires time quadratic in `n`.
* To achieve acceptable performance (to avoid the case mentioned before), use a `StringBuilder` in place of a `String`.
* Don’t use the string concatenation operator to combine more than a few strings unless performance is irrelevant.
