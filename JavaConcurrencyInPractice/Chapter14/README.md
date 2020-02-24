# Chapter 14: Building Custom Synchronizers

The easiest way to construct a state-dependent class is usually to build on top of an existing state-dependent library class; but if the library classes do not provide the functionality you need, you can also build your own synchronizers using the low-level mechanisms provided by the language and libraries, including intrinsic condition queues, explicit `Condition` objects, and the `AbstractQueuedSynchronizer` framework.

## 14.1 Managing state dependence

In a single-threaded program, if a state-based precondition (like “the connection pool is nonempty”) does not hold when a method is called, it will never become true. Therefore, classes in sequential programs can be coded to fail when their preconditions do not hold. But in a concurrent program, state-based conditions can change through the actions of other threads.

State-dependent operations that block until the operation can proceed are more convenient and less error-prone than those that simply fail.

### 14.1.3 Condition queues

A condition queue gets its name because it gives a group of threads—called the wait set—a way to wait for a specific condition to become true. Unlike typical queues in which the elements are data items, the elements of a condition queue are the threads waiting for the condition.

Just as each Java object can act as a lock, each object can also act as a condition queue, and the wait , notify , and notifyAll methods in Object constitute the API for intrinsic condition queues.

## 14.2 Using condition queues

Condition queues make it easier to build efficient and responsive state-dependent classes, but they are still easy to use incorrectly; there are a lot of rules regarding their proper use that are not enforced by the compiler or platform.

### 14.2.1 The condition predicate

The key to using condition queues correctly is identifying the _condition predicates_ that the object may wait for.

The condition predicate is the precondition that makes an operation state-dependent in the first place.

Document the condition predicate(s) associated with a condition queue and the operations that wait on them.

Condition predicates are expressions constructed from the state variables of the class; for example, a `BaseBoundedBuffer` tests for “buffer not empty” by comparing count to zero, and tests for “buffer not full” by comparing count to the buffer size.

There is an important three-way relationship in a condition wait involving locking, the wait method, and a condition predicate. The condition predicate involves state variables, and the state variables are guarded by a lock, so before testing the condition predicate, we must hold that lock. The lock object and the condition queue object (the object on which wait and notify are invoked) must also be the same object.

Every call to wait is implicitly associated with a specific condition predicate. When calling wait regarding a particular condition predicate, the caller must already hold the lock associated with the condition queue, and that lock must also guard the state variables from which the condition predicate is composed.

### 14.2.2 Waking up too soon

When a thread wakes up from `wait` the condition predicate  must tested again, and go back to waiting (or fail) if it is not yet `true`. Since the thread can wake up repeatedly without the condition predicate being `true`, therefore always the call to `wait` must be done from within a loop, testing the condition predicate in each iteration.

This is the canonical form for state-dependent condition wait methods.

```
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
* Ensure that the state variables making up the condition predicate are guarded by the lock associated with the condition queue;
* Hold the lock associated with the the condition queue when calling `wait`, `notify`, or `notifyAll`; and
* Do not release the lock after checking the condition predicate but before acting on it.

### 14.2.3 Missed signals

A missed signal occurs when a thread must wait for a specific condition that is already true, but fails to check the condition predicate before waiting. Now the thread is waiting to be notified of an event that has already occurred.

### 14.2.4 Notification

Whenever you wait on a condition, make sure that someone will perform a notification whenever the condition predicate becomes true.

There are two notification methods in the condition queue API— `notify` and `notifyAll`. To call either, you must hold the lock associated with the condition queue object.

Calling `notify` causes the JVM to select one thread waiting on that condition queue to wake up; calling `notifyAll` wakes up all the threads waiting on that condition queue.

Because you must hold the lock on the condition queue object when calling `notify` or `notifyAll`, and waiting threads cannot return from `wait` without reacquiring the lock, the notifying thread should release the lock quickly to ensure that the waiting threads are unblocked as soon as possible.

Single `notify` can be used instead of `notifyAll` _only_ when both of the following conditions hold:

* **Uniform waiters**. Only one condition predicate is associated with the condition queue, and each thread executes the same logic upon returning from wait; and
* **One-in, one-out**. A notification on the condition variable enables at most one thread to proceed.

### 14.2.6 Subclass safety issues

Using conditional or single notification introduces constraints that can complicate subclassing. If you want to support subclassing at all, you must structure your class so subclasses can add the appropriate notification on behalf of the base class if it is subclassed in a way that violates one of the requirements for single or conditional notification.

A state-dependent class should either fully expose (and document) its waiting and notification protocols to subclasses, or prevent subclasses from participating in them at all.

### 14.2.7 Encapsulating condition queues

It is generally best to encapsulate the condition queue so that it is not accessible outside the class hierarchy in which it is used. Otherwise, callers might be tempted to think they understand your protocols for waiting and notification and use them in a manner inconsistent with your design.

## 14.3 Explicit condition objects
