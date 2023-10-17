# Chapter 14: Building Custom Synchronizers

The easiest way to construct a state-dependent class is usually to build on top of an existing state-dependent library
class; but if the library classes do not provide the functionality you need, you can also build your own synchronizers
using the low-level mechanisms provided by the language and libraries, including intrinsic condition queues,
explicit `Condition` objects, and the `AbstractQueuedSynchronizer` framework.

## 14.1 Managing state dependence

In a single-threaded program, if a state-based precondition (like “the connection pool is nonempty”) does not hold when
a method is called, it will never become true. Therefore, classes in sequential programs can be coded to fail when their
preconditions do not hold. But in a concurrent program, state-based conditions can change through the actions of other
threads.

State-dependent operations that block until the operation can proceed are more convenient and less error-prone than
those that simply fail.

### 14.1.3 Condition queues

A condition queue gets its name because it gives a group of threads—called the wait set—a way to wait for a specific
condition to become true. Unlike typical queues in which the elements are data items, the elements of a condition queue
are the threads waiting for the condition.

Just as each Java object can act as a lock, each object can also act as a condition queue, and the wait , notify , and
notifyAll methods in Object constitute the API for intrinsic condition queues.

## 14.2 Using condition queues

Condition queues make it easier to build efficient and responsive state-dependent classes, but they are still easy to
use incorrectly; there are a lot of rules regarding their proper use that are not enforced by the compiler or platform.

### 14.2.1 The condition predicate

The key to using condition queues correctly is identifying the _condition predicates_ that the object may wait for.

The condition predicate is the precondition that makes an operation state-dependent in the first place.

Document the condition predicate(s) associated with a condition queue and the operations that wait on them.

Condition predicates are expressions constructed from the state variables of the class; for example,
a `BaseBoundedBuffer` tests for “buffer not empty” by comparing count to zero, and tests for “buffer not full” by
comparing count to the buffer size.

There is an important three-way relationship in a condition wait involving locking, the wait method, and a condition
predicate. The condition predicate involves state variables, and the state variables are guarded by a lock, so before
testing the condition predicate, we must hold that lock. The lock object and the condition queue object (the object on
which wait and notify are invoked) must also be the same object.

Every call to wait is implicitly associated with a specific condition predicate. When calling wait regarding a
particular condition predicate, the caller must already hold the lock associated with the condition queue, and that lock
must also guard the state variables from which the condition predicate is composed.

### 14.2.2 Waking up too soon

When a thread wakes up from `wait` the condition predicate must be tested again, and go back to waiting (or fail) if it
is not yet `true`. Since the thread can wake up repeatedly without the condition predicate being `true`, therefore
always the call to `wait` must be done from within a loop, testing the condition predicate in each iteration.

This is the canonical form for state-dependent condition wait methods.

```java
void stateDependentMethod() throws InterruptedException {
  // condition predicate must be guarded by lock
  synchronized(lock) {
    while (!conditionPredicate())
      lock.wait();
  // object is now in desired state
  }
}
```

When using condition waits (`Object.wait` or `Condition.await`):

* Always have a condition predicate—some test of object state that must hold before proceeding;
* Always test the condition predicate before calling `wait`, and again after returning from `wait`;
* Always call `wait` in a loop;
* Ensure that the state variables making up the condition predicate are guarded by the lock associated with the
  condition queue;
* Hold the lock associated with the condition queue when calling `wait`, `notify`, or `notifyAll`; and
* Do not release the lock after checking the condition predicate but before acting on it.

### 14.2.3 Missed signals

A missed signal occurs when a thread must wait for a specific condition that is already true, but fails to check the
condition predicate before waiting. Now the thread is waiting to be notified of an event that has already occurred.

### 14.2.4 Notification

Whenever you wait on a condition, make sure that someone will perform a notification whenever the condition predicate
becomes true.

There are two notification methods in the condition queue API— `notify` and `notifyAll`. To call either, you must hold
the lock associated with the condition queue object.

Calling `notify` causes the JVM to select one thread waiting on that condition queue to wake up; calling `notifyAll`
wakes up all the threads waiting on that condition queue.

