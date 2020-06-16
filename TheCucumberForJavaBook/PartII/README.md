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

When you add asynchronous behavior to a system, you need to make a concerted effort to tame the random effects that it can have on your tests. Build your tests with a knowledge of how the system works and introduce synchronization points where timing issues are likely to arise.

Using sleeps in your steps is not a good way to tackle these timing issues, because it makes your tests slow and doesn’t solve the reliability problem: if the system changes and becomes slower, your sleep may not be long enough and the test will start to break again.

The best solution is to listen for events broadcast by the system and pause at the appropriate points in the scenario until those events have been received. That way, you minimize the amount of time the tests waste waiting for the system.

The next best solution is to use sampling to repeatedly poll the system, looking for an expected change of state. This approach works in most circumstances, but you need to take care, especially when the outcome you’re looking for at the end of the scenario looks just the same as at an earlier time in the scenario.
