# Chapter 12: Testing Concurrent Programs

Most tests of concurrent classes fall into one or both of the classic categories of _safety_(nothing bad ever happens) and _liveness_(“something good eventually happens).

Unfortunately, test code can introduce timing or synchronization artifacts that can mask
bugs that might otherwise manifest themselves (Bugs that disappear when you add debugging or test code are playfully called Heisenbugs).

## 12.1 Testing for correctness

Developing unit tests for a concurrent class starts with the same analysis as for a sequential class—identifying invariants and postconditions that are amenable to mechanical checking.

### 12.1.1 Basic unit tests

The most basic unit tests for classes involved in multithreading scenarions are similar to what we’d use in a sequential context—create a bounded buffer, call its methods, and assert postconditions and invariants.

Including a set of sequential tests in your test suite is often helpful, since they can disclose when a problem is not related to concurrency issues before you start looking for data races.

### 12.1.2 Testing blocking operations

Tests of essential concurrency properties require introducing more than one thread. Most testing frameworks are not very concurrency-friendly: they rarely include facilities to create threads or monitor them to ensure that they do not die unexpectedly. If a helper thread created by a test case discovers a failure, the framework usually does not know with which test the thread is associated, so some work may be required to relay success or failure information back to the main test runner thread so it can be reported.

If a method is supposed to block under certain conditions, then a test for that behavior should succeed only if the thread does _not_ proceed. Testing that a method blocks is similar to testing that a method throws an exception; if the method returns normally, the test has failed.

The result of `Thread.getState` should not be used for concurrency control, and is of limited usefulness for testing—its primary utility is as a source of debugging information.

### 12.1.3 Testing safety

The challenge to constructing effective safety tests for concurrent classes is identifying easily checked properties that will, with high probability, fail if something goes wrong, while at the same time not letting the failure-auditing code limit concurrency artificially. It is best if checking the test property does not require any synchronization.

For testing purposes, rather than using a general-purpose random generators, it is better to use simple pseu-dorandom functions. High-quality randomness is not needed. The `xor-Shift` function is among the cheapest medium-quality random number functions:
```
static int xorShift(int y) {
  y ^= (y << 6);
  y ^= (y >>> 21);
  y ^= (y << 7);
  return y;
}
```

Tests should be run on multiprocessor systems to increase the diversity of potential interleavings. However, having more than a few CPUs does not necessarily make tests more effective. To maximize the chance of detecting timing-sensitive data races, there should be more active threads than CPUs, so that at any given time some threads are running and some are switched out, thus reducing the predicatability of interactions between threads.

In tests that run until they complete a fixed number of operations, it is possible that the test case will never finish if the code being tested encounters an exception due to a bug. The most common way to handle this is to have the test framework abort tests that do not terminate within a certain amount of time. This problem is not unique to testing concurrent classes; sequential tests must also distinguish between long-running and infinite loops.

### 12.1.4 Testing resource management

Any object that holds or manages other objects should not continue to maintain references to those objects longer than necessary. Such storage leaks prevent garbage collectors from reclaiming memory (or threads, file handles, sockets, database connections, or other limited resources) and can lead to resource exhaustion and application failure.

Undesirable memory retention can be easily tested with heap-inspection tools that measure application memory usage; a variety of commercial and open-source heap-profiling tools can do this.

### 12.1.5 Using callbacks

Callbacks to client-provided code can be helpful in constructing test cases; callbacks are often made at known points in an object’s lifecycle that are good opportunities to assert invariants.

### 12.1.6 Generating more interleavings

Testing on a variety of systems with different processor counts, operating systems, and processor architectures can disclose problems that might not occur on all systems.

A useful trick for increasing the number of interleavings, and therefore more effectively exploring the state space of your programs, is to use `Thread.yield` to encourage more context switches during operations that access shared state.

## 12.2 Testing for performance

Performance tests are often extended versions of functionality tests. In fact, it is almost always worthwhile to include some basic functionality testing within performance tests to ensure that you are not testing the performance of broken code.

Performance tests seek to measure end-to-end performance metrics for representative use cases.

A common secondary goal of performance testing is to select sizings empirically for various bounds—numbers of threads, buffer capacities, and so on. While these values might turn out to be sensitive enough to platform characteristics (such as processor type or even processor stepping level, number of CPUs, or memory size) to require dynamic configuration, it is equally common that reasonable choices for these values work well across a wide range of systems.

### 12.2.3 Measuring responsiveness

Sometimes it is more important to know how long an individual action might take to complete, and in this case we want to measure the _variance_ of service time.