Because you must hold the lock on the condition queue object when calling `notify` or `notifyAll`, and waiting threads
cannot return from `wait` without reacquiring the lock, the notifying thread should release the lock quickly to ensure
that the waiting threads are unblocked as soon as possible.

Single `notify` can be used instead of `notifyAll` _only_ when both of the following conditions hold:

* **Uniform waiters**. Only one condition predicate is associated with the condition queue, and each thread executes the
  same logic upon returning from wait; and
* **One-in, one-out**. A notification on the condition variable enables at most one thread to proceed.

### 14.2.6 Subclass safety issues

Using conditional or single notification introduces constraints that can complicate subclassing. If you want to support
subclassing at all, you must structure your class so subclasses can add the appropriate notification on behalf of the
base class if it is subclassed in a way that violates one of the requirements for single or conditional notification.

A state-dependent class should either fully expose (and document) its waiting and notification protocols to subclasses,
or prevent subclasses from participating in them at all.

### 14.2.7 Encapsulating condition queues

It is generally best to encapsulate the condition queue so that it is not accessible outside the class hierarchy in
which it is used. Otherwise, callers might be tempted to think they understand your protocols for waiting and
notification and use them in a manner inconsistent with your design.

## 14.3 Explicit condition objects

Just as `Lock` is a generalization of intrinsic locks, `Condition` is a generalization of intrinsic condition queues.

Intrinsic condition queues have several drawbacks. Each intrinsic lock can have only one associated condition queue,
which means that some classes multiple threads might wait on the same condition queue for different condition
predicates, and the most common pattern for locking involves exposing the condition queue object. Both of these factors
make it impossible to enforce the _uniform waiter_ requirement for using `notify`.

If it is needed to write a concurrent object with multiple condition predicates, or it is wanted to exercise more
control over the visibility of the condition queue, the explicit `Lock` and `Condition` classes offer a more flexible
alternative to intrinsic locks and condition queues.

Hazard warning: The equivalents of `wait`, `notify`, and `notifyAll` for `Condition` objects are `await`, `signal`,
and `signalAll`. However, `Condition` extends `Object`, which means that it also has `wait` and `notify` methods. Be
sure to use the proper versions—`await` and `signal`—instead!

Choose between using explicit `Condition`s and intrinsic condition queues in the same way as you would choose
between `ReentrantLock` and `synchronized`: use `Condition` if you need its advanced features such as fair queueing or
multiple wait sets per lock, and otherwise prefer intrinsic condition queues.

Example of bounded buffer using explicit condition variables:

```java

@ThreadSafe
public class ConditionBoundedBuffer<T> {
  protected final Lock lock = new ReentrantLock();
  // CONDITION PREDICATE: notFull (count < items.length)
  private final Condition notFull = lock.newCondition();
  // CONDITION PREDICATE: notEmpty (count > 0)
  private final Condition notEmpty = lock.newCondition();
  @GuardedBy("lock")
  private final T[] items = (T[]) new Object[BUFFER_SIZE];
  @GuardedBy("lock") private int tail, head, count;
  // BLOCKS-UNTIL: notFull
  public void put(T x) throws InterruptedException {
    lock.lock();
    try {
      while (count == items.length)
        notFull.await();
      items[tail] = x;
      if (++tail == items.length)
        tail = 0;
      ++count;
      notEmpty.signal();
    } finally {
      lock.unlock();
    }
  }
  // BLOCKS-UNTIL: notEmpty
  public T take() throws InterruptedException {
    lock.lock();
    try {
      while (count == 0)
        notEmpty.await();
      T x = items[head];
      items[head] = null;
      if (++head == items.length)
        head = 0;
      --count;
      notFull.signal();
      return x;
    } finally {
      lock.unlock();
    }
  }
}
```

## 14.4 Anatomy of a synchronizer

`ReentrantLock` and `Semaphore` are both implemented using a common base class, `AbstractQueuedSynchronizer` (AQS).

Synchronizers built with AQS have only one point where they might block, reducing context-switch overhead and improving
throughput. AQS was designed for scalability, and all the synchronizers in `java.util.concurrent` that are built with
AQS benefit from this.

## 14.5 AbstractQueuedSynchronizer

