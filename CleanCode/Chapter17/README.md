# Chapter 17: Smells and Heuristics

## Comments

### C1: Inappropriate Information

It is inappropriate for a comment to hold information better held in a different kind of system such as your source code control system, your issue tracking system, or any other record-keeping system. Comments should be reserved for technical notes about the code and design.

### C2: Obsolete Comment

A comment that has gotten old, irrelevant, and incorrect is obsolete. They become floating islands of irrelevance and misdirection in the code.

### C3: Redundant Comment

A comment is redundant if it describes something that adequately describes itself. Comments should say things that the code cannot say for itself.

### C4: Poorly Written Comment

Choose your words carefully. Use correct grammar and punctuation. Don’t ramble. Don’t state the obvious. Be brief.

### C5: Commented-Out Code

That code sits there and rots, getting less and less relevant with every passing day. It calls functions that no longer exist. It uses variables whose names have changed. It follows conventions that are long obsolete. It pollutes the modules that contain it and distracts the people who try to read it. It can be safely deleted! If it is needed again, the source code control system can be used to get it back.

## Environment

### E1: Build Requires More Than One Step

You should be able to check out the system with one simple command and then issue one other simple command to build it.

### E2: Tests Require More Than One Step

You should be able to run all the unit tests with just one command.

## Functions

### F1: Too Many Arguments

Functions should have a small number of arguments. No argument is best, followed by one, two, and three. More than three is very questionable and should be avoided with prejudice.

### F2: Output Arguments

Output arguments are counterintuitive. Readers expect arguments to be inputs, not outputs.

If your function must change the state of something, have it change the state of the object it is called on.

### F3: Flag Arguments

Boolean arguments loudly declare that the function does more than one thing. They are confusing and should be eliminated.

### F4: Dead Function

Methods that are never called should be discarded. Keeping dead code around is wasteful.

## General

### G1: Multiple Languages in One Source File

The ideal is for a source file to contain one, and only one, language. Realistically, we will probably have to use more than one. But we should take pains to minimize both the number and extent of extra languages in our source files.

### G2: Obvious Behavior Is Unimplemented

Following “The Principle of Least Surprise,” any function or class should implement the behaviors that another programmer could reasonably expect.

When an obvious behavior is not implemented, readers and users of the code can no longer depend on their intuition about function names. They lose their trust in the original author and must fall back on reading the details of the code.

### G3: Incorrect Behavior at the Boundaries

Every boundary condition, every corner case, every quirk and exception represents something that can confound an elegant and intuitive algorithm. *Don’t rely on your intuition*. Look for every boundary condition and write a test for it.

### G4: Overridden Safeties

It is risky to override safeties.

### G5: Duplication!

Find and eliminate duplication wherever you can.

Every time you see duplication in the code, it represents a missed opportunity for abstraction. That duplication could probably become a subroutine or perhaps another class outright. By folding the duplication into such an abstraction, you increase the vocabulary of the language of your design. Other programmers can use the abstract facilities you create. Coding becomes faster and less error prone because you have raised the abstraction level.

### G6: Code at Wrong Level of Abstraction

It is important to create abstractions that separate higher level general concepts from lower level detailed concepts. Sometimes we do this by creating abstract classes to hold the higher level concepts and derivatives to hold the lower level concepts. When we do this, we need to make sure that the separation is complete. We want *all* the lower level concepts to be in the derivatives and *all* the higher level concepts to be in the base class.

### G7: Base Classes Depending on Their Derivatives

The most common reason for partitioning concepts into base and derivative classes is so that the higher level base class concepts can be independent of the lower level derivative class concepts. Therefore, when we see base classes mentioning the names of their derivatives, we suspect a problem. In general, base classes should know nothing about their derivatives.

Some exceptions to this are, for example, when the number of derivatives is strictly fixed, and the base class has code that selects between the derivatives.

### G8: Too Much Information

Hide your data. Hide your utility functions. Hide your constants and your temporaries. Don’t create classes with lots of methods or lots of instance variables. Don’t create lots of protected variables and functions for your subclasses. Concentrate on keeping interfaces very tight and very small. Help keep coupling low by limiting information.

### G9: Dead Code

Dead code is not completely updated when designs change. It still *compiles*, but it does not follow newer conventions or rules. It was written at a time when the system was *different*. When you find dead code, do the right thing: delete it from the system.

### G10: Vertical Separation

Variables and function should be defined close to where they are used. Local variables should be declared just above their first usage and should have a small vertical scope.

Private functions should be defined just below their first usage. Private functions belong to the scope of the whole class, but we’d still like to limit the vertical distance between the invocations and definitions.

### G11: Inconsistency

If you do something a certain way, do all similar things in the same way. This goes back to the principle of least surprise. Be careful with the conventions you choose, and once chosen, be careful to continue to follow them.

### G12: Clutter

Keep your source files clean, well organized, and free of clutter.

