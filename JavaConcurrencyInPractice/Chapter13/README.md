# Chapter 13: Explicit Locks

## 13.1 `Lock` and `ReentrantLock`

Unlike intrinsic locking, `Lock` offers a choice of unconditional, polled, timed, and interruptible lock acquisition,
and all lock and unlock operations are explicit.

`Lock` implementations must provide the same memory-visibility semantics as intrinsic locks, but can differ in their
locking semantics, scheduling algorithms, ordering guarantees, and performance characteristics.

`ReentrantLock` implements `Lock`, providing the same mutual exclusion and memory-visibility guarantees as synchronized.

Acquiring a `ReentrantLock` has the same memory semantics as entering a synchronized block, and releasing
a `ReentrantLock` has the same memory semantics as exiting a synchronized block.

`Lock`s must be released in a `finally` block, otherwise, the lock would never be released if the guarded code were to
throw an exception.

A reason not to use `ReentrantLock` as a substitute for `synchronized` is that it is more “dangerous” because it doesn't
automatically clean up the lock when control leaves the guarded block.

### 13.1.1 Polled and timed lock acquisition

Using timed or polled lock acquisition (`tryLock`) lets you regain control if you cannot acquire all the required locks,
release the ones you did acquire, and try again (or at least log the failure and do something else).

### 13.1.2 Interruptible lock acquisition

Interruptible lock acquisition allows locking to be used within cancellable activities.

## 13.2 Performance considerations

When `ReentrantLock` was added in Java 5.0, it offered far better contended performance than intrinsic locking.

After Java 6 an improved algorithm is used for managing intrinsic locks, similar to that used by `ReentrantLock`, that
closes the scalability gap considerably.

Performance is a moving target; yesterday’s benchmark showing that X is faster than Y may already be out of date today.

## 13.3 Fairness

The `ReentrantLock` constructor offers a choice of two fairness options: create a _nonfair_ lock (the default) or a
_fair_ lock.

Threads acquire a fair lock in the order in which they requested it, whereas a nonfair lock permits barging: threads
requesting a lock can jump ahead of the queue of waiting threads if the lock happens to be available when it is
requested.

## 13.4 Choosing between `synchronized` and `ReentrantLock`

`ReentrantLock` is an advanced tool for situations where intrinsic locking is not practical. Use it if you need its
advanced features: timed, polled, or interruptible lock acquisition, fair queueing, or non-block-structured locking.
Otherwise, prefer `synchronized`.

## 13.5 Read-write locks

Read-write locks allow a resource to be accessed by multiple readers or a single writer at a time, but not both.

## Summary

Explicit `Lock`s offer an extended feature set compared to intrinsic locking, including greater flexibility in dealing
with lock unavailability and greater control over queueing behavior. But `ReentrantLock` is not a blanket substitute
for `synchronized`; use it only when you need features that `synchronized` lacks. Read-write locks allow multiple
readers to access a guarded object concurrently, offering the potential for improved scalability when accessing
read-mostly data structures.
