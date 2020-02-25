# Chapter 15: Atomic Variables and Nonblocking Synchronization

Much of the recent research on concurrent algorithms has focused on nonblocking algorithms, which use low-level atomic machine instructions such as _compare-and-swap_ instead of locks to ensure data integrity under concurrent access.

Nonblocking algorithms coordinate at a finer level of granularity and can greatly reduce scheduling overhead because they don’t block when multiple threads contend for the same data. Further, they are immune to deadlock and other liveness problems. In lock-based algorithms, other threads cannot make progress if a thread goes to sleep or spins while holding a lock, whereas nonblocking algorithms are impervious to individual thread failures.

## 15.1 Disadvantages of locking

Modern JVMs can optimize uncontended lock acquisition and release fairly effectively, but if multiple threads request the lock at the same time the JVM enlists the help of the operating system, thus, some unfortunate thread will be suspended and have to be resumed later. When that thread is resumed, it may have to wait for other threads to finish their scheduling quanta before it is actually scheduled. Suspending and resuming a thread has a lot of overhead and generally entails a lengthy interruption.

Volatile variables have some limitations compared to locking: while they provide similar visibility guarantees, they cannot be used to construct atomic compound actions. This means that volatile variables cannot be used when one variable depends on another, or when the new value of a variable depends on its old value. This limits when volatile variables are appropriate, since they cannot be used to reliably implement common tools such as counters or mutexes.

When a thread is waiting for a lock, it cannot do anything else. If a thread holding a lock is delayed (due to a page fault, scheduling delay, or the like), then no thread that needs that lock can make progress.

This can be a serious problem if the blocked thread is a high-priority thread but the thread holding the lock is a lower-priority thread—a performance hazard known as _priority inversion_.

If a thread holding a lock is permanently blocked (due to an infinite loop, deadlock, livelock, or other liveness failure), any threads waiting for that lock can never make progress.

Locking is simply a heavyweight mechanism for fine-grained operations such as incrementing a counter.

## 15.2 Hardware support for concurrency
