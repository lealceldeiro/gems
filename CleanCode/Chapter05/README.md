# Chapter 5: Formatting

You should take care that your code is nicely formatted. You should choose a set of simple rules that govern the format of your code, and then you should consistently apply those rules. If you are working on a team, then the team should agree to a single set of formatting rules and all members should comply. It helps to have an automated tool that can apply those formatting rules for you.

## The Purpose of Formatting

Code formatting is _important_. It is about communication, and communication is the professional developer’s first order of business.

## Vertical Formatting

Small files are usually easier to understand than large files are.

### The Newspaper Metaphor

In a source file, the name should be simple but explanatory. The topmost parts of the source file should provide the high-level concepts and algorithms. Detail should increase as we move downward, until at the end we find the lowest level functions and details in the source file.

### Vertical Openness Between Concepts

Nearly all code is read left to right and top to bottom. Each line represents an expression or a clause, and each group of lines represents a complete thought. Those thoughts should be separated from each other with blank lines.

### Vertical Density

If openness separates concepts, then vertical density implies close association. So lines of code that are tightly related should appear vertically dense.

### Vertical Distance

Concepts that are closely related should be kept vertically close to each other. Closely related concepts should not be separated into different files unless you have a very good reason. This is one of the reasons that protected variables should be avoided.

For those concepts that are so closely related that they belong in the same source file, their vertical separation should be a measure of how important each is to the understandability of the other.

#### Variable Declarations

Variables should be declared as close to their usage as possible. Control variables for loops should usually be declared within the loop statement.

**Instance variables**, on the other hand, should be declared at the top of the class. This should not increase the vertical distance of these variables, because in a well-designed class, they are used by many, if not all, of the methods of the class.

#### Dependent Functions.

If one function calls another, they should be vertically close, and the caller should be above the callee, if at all possible. This gives the program a natural flow. If the convention is followed reliably, readers will be able to trust that function definitions will follow shortly after their use.

#### Conceptual Affinity

Certain bits of code _want_ to be near other bits. They have a certain conceptual affinity. The stronger that affinity, the less vertical distance there should be between them.

### Vertical Ordering

In general we want function call dependencies to point in the downward direction. That is, a function that is called should be below a function that does the calling. This creates a nice flow down the source code module from high level to low level (This is the exact opposite of languages like Pascal, C, and C++ that enforce functions to be defined, or at least declared, before they are used).

## Horizontal Formatting