### G13: Artificial Coupling

Things that don’t depend upon each other should not be artificially coupled.

In general an artificial coupling is a coupling between two modules that serves no direct purpose. It is a result of putting a variable, constant, or function in a temporarily convenient, though inappropriate, location.

### G14: Feature Envy

The methods of a class should be interested in the variables and functions of the class they belong to, and not the variables and functions of other classes. When a method uses accessors and mutators of some other object to manipulate the data within that object, then it _envies_ the scope of the class of that other object. It wishes that it were inside that other class so that it could have direct access to the variables it is manipulating.

### G15: Selector Arguments

Not only is the purpose of a selector argument difficult to remember, each selector argument combines many functions into one. Selector arguments are just a lazy way to avoid splitting a large function into several smaller functions. In general it is better to have many functions than to pass some code into a function to select the behavior.

### G16: Obscured Intent

Run-on expressions, Hungarian notation, and magic numbers all obscure the author’s intent and should be avoided in order to get the code to be as expressive as possible.

### G17: Misplaced Responsibility

Code should be placed where a reader would naturally expect it to be.

### G18: Inappropriate Static

In general you should prefer nonstatic methods to static methods. When in doubt, make the function nonstatic. If you really want a function to be static, make sure that there is no chance that you’ll want it to behave polymorphically.

### G19: Use Explanatory Variables

One of the more powerful ways to make a program readable is to break the calculations up into intermediate values that are held in variables with meaningful names. More explanatory variables are generally better than fewer.

### G20: Function Names Should Say What They Do

If you have to look at the implementation (or documentation) of the function to know what it does, then you should work to find a better name or rearrange the functionality so that it can be placed in functions with better names.

### G21: Understand the Algorithm

Before you consider yourself to be done with a function, make sure you understand how it works. It is not good enough that it passes all the tests. You must know 10 that the solution is correct.

Often the best way to gain this knowledge and understanding is to refactor the function into something that is so clean and expressive that it is obvious how it works.

### G22: Make Logical Dependencies Physical

If one module depends upon another, that dependency should be physical, not just logical. The dependent module should not make assumptions (in other words, logical dependencies) about the module it depends upon. Rather it should explicitly ask that module for all the information it depends upon.

### G23: Prefer Polymorphism to If/Else or Switch/Case

This heuristic is here to remind us to consider polymorphism before using a `switch`.

The cases where functions are more volatile than types are relatively rare. So every `switch` statement should be suspect.

There may be no more than one `switch` statement for a given type of selection. The place of other such `switch` statements in the rest of the system.

### G24: Follow Standard Conventions

Every team should follow a coding standard based on common industry norms. This coding standard should specify things like where to declare instance variables; how to name classes, methods, and variables; where to put braces; and so on.

Everyone on the team should follow these conventions. This means that each team member must be mature enough to realize that it doesn’t matter where you put your braces so long as you all agree on where to put them.

### G25: Replace Magic Numbers with Named Constants

The term “Magic Number” does not apply only to numbers. It applies to any token that has a value that is not self-describing.

In general it is a bad idea to have raw numbers in your code. You should hide them behind well-named constants. However some constants are so easy to recognize that they don’t always need a named constant to hide behind so long as they are used in conjunction with very self-explanatory code. i.e.:

```java
int dailyPay = hourlyRate * 8;
```

Also, there are some formulae in which constants are simply better written as raw numbers. i.e.:

```java
double circumference = radius * Math.PI * 2;
```

### G26: Be Precise

When you make a decision in your code, make sure you make it precisely. Know why you have made it and how you will deal with any exceptions. Don’t be lazy about the precision of your decisions.

Ambiguities and imprecision in code are either a result of disagreements or laziness. In either case they should be eliminated.

### G27: Structure over Convention

Enforce design decisions with structure over convention. Naming conventions are good, but they are inferior to structures that force compliance.

### G28: Encapsulate Conditionals

Boolean logic is hard enough to understand without having to see it in the context of an if or while statement. Extract functions that explain the intent of the conditional.i.e.:

```java
if (shouldBeDeleted(timer))
```

is preferable to

```java
if (timer.hasExpired() && !timer.isRecurrent())
```

### G29: Avoid Negative Conditionals

When possible, conditionals should be expressed as positives. i.e:

```java
if (buffer.shouldCompact())
```

is preferable to

```java
if (!buffer.shouldNotCompact())
```

### G30: Functions Should Do One Thing

It is often tempting to create functions that have multiple sections that perform a series of operations. Functions of this kind do more than *one thing*, and should be converted into many smaller functions, each of which does *one thing*.

### G31: Hidden Temporal Couplings

Temporal couplings are often necessary, but you should not hide the coupling. Structure the arguments of your functions such that the order in which they should be called is obvious.

### G32: Don’t Be Arbitrary

