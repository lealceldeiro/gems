# Chapter 16: The Java Memory Model

## 16.1 What is a memory model, and why would I want one?

The JMM specifies the minimal guarantees the JVM must make about when writes to variables become visible to other threads. It was designed to balance the need for predictability and ease of program development with the realities of implementing high-performance JVMs on a wide range of popular processor architectures.

### 16.1.1 Platform memory models

In a shared-memory multiprocessor architecture, each processor has its own cache that is periodically reconciled with main memory. Processor architectures provide varying degrees of cache coherence; some provide minimal guarantees that allow different processors to see different values for the same memory location at virtually any time.

An architecture’s memory model tells programs what guarantees they can expect from the memory system, and specifies the special instructions required (called memory barriers or fences) to get the additional memory coordination guarantees required when sharing data.

In order to shield the Java developer from the differences between memory models across architectures, Java provides its own memory model, and the JVM deals with the differences between the JMM and the underlying platform’s memory model by inserting memory barriers at the appropriate places.

### 16.1.2 Reordering

he various reasons why operations might be delayed or appear to execute out of order can all be grouped into the general category of _reordering_.

### 16.1.3 The Java Memory Model

The JMM defines a partial ordering called happens-before on all actions within the program. To guarantee that the thread executing action B can see the results of action A (whether or not A and B occur in different threads), there must be a happens-before relationship between A and B. In the absence of a happens-before ordering between two operations, the JVM is free to reorder them as it pleases.

A data race occurs when a variable is read by more than one thread, and written by at least one thread, but the reads and writes are not ordered by _happens-before_. A _correctly synchronized program_ is one with no data races; correctly synchronized programs exhibit sequential consistency, meaning that all actions within the program appear to happen in a fixed, global order.

The rules for happens-before are:

* **Program order rule**. Each action in a thread _happens-before_ every action in that thread that comes later in the program order.
* **Monitor lock rule**. An unlock on a monitor lock _happens-before_ every subsequent lock on that same monitor lock.
* **Volatile variable rule**. A write to a volatile field _happens-before_ every subsequent read of that same field.
* **Thread start rule**. A call to `Thread.start` on a thread _happens-before_ every action in the started thread.
* **Thread termination rule**. Any action in a thread _happens-before_ any other thread detects that thread has terminated, either by successfully return from `Thread.join` or by `Thread.isAlive` returning `false`.
* **Interruption rule**. A thread calling interrupt on another thread _happens-before_ the interrupted thread detects the interrupt (either by having `InterruptedException` thrown, or invoking `isInterrupted` or `interrupted`).
* **Finalizer rule**. The end of a constructor for an object _happens-before_ the start of the finalizer for that object.
* **Transitivity**. If `A` _happens-before_ `B`, and `B` _happens-before_ `C`, then `A` _happens-before_ `C`.

When two threads synchronize on _different locks_, we can’t say anything about the ordering of actions between them—there is no _happens-before_ relation between the actions in the two threads.

### 16.1.4 Piggybacking on synchronization

Because of the strength of the _happens-before_ ordering, you can sometimes piggyback on the visibility properties of an existing synchronization. This entails combining the program order rule for happens-before with one of the other ordering rules (usually the monitor lock or volatile variable rule) to order accesses to a variable not otherwise guarded by a lock. This technique is very sensitive to the order in which statements occur and is therefore quite fragile.

This technique is called “piggybacking” because it uses an existing _happens-before_ ordering that was created for some other reason to ensure the visibility of object `X`, rather than creating a _happens-before_ ordering specifically for publishing `X`.

Some _happens-before_ orderings guaranteed by the class library include:

* Placing an item in a thread-safe collection _happens-before_ another thread retrieves that item from the collection;
* Counting down on a `CountDownLatch` _happens-before_ a thread returns from `await` on that latch;
* Releasing a permit to a `Semaphore` _happens-before_ acquiring a permit from that same `Semaphore`;
* Actions taken by the task represented by a `Future` _happens-before_ another thread successfully returns from `Future.get`;
* Submitting a `Runnable` or `Callable` to an `Executor` _happens-before_ the task begins execution; and
* A thread arriving at a `CyclicBarrier` or `Exchanger` _happens-before_ the other threads are released from that same barrier or exchange point. If `CyclicBarrier` uses a barrier action, arriving at the barrier _happens-before_ the barrier action, which in turn _happens-before_ threads are released from the barrier.

## 16.2 Publication

### 16.2.1 Unsafe publication

With the exception of immutable objects, it is not safe to use an object that has been initialized by another thread unless the publication _happens-before_ the consuming thread uses it.

### 16.2.3 Safe initialization idioms

Static initializers are run by the JVM at class initialization time, after class loading but before the class is used by any thread. Because the JVM acquires a lock during initialization and this lock is acquired by each thread at least once to ensure that the class has been loaded, memory writes made during static initialization are automatically visible to all threads. Thus statically initialized objects require no explicit synchronization either during construction or when being referenced. However, this applies only to the _as-constructed_ state—if the object is mutable, synchronization is still required by both readers and writers to make subsequent modifications visible and to avoid data corruption.

### 16.2.4 Double-checked locking

The purpose of double-checked locking (DCL) was to reduce the impact of synchronization while implementing lazy initialization in earlier Java versions. The way it worked was first to check whether initialization was needed without synchronizing, and if the resource reference was not `null`, use it. Otherwise, synchronize and check again if the `Resource` is initialized, ensuring that only one thread actually initializes the shared `Resource`. The common code path—fetching a reference to an already constructed Resource —doesn’t use synchronization. And that’s where the problem is: it is possible for a thread to see a partially constructed `Resource`.

Subsequent changes in the JMM (Java 5.0 and later) have enabled DCL to work if resource is made `volatile`, and the performance impact of this is small since volatile reads are usually only slightly more expensive than nonvolatile reads.

However, this is an idiom whose utility has largely passed—the forces that motivated it (slow uncontended synchronization, slow JVM startup) are no longer in play, making it less effective as an optimization. The [lazy initialization holder idiom](https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom) offers the same benefits and is easier to understand.

## 16.3 Initialization safety

The guarantee of _initialization safety_ allows properly constructed immutable objects to be safely shared across threads without synchronization, regardless of how they are published—even if published using a data race.

Initialization safety guarantees that for properly constructed objects, all threads will see the correct values of final fields that were set by the constructor, regardless of how the object is published. Further, any variables that can be reached through a final field of a properly constructed object (such as the elements of a final array or the contents of a HashMap referenced by a final field) are also guaranteed to be visible to other threads.

Initialization safety makes visibility guarantees only for the values that are reachable through final fields as of the time the constructor finishes. For values reachable through nonfinal fields, or values that may change after construction, you must use synchronization to ensure visibility.

## Summary

The Java Memory Model specifies when the actions of one thread on memory are guaranteed to be visible to another. The specifics involve ensuring that operations are ordered by a partial ordering called _happens-before_, which is specified at the level of individual memory and synchronization operations. In the absence of sufficient synchronization, some very strange things can happen when threads access shared data. However, the higher-level rules offered in Chapters 2 and 3, such as `@GuardedBy` and safe publication, can be used to ensure thread safety without resorting to the low-level details of _happens-before_.
