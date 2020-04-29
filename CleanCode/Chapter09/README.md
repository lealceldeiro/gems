# Chapter 9: Unit Tests

## The Three Laws of TDD

**First Law**: You may not write production code until you have written a failing unit test.

**Second Law**: You may not write more of a unit test than is sufficient to fail, and not compiling is failing.7

**Third Law**: You may not write more production code than is sufficient to pass the currently failing test.

## Keeping Tests Clean

Test code is just as important as production code. It is not a second-class citizen. It requires thought, design, and care. It must be kept as clean as production code.

### Tests Enable the -ilities

It is unit tests that keep our code flexible, maintainable, and reusable.

## Clean Tests

What makes a clean test? Three things. Readability, readability, and readability.

What makes tests readable? Clarity, simplicity, and density of expression. In a test you want to say a lot with as few expressions as possible.

### Domain-Specific Testing Language

Rather than using the APIs that programmers use to manipulate the system, we build up a set of functions and utilities that make use of those APIs and that make the tests more convenient to write and easier to read. These functions and utilities become a specialized API used by the tests. They are a testing _language_ that programmers use to help themselves to write their tests and to help those who must read those tests later on.

### A Dual Standard

The code within the testing API does have a different set of engineering standards than production code. It must still be simple, succinct, and expressive, but it need not be as efficient as production code.

## One Assert per Test

Those tests with only one assertion come to a single conclusion that is quick and easy to understand, however the takeaway here is that the number of asserts in a test ought to be minimized.

### Single Concept per Test

A better rule is that we want to test a single concept in each test function.

## F.I.R.S.T.

Clean tests follow five other rules:

* **Fast**: Tests should be fast. They should run quickly.
* **Independent**: Tests should not depend on each other. One test should not set up the conditions for the next test.
* **Repeatable**: Tests should be repeatable in any environment. You should be able to run the tests in the production environment, in the QA environment, and on your personal device.
* **Self-Validating**: The tests should have a boolean output. Either they pass or fail. You should not have to read through a log file to tell whether the tests pass. You should not have to manually compare two different text files to see whether the tests pass.
* **Timely**: The tests need to be written in a timely fashion. Unit tests should be written just before the production code that makes them pass.