Have a reason for the way you structure your code, and make sure that reason is communicated by the structure of the code. If a structure appears arbitrary, others will feel empowered to change it. If a structure appears consistently throughout the system, others will use it and preserve the convention.

### G33: Encapsulate Boundary Conditions

Boundary conditions are hard to keep track of. Put the processing for them in one place. Don’t let them leak all over the code. We don’t want swarms of `+1`s and `-1`s scattered hither and yon.


### G34: Functions Should Descend Only One Level of Abstraction

The statements within a function should all be written at the same level of abstraction, which should be one level below the operation described by the name of the function. This may be the hardest of these heuristics to interpret and follow. Though the idea is plain enough, humans are just far too good at seamlessly mixing levels of abstraction.

### G35: Keep Configurable Data at High Levels

If you have a constant such as a default or configuration value that is known and expected at a high level of abstraction, do not bury it in a low-level function. Expose it as an argument to that low-level function called from the high-level function.

### G36: Avoid Transitive Navigation

In general we don’t want a single module to know much about its collaborators. More specifically, if `A` collaborates with `B`, and `B` collaborates with `C`, we don’t want modules that use `A` to know about `C`. (For example, we don’t want `a.getB().getC().doSomething();`.)

If many modules used some form of the statement `a.getB().getC()`, then it would be difficult to change the design and architecture to interpose a `Q` between `B` and `C`. You’d have to find every instance of `a.getB().getC()` and convert it to `a.getB().getQ().getC()`.

This is how architectures become rigid. Too many modules know too much about the architecture.

Rather we want our immediate collaborators to offer all the services we need. We should not have to roam through the object graph of the system, hunting for the method we want to call.

## Java

### J1: Avoid Long Import Lists by Using Wildcards

*(arguable from my perspective)*

### J2: Don’t Inherit Constants

Don’t use inheritance as a way to cheat the scoping rules of the language. Use a static import instead.

### J3: Constants versus Enums

Don’t keep using the old trick of `public static final int`s. The meaning of `int`s can get lost. The meaning of `enum`s cannot, because they belong to an enumeration that is named.

## Names

### N1: Choose Descriptive Names

Don’t be too quick to choose a name. Make sure the name is descriptive. You need to take the time to choose them wisely and keep them relevant. Names are too important to treat carelessly.

### N2: Choose Names at the Appropriate Level of Abstraction

Don’t pick names that communicate implementation; choose names that reflect the level of abstraction of the class or function you are working in.

### N3: Use Standard Nomenclature Where Possible

Names are easier to understand if they are based on existing convention or usage.

### N4: Unambiguous Names

Choose names that make the workings of a function or variable unambiguous.

### N5: Use Long Names for Long Scopes

The length of a name should be related to the length of the scope. You can use very short variable names for tiny scopes, but for big scopes you should use longer names.

### N6: Avoid Encodings

Names should not be encoded with type or scope information. Prefixes such as `m_` or `f` are useless in today’s environments. Also project and/or subsystem encodings such as `vis_` (for visual imaging system) are distracting and redundant. Again, today’s environments provide all that information without having to mangle the names. Keep your names free of Hungarian pollution.

### N7: Names Should Describe Side-Effects

Names should describe everything that a function, variable, or class is or does. Don’t hide side effects with a name. Don’t use a simple verb to describe a function that does more than just that simple action.

## Tests

### T1: Insufficient Tests

A test suite should test everything that could possibly break. The tests are insufficient so long as there are conditions that have not been explored by the tests or calculations that have not been validated.

### T2: Use a Coverage Tool!

Coverage tools reports gaps in your testing strategy. They make it easy to find modules, classes, and functions that are insufficiently tested.

### T3: Don’t Skip Trivial Tests

They are easy to write and their documentary value is higher than the cost to produce them.

### T4: An Ignored Test Is a Question about an Ambiguity

Sometimes we are uncertain about a behavioral detail because the requirements are unclear. We can express our question about the requirements as a test that is commented out, or as a test that annotated with @Ignore . Which you choose depends upon whether the ambiguity is about something that would compile or not.

### T5: Test Boundary Conditions

Take special care to test boundary conditions. We often get the middle of an algorithm right but misjudge the boundaries.

### T6: Exhaustively Test Near Bugs

Bugs tend to congregate. When you find a bug in a function, it is wise to do an exhaustive test of that function. You’ll probably find that the bug was not alone.

### T7: Patterns of Failure Are Revealing

Sometimes you can diagnose a problem by finding patterns in the way the test cases fail. This is another argument for making the test cases as complete as possible. Complete test cases, ordered in a reasonable way, expose patterns.

### T8: Test Coverage Patterns Can Be Revealing

Looking at the code that is or is not executed by the passing tests gives clues to why the failing tests fail.

### T9: Tests Should Be Fast

A slow test is a test that won’t get run. When things get tight, it’s the slow tests that will be dropped from the suite. So do what you must to keep your tests fast.
