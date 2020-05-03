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

### Producer-Consumer

One or more producer threads create some work and place it in a buffer or queue. One or more consumer threads acquire that work from the queue and complete it. The queue between the producers and consumers is a _bound resource_.

### Readers-Writers

When you have a shared resource that primarily serves as a source of information for readers, but which is occasionally updated by writers, throughput is an issue.

### Dining Philosophers

Takins as reference the [Dining philosophers problem](https://en.wikipedia.org/wiki/Dining_philosophers_problem), replace philosophers with threads and forks with resources and this problem is similar to many enterprise applications in which processes compete for resources. Unless carefully designed, systems that compete in this way can experience deadlock, livelock, throughput, and efficiency degradation.

*Recommendation*: Learn these basic algorithms and understand their solutions.

## Beware Dependencies Between Synchronized Methods

Dependencies between synchronized methods cause subtle bugs in concurrent code.

*Recommendation*: Avoid using more than one method on a shared object. Otherwise there are three ways to make the code correct:

* **Client-Based Locking**: Have the client lock the server before calling the first method and make sure the lock’s extent includes code calling the last method.

* **Server-Based Locking**: Within the server create a method that locks the server, calls all the methods, and then unlocks. Have the client call the new method.

* **Adapted Server**: Create an intermediary that performs the locking. This is an example of server-based locking, where the original server cannot be changed.

## Keep Synchronized Sections Small

We want to design our code with as few critical sections as possible.

Extending synchronization beyond the minimal critical section increases contention and degrades performance.

## Writing Correct Shut-Down Code Is Hard

Think about shut-down early and get it working early. It’s going to take longer than you expect. Review existing algorithms because this is probably harder than you think.

## Testing Threaded Code

*Recommendation*: Write tests that have the potential to expose problems and then run them frequently, with different programatic configurations and system configurations and load. If tests ever fail, track down the failure. Don’t ignore a failure just because the tests pass on a subsequent run. So:

* Treat spurious failures as candidate threading issues (Do not ignore system failures as one-offs).
* Get your nonthreaded code working first (Do not try to chase down nonthreading bugs and threading bugs at the same time. Make sure your code works outside of threads).
* Make your threaded code pluggable (so that you can run it in various configurations: one, two, various threads, for different number of iterations, etc).
* Make your threaded code tunable.
* Run with more threads than processors (the more frequently your tasks swap, the more likely you’ll encounter code that is missing a critical section or causes deadlock).
* Run on different platforms (Multithreaded code behaves differently in different environments. Run your threaded code on all target platforms early and often).
* Instrument your code to try and force failures.
