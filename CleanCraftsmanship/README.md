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

#### The Three Laws of TDD

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

#### The fourth law

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

**Stairstep tests**

Some tests are written just to force us to create classes or functions or other structures that we’re going to need.
Sometimes these tests are so simple that they assert nothing. Other times they assert something very naive. Often
these tests are superseded by more comprehensive tests later and can be safely deleted. We call these kinds of tests
_stairstep tests_ because they are like stairs that allow us to incrementally increase the complexity to the
appropriate level.

**Misplaced responsibility**

A design flaw in which the function that claims to perform a computation does not actually perform the computation.
The computation is performed elsewhere.

Rule 6: When the code feels wrong, fix the design before proceeding.

### Chapter 3: Advanced TDD

Rule 7: Exhaust the current simpler case before testing the next more complex case.

Rule 8: If you must implement too much to get the current test to pass, delete that test and write a simpler test
that you can more easily pass.

Rule 9: Follow a deliberate and incremental pattern that covers the test space.

Test doubles form a type hierarchy:

Dummies are the simplest. Stubs are dummies, spies are stubs, and mocks are spies. Fakes stand alone.

![Image of the Test Doubles type hierarchy](./test_doubles.png "Test Doubles type hierarchy")

Rule 10: Don’t include things in your tests that your tests don’t need.

Rule 11: Don’t use production data in your tests.

A dummy is an implementation that does nothing. Every method of the interface is implemented to do nothing. If a method
returns a value, then the value returned by the dummy will be as close as possible to null or zero.

A stub is a dummy that returns test-specific values in order to drive the system under test through the pathways being
tested.

A spy also returns test-specific values in order to drive the system under test through desired pathways. However,
a spy remembers what was done to it and allows the test to ask about it.

A mock is a spy and returns test-specific values in order to drive the system under test through desired pathways,
and it remembers what was done to it. However, a mock also knows what to expect and will pass or fail the test on the
basis of those expectations. In other words, the test assertions are written into the mock.

A fake is a different kind of test double entirely. A fake is a simulator. It is a test double that implements some
kind of rudimentary business rules (usually, by having some canned behavior/responses codede in its logic) so that the
tests that use that fake can select how the fake behaves.

_The TDD uncertainty principle_: To the extent you demand certainty, your tests will be inflexible. To the extent you
demand flexible tests, you will have diminished certainty.

### Chapter 4: Test Design

Rules for testing databases:

- 1: Don't test databases (test the queries).
- 2: Decouple the database from the business rules.

Rules for testing GUIs:

- Don’t test GUIs.
- Test everything but the GUI.
- The GUI is smaller than you think it is.

## Part I: Standards

## Part I: Ethics
