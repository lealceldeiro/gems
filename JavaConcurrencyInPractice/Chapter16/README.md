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
