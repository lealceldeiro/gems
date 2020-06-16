# Part II: A Worked Example

## Chapter 7: Step Definitions: On the Inside

Transforms help with maintainability by removing annoying duplicate code to process captured arguments from steps.

Java code that supports the step definitions can be factored out into separate classes.

It’s good practice to organize step definition files with one file per domain entity.

You can pass state between steps using helper classes that are instantiated and managed by Cucumber’s integration with one of several dependency injection frameworks.

## Chapter 8: Support Code

Four criteria for a simple design, according to Kent Beck (*Extreme Programming Explained*)

* Passes all the tests
* Reveals all the intention
* Contains no duplication
* Uses the fewest number of classes or methods

Working outside-in with Cucumber blurs the lines between testing and development. Always be ready to learn something new about the problem domain, whether you’re deciding on the wording in a Cucumber scenario or choosing the parameters for a method.

By taking care to craft a clean interface between your tests and the system underneath, you’ll end up with tests that can easily evolve with the system’s changing requirements.

Cucumber’s hooks can be used to invoke Java code before and after each scenario or to run them before specific scenarios using tags.

## Chapter 9: Message Queues and Asynchronous Components
