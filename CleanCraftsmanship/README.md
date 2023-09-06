# Clean Craftsmanship: Disciplines, Standards, and Ethics

Main notes taken from the book [Clean Craftsmanship: Disciplines, Standards, and Ethics](https://www.amazon.com/Clean-Craftsmanship-Disciplines-Standards-Ethics/dp/013691571X)

## Part I: The Disciplines

Engineering practices of XP (not all):

- Test-driven development (TDD)
- Refactoring
- Simple design
- Pairing (collaborative programming)
- Acceptance Tests, which is the most technical and engineering focused of the business practices of XP.

The essence of the TDD discipline is very simple. Small cycles and tests come first. Tests come first in everything.
Tests are written first. Tests are cleaned up first. In all activities, tests come first. And all activities are broken
down into the tiniest of cycles.

The goal of TDD is to create a trusted test suite. If the test suite passes, you should feel safe to deploy the code.

Refactoring is the discipline by which we manipulate poorly structured code into code with a better structure—without
affecting behavior

A well-done simple design is the first indication that separates an apprentice who knows the rules from a journeyman
who understands the principles.

Collaborative programming is the least technical and the least prescriptive. Nevertheless, it may be the most
important of the five disciplines, because the building of an effective team is both a rare and precious thing.

Acceptance testing is the discipline that ties the software development team to the business. The business purpose is
the specification of the desired behaviors of the system. Those behaviors are encoded into tests. If those tests pass,
the system behaves as specified.

### Chapter 2: Test-Driven Development

The essence of TDD entails the discipline to do the following:

- Create a test suite that enables refactoring and is trusted to the extent that passage implies deployability.
  That is, if the test suite passes, the system can be deployed.
- Create production code that is decoupled enough to be testable and refactorable.
- Create an extremely short-cycle feedback loop that maintains the task of writing programs with a stable rhythm and
  productivity.
- Create tests and production code that are sufficiently decoupled from each other so as to allow convenient
  maintenance of both, without the impediment of replicating changes between the two.

### The Three Laws of TDD

1. The First Law: Write no production code until you have first written a test that fails due to the lack of that
   production code.
2. The Second Law: Write no more of a test than is sufficient to fail or fail to compile. Resolve the failure by
   writing some production code.
3. The Third Law: Write no more production code than will resolve the currently failing test. Once the test passes,
   write more test code.

Benefits of applying these three laws:

- You will spend more time writing code that works and less time debugging code that doesn't.
- You will produce a set of nearly perfect low-level documentation.
- It is fun—or at least motivating.
- You will produce a test suite that will give you the confidence to deploy.
- You will create less-coupled designs.

### The fourth law

Refactoring: First you write a small amount of failing test code. Then you write a small amount of passing production
code. Then you clean up the mess you just made.

Rule 1: Write the test that forces you to write the code you already know you want to write.

Rule 2: Make it fail. Make it pass. Clean it up.

Rule 3: Don't go for the gold.

> When you go for the gold too early, you tend to miss all the details around the outside. Also, you tend to miss
> the simplifying opportunities that those ancillary details provide.

Rule 4: Write the simplest, most specific, the most *absurdly simple starting point* test that will fail.

Rule 5: Generalize where possible.

> As the tests get more specific, the code gets more generic.

## Part I: Standards

## Part I: Ethics
