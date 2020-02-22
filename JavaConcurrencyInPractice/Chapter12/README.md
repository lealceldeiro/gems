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
