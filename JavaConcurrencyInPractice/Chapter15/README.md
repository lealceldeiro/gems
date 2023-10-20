# Chapter 15: Atomic Variables and Nonblocking Synchronization

Much of the recent research on concurrent algorithms has focused on nonblocking algorithms, which use low-level atomic
machine instructions such as _compare-and-swap_ instead of locks to ensure data integrity under concurrent access.

Nonblocking algorithms coordinate at a finer level of granularity and can greatly reduce scheduling overhead because
they don’t block when multiple threads contend for the same data. Further, they are immune to deadlock and other
liveness problems. In lock-based algorithms, other threads cannot make progress if a thread goes to sleep or spins while
holding a lock, whereas nonblocking algorithms are impervious to individual thread failures.

## 15.1 Disadvantages of locking

Modern JVMs can optimize uncontended lock acquisition and release fairly effectively, but if multiple threads request
the lock at the same time the JVM enlists the help of the operating system, thus, some unfortunate thread will be
suspended and have to be resumed later. When that thread is resumed, it may have to wait for other threads to finish
their scheduling quanta before it is actually scheduled. Suspending and resuming a thread has a lot of overhead and
generally entails a lengthy interruption.

Volatile variables have some limitations compared to locking: while they provide similar visibility guarantees, they
cannot be used to construct atomic compound actions. This means that volatile variables cannot be used when one variable
depends on another, or when the new value of a variable depends on its old value. This limits when volatile variables
are appropriate, since they cannot be used to reliably implement common tools such as counters or mutexes.

When a thread is waiting for a lock, it cannot do anything else. If a thread holding a lock is delayed (due to a page
fault, scheduling delay, or the like), then no thread that needs that lock can make progress.

This can be a serious problem if the blocked thread is a high-priority thread but the thread holding the lock is a
lower-priority thread—a performance hazard known as _priority inversion_.

If a thread holding a lock is permanently blocked (due to an infinite loop, deadlock, livelock, or other liveness
failure), any threads waiting for that lock can never make progress.

Locking is simply a heavyweight mechanism for fine-grained operations such as incrementing a counter.

## 15.2 Hardware support for concurrency

Processors designed for multiprocessor operation provide special instructions for managing concurrent access to shared
variables. Early processors had atomic _test-and-set_, _fetch-and-increment_, or _swap_ instructions sufficient for
implementing mutexes that could in turn be used to implement more sophisticated concurrent objects. Today, nearly every
modern processor has some form of atomic read-modify-write instruction, such as _compare-and-swap_ or
_load-linked/store-conditional_.

Operating systems and JVMs use these instructions to implement locks and concurrent data structures, but until Java 5.0
they had not been available directly to Java classes.

### 15.2.1 Compare and swap

Compare and Swap (CAS) has three operands—a memory location `V` on which to operate, the expected old value `A`, and the
new value `B`. CAS atomically updates `V` to the new value `B`, but only if the value in `V` matches the expected old
value `A`; otherwise it does nothing. In either case, it returns the value currently in `V`.

### 15.2.3 CAS support in the JVM

Prior to Java 5.0, there was no way to do this short of writing native code. In Java 5.0, low-level support was added to
expose CAS operations on `int`, `long`, and object references, and the JVM compiles these into the most efficient means
provided by the underlying hardware. On platforms supporting CAS, the runtime inlines them into the appropriate machine
instruction(s); in the worst case, if a CAS-like instruction is not available the JVM uses a spin lock.

## 15.3 Atomic variable classes

Atomic variables are finer-grained and lighter-weight than locks, and are critical for implementing high-performance
concurrent code on multiprocessor systems. They limit the scope of contention to a single variable; this is as
fine-grained as you can get.

With algorithms based on atomic variables instead of locks, threads are more likely to be able to proceed without delay
and have an easier time recovering if they do experience contention.

There are twelve atomic variable classes, divided into four groups: scalars, field updaters, arrays, and compound
variables. The most commonly used atomic variables are the scalars: `AtomicInteger`, `AtomicLong`, `AtomicBoolean`,
and `AtomicReference`. All support CAS; the `Integer` and `Long` versions support arithmetic as well.

Like most mutable objects, they are not good candidates for keys in hash-based collections.

### 15.3.2 Performance comparison: locks versus atomic variables

At high contention levels locking tends to outperform atomic variables, but at more realistic contention levels atomic
variables outperform locks.

In practice, atomics tend to scale better than locks because atomics deal more effectively with typical contention
levels.

With low to moderate contention, atomics offer better scalability; with high contention, locks offer better contention
avoidance.

## 15.4 Nonblocking algorithms

An algorithm is called nonblocking if failure or suspension of any thread cannot cause failure or suspension of another
thread; an algorithm is called lock-free if, at each step, some thread can make progress.

Algorithms that use CAS exclusively for coordination between threads can, if constructed correctly, be both nonblocking
and lock-free. An uncontended CAS always succeeds, and if multiple threads contend for a CAS, one always wins and
therefore makes progress. Nonblocking algorithms are also immune to deadlock or priority inversion.

## Summary

Nonblocking algorithms maintain thread safety by using low-level concurrency primitives such as compare-and-swap instead
of locks. These low-level primitives are exposed through the atomic variable classes, which can also be used as “better
volatile variables” providing atomic update operations for integers and object references.

Nonblocking algorithms are difficult to design and implement, but can offer better scalability under typical conditions
and greater resistance to liveness failures. Many of the advances in concurrent performance from one JVM version to the
next come from the use of nonblocking algorithms, both within the JVM and in the platform libraries.
