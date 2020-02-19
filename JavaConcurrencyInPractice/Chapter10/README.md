# Chapter 10: Avoiding Liveness Hazards

Indiscriminate use of locking can cause _lock-ordering deadlocks_.

Failure to understand the activities being bounded while consuming resources (for example, using thread pools and semaphores) can cause resource deadlocks.

Java applications do not recover from deadlock.

## 10.1 Deadlock

When a thread holds a lock forever, other threads attempting to acquire that lock will block forever waiting. When thread `A` holds lock `L` and tries to acquire lock `M`, but at the same time thread `B` holds `M` and tries to acquire `L`, both threads will wait forever (_deadly embrace_).

### 10.1.1 Lock-ordering deadlocks

A program will be free of lock-ordering deadlocks if all threads acquire the locks they need in a fixed global order.

### 10.1.2 Dynamic lock order deadlocks

Deadlocks like this one can be spotted by looking for nested lock acquisitions. Since the order of arguments (objects where the locks are acquired) is out of our control, to fix the problem we must _induce_ an ordering on the locks and acquire them according to the induced ordering consistently throughout the application (See [this SO post](https://stackoverflow.com/a/55849778/5640649)).

### 10.1.3 Deadlocks between cooperating objects

Invoking an _alien_ method with a lock held is asking for liveness trouble. The alien method might acquire other locks (risking deadlock) or block for an unexpectedly long time, stalling other threads that need the lock you hold.

### 10.1.4 Open calls

Calling a method with no locks held is called an open call, and classes that rely on open calls are more well-behaved and composable than classes that make calls with locks held.

Very often, the cause of problems like these is the use of `synchronized` methods instead of smaller `synchronized` blocks for reasons of compact syntax or simplicity rather than because the entire method must be guarded by a lock.

Strive to use open calls throughout your program. Programs that rely on open calls are far easier to analyze for deadlock-freedom than those that allow calls to alien methods with locks held.

### 10.1.5 Resource deadlocks

One form of resource-based deadlock is _thread-starvation deadlock_.

Tasks that wait for the results of other tasks are the primary source of thread-starvation deadlock.

## 10.2 Avoiding and diagnosing deadlocks

A program that never acquires more than one lock at a time cannot experience lock-ordering deadlock.

If multiple locks must be acquired, lock ordering must be a part of the design: it should be tried to minimize the number of potential locking interactions, and a lock-ordering protocol for locks that may be acquired together should be followed and documented.

In programs that use fine-grained locking, the code should be audited for deadlock freedom using a two-part strategy: first, identifying where multiple locks could be acquired (trying to make this a small set), and then performing a global analysis of all such instances to ensure that lock ordering is consistent across the entire program.

### 10.2.1 Timed lock attempts

Another technique for detecting and recovering from deadlocks is to use the timed `tryLock` feature of the explicit `Lock` classes instead of intrinsic locking.

Using timed lock acquisition to acquire multiple locks can be effective against deadlock even when timed locking is not used consistently throughout the program.

### 10.2.2 Deadlock analysis with thread dumps

The JVM can help identify deadlocks when they do happen using _thread dumps_.

To trigger a thread dump, you can send the JVM process a `SIGQUIT` signal (kill -3) on Unix platforms, or press the <kbd>Ctrl</kbd> + <kbd>`\`</kbd> key on Unix or <kbd>Ctrl</kbd> + <kbd>Break</kbd> on Windows platforms. Many IDEs can request a thread dump as well.

Java 6 does include thread dump support and deadlock detection with explicit `Lock`s, but the information on where `Lock`s are acquired is necessarily less precise than for intrinsic locks. Intrinsic locks are associated with the stack frame in which they were acquired; explicit Lock s are associated only with the acquiring thread.

## 10.3 Other liveness hazards

While deadlock is the most widely encountered liveness hazard, there are several other liveness hazards you may encounter in concurrent programs including starvation, missed signals, and livelock.

### 10.3.1 Starvation

Starvation occurs when a thread is perpetually denied access to resources it needs in order to make progress; the most commonly starved resource is CPU cycles.

Avoid the temptation to use thread priorities, since they increase platform dependence and can cause liveness problems. Most concurrent applications can use the default priority for all threads.

### 10.3.2 Poor responsiveness

CPU-intensive background tasks can still affect responsiveness because they can compete for CPU cycles with the event thread.

Poor responsiveness can also be caused by poor lock management.

### 10.3.3 Livelock

_Livelock_ is a form of liveness failure in which a thread, while not blocked, still cannot make progress because it keeps retrying an operation that will always fail.

Livelock can occur when multiple cooperating threads change their state in response to the others in such a way that no thread can ever make progress.

## Summary

Liveness failures are a serious problem because there is no way to recover from them short of aborting the application. The most common form of liveness failure is lock-ordering deadlock. Avoiding lock ordering deadlock starts at design time: ensure that when threads acquire multiple locks, they do so in a consistent order. The best way to do this is by using open calls throughout your program. This greatly reduces the number of places where multiple locks are held at once, and makes it more obvious where those places are.
