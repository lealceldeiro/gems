# Chapter 13: Concurrency

## Concurrency Defense Principles

### Single Responsibility Principle

Concurrency design is complex enough to be a reason to change in it’s own right and therefore deserves to be separated from the rest of the code.

*Recommendation*: Keep your concurrency-related code separate from other code.

### Corollary: Limit the Scope of Data

*Recommendation*: Take data encapsulation to heart; severely limit the access of any data that may be shared.

### Corollary: Use Copies of Data

A good way to avoid shared data is to avoid sharing the data in the first place. In some situations it is possible to copy objects and treat them as read-only. In other cases it might be possible to copy objects, collect results from multiple threads in these copies and then merge the results in a single thread.

### Corollary: Threads Should Be as Independent as Possible

Consider writing your threaded code such that each thread exists in its own world, sharing no data with any other thread. Each thread processes one client request, with all of its required data coming from an unshared source and stored as local variables. This makes each of those threads behave as if it were the only thread in the world and there were no synchronization requirements.

*Recommendation*: Attempt to partition data into independent subsets than can be operated on by independent threads, possibly in different processors.

## Know Your Library

Things to consider when writing threaded code in Java 5 or later:

* Use the provided thread-safe collections.
* Use the executor framework for executing unrelated tasks.
* Use nonblocking solutions when possible.
* Several library classes are not thread safe.

### Thread-Safe Collections

*Recommendation*: Review the classes available to you. In the case of Java, become familiar with `java.util.concurrent`, `java.util.concurrent.atomic`, `java.util.concurrent.locks`.

## Know Your Execution Models