The basic operations that an AQS-based synchronizer performs are some variants of _acquire_ and _release_. Acquisition
is the state-dependent operation and can always block. With a lock or semaphore, the meaning of acquire is
straightforward—acquire the lock or a permit—and the caller may have to wait until the synchronizer is in a state where
that can happen.

## 14.6 AQS in `java.util.concurrent` synchronizer classes

### 14.6.1 `ReentrantLock`

`ReentrantLock` supports only exclusive acquisition, so it implements `tryAcquire`, `tryRelease`,
and `isHeldExclusively`.

`ReentrantLock` uses the synchronization state to hold the lock acquisition count, and maintains an `owner` variable
holding the identity of the owning thread that is modified only when the current thread has just acquired the lock or is
just about to release it.

`ReentrantLock` also takes advantage of AQS’s built-in support for multiple condition variables and wait
sets. `Lock.newCondition` returns a new instance of `ConditionObject`, an inner class of AQS.

### 14.6.2 `Semaphore` and `CountDownLatch`

`Semaphore` uses the AQS synchronization state to hold the count of permits currently available. The `tryAcquireShared`
method first computes the number of permits remaining, and if there are not enough, returns a value indicating that the
acquire failed. If sufficient permits appear to be left, it attempts to atomically reduce the permit count
using `compareAndSetState`. If that succeeds (meaning that the permit count had not changed since it last looked), it
returns a value indicating that the acquire succeeded. The return value also encodes whether other shared acquisition
attempts might succeed, in which case other waiting threads will also be unblocked.

The `while` loop terminates either when there are not enough permits or when `tryAcquireShared` can atomically update
the permit count to reflect acquisition.

```java
protected int tryAcquireShared(int acquires) {
  while (true) {
    int available = getState();
    int remaining = available - acquires;
    if (remaining < 0
      || compareAndSetState(available, remaining))
      return remaining;
  }
}

protected boolean tryReleaseShared(int releases) {
  while (true) {
    int p = getState();
    if (compareAndSetState(p, p + releases))
      return true;
  }
}
```

While any given call to `compareAndSetState` may fail due to contention with another thread, causing it to retry, one of
these two termination criteria will become true within a reasonable number of retries.

### 14.6.3 `FutureTask`

`FutureTask` uses the AQS synchronization state to hold the task status—running, completed, or cancelled. It also
maintains additional state variables to hold the result of the computation or the exception it threw. It further
maintains a reference to the thread that is running the computation (if it is currently in the running state), so that
it can be interrupted if the task is cancelled.

### 14.6.4 `ReentrantReadWriteLock`

The interface for `ReadWriteLock` suggests there are two locks—a reader lock and a writer lock—but in the AQS-based
implementation of `ReentrantReadWriteLock`, a single AQS subclass manages both read and write
locking. `ReentrantReadWriteLock` uses 16 bits of the state for the write-lock count, and the other 16 bits for the
read-lock count. Operations on the read lock use the shared acquire and release methods; operations on the write lock
use the exclusive acquire and release methods. Internally, AQS maintains a queue of waiting threads, keeping track of
whether a thread has requested exclusive or shared access. In `ReentrantReadWriteLock`, when the lock becomes available,
if the thread at the head of the queue was looking for write access it will get it, and if the thread at the head of the
queue was looking for read access, all queued threads up to the first writer will get it.

## Summary

If it is needed to implement a state-dependent class—one whose methods must block if a state-based precondition does not
hold—the best strategy is usually to build upon an existing library class such as `Semaphore`, `BlockingQueue`,
or `CountDownLatch`. However, sometimes existing library classes do not provide a sufficient foundation; in these cases,
we can build our own synchronizers using intrinsic condition queues, explicit `Condition` objects,
or `AbstractQueuedSynchronizer`. Intrinsic condition queues are tightly bound to intrinsic locking, since the mechanism
for managing state dependence is necessarily tied to the mechanism for ensuring state consistency.

Similarly, explicit `Condition`s are tightly bound to explicit `Lock`s, and offer an extended feature set compared to
intrinsic condition queues, including multiple wait sets per lock, interruptible or uninterruptible condition waits,
fair or nonfair queuing, and deadline-based waiting.