Histograms of task completion times are normally the best way to visualize variance in service time. Variances are only slightly more difficult to measure than averages—you need to keep track of per-task completion times in addition to aggregate completion time.

Unless threads are continually blocking anyway because of tight synchronization requirements, nonfair semaphores provide much better throughput and fair semaphores provides lower variance. Because the results are so dramatically different, Semaphore forces its clients to decide which of the two factors to optimize for.

## 12.3 Avoiding performance testing pitfalls

### 12.3.1 Garbage collection

The timing of garbage collection is unpredictable, so there is always the possibility that the garbage collector will run during a measured test run. If a test program performs N iterations and triggers no garbage collection but iteration N + 1 would trigger a garbage collection, a small variation in the size of the run could have a big (but spurious) effect on the measured time per iteration.

There are two strategies for preventing garbage collection from biasing your results. One is to ensure that garbage collection does not run at all during your test (you can invoke the JVM with -verbose:gc to find out); alternatively, you can make sure that the garbage collector runs a number of times during your run so that the test program adequately reflects the cost of ongoing allocation and garbage collection. The latter strategy is often better—it requires a longer test and is more likely to reflect real-world performance.

### 12.3.2 Dynamic compilation

When a class is first loaded, the JVM executes it by interpreting the bytecode. At some point, if a method is run often enough, the dynamic compiler kicks in and converts it to machine code; when compilation completes, it switches from interpretation to direct execution.

The timing of compilation is unpredictable. Your timing tests should run only after all code has been compiled.

Allowing the compiler to run during a measured test run can bias test results in two ways: compilation consumes CPU resources, and measuring the run time of a combination of interpreted and compiled code is not a meaningful performance metric.

One way to prevent compilation from biasing your results is to run your program for a long time (at least several minutes) so that compilation and interpreted execution represent a small fraction of the total run time. Another approach is to use an unmeasured “warm-up” run, in which your code is executed enough to be fully compiled when you actually start timing.

Running the same test several times in the same JVM instance can be used to validate the testing methodology. The first group of results should be discarded as warm-up; seeing inconsistent results in the remaining groups suggests that the test should be examined further to determine why the timing results are not repeatable.

### 12.3.3 Unrealistic sampling of code paths

Runtime compilers use profiling information to help optimize the code being compiled. The JVM is permitted to use information specific to the execution in order to produce better code, which means that compiling method `M` in one program may generate different code than compiling `M` in another.

It is important that the test programs not only adequately approximate the usage patterns of a typical application, but also approximate the set of code paths used by such an application. Otherwise, a dynamic compiler could make special optimizations to a purely single-threaded test program that could not be applied in real applications containing at least occasional parallelism.

### 12.3.4 Unrealistic degrees of contention

If `N` threads are fetching tasks from a shared work queue and executing them, and the tasks are compute-intensive and long-running (and do not access shared data very much), there will be almost no contention; throughput is dominated by the availability of CPU resources.

If the tasks are very short-lived, there will be a lot of contention for the work queue and throughput is dominated by the cost of synchronization.

Concurrent performance tests should try to approximate the thread-local computation done by a typical application in addition to the concurrent coordination under study.

If the work done for each task in an application is significantly different in nature or scope from the test program, it is easy to arrive at unwarranted conclusions about where the performance bottlenecks lie.

### 12.3.5 Dead code elimination

Writing effective performance tests requires tricking the optimizer into not optimizing away your benchmark as dead code. This requires every computed result to be used somehow by your program—in a way that does not require synchronization or substantial computation.

## 12.4 Complementary testing approaches

The goal of testing is not so much to find errors as it is to increase confidence that the code works as expected.

Different QA methodologies are more effective at finding some types of defects and less effective at finding others. By employing complementary testing methodologies such as code review and static analysis, you can achieve greater confidence than you could with any single approach.

### 12.4.1 Code review

Taking the time to have someone else review the code is almost always worthwhile

It not only can it find errors, but it often improves the quality of comments describing the implementation details, thus reducing future maintenence cost and risk.

### 12.4.2 Static analysis tools

Static code analysis is the process of analyzing code without executing it, and code auditing tools can analyze classes to look for instances of common _bug patterns_.

## Summary

Testing concurrent programs for correctness can be extremely challenging because many of the possible failure modes of concurrent programs are low-probability events that are sensitive to timing, load, and other hard-to-reproduce conditions. Further, the testing infrastructure can introduce additional synchronization or timing constraints that can mask concurrency problems in the code being tested. Testing concurrent programs for performance can be equally challenging; Java programs are more difficult to test than programs written in statically compiled languages like C, because timing measurements can be affected by dynamic compilation, garbage collection, and adaptive optimization.
