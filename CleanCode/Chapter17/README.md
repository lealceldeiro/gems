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
