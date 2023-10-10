# Chapter 7: Cancellation and Shutdown

## 7.1 Task cancellation

An activity is _cancellable_ if external code can move it to completion before its normal completion.

There is no safe way to preemptively stop a thread in Java, and therefore no safe way to preemptively stop a task. There
are only cooperative mechanisms, by which the task and the code requesting cancellation follow an agreed-upon protocol.

### 7.1.1 Interruption

There is nothing in the API or language specification that ties interruption to any specific cancellation semantics, but
in practice, using interruption for anything but cancellation is fragile and difficult to sustain in larger
applications.

Each thread has a boolean _interrupted_ status; interrupting a thread sets its interrupted status to true.

Calling interrupt does not necessarily stop the target thread from doing what it is doing; it merely delivers the
message that interruption has been requested.

Interruption is usually the most sensible way to implement cancellation.

### 7.1.2 Interruption policies

A task should not assume anything about the interruption policy of its executing thread unless it is explicitly designed
to run within a service that has a specific interruption policy.

A thread should be interrupted only by its owner; the owner can encapsulate knowledge of the thread’s interruption
policy in an appropriate cancellation mechanism such as a shutdown method.

Because each thread has its own interruption policy, you should not interrupt a thread unless you know what interruption
means to that thread.

### 7.1.3 Responding to interruption

When you call an interruptible blocking method such as `Thread.sleep` or `BlockingQueue.put` there are two practical
strategies for handling `InterruptedException`:

* Propagate the exception (possibly after some task-specific cleanup), making your method an interruptible blocking
  method, too; or
* Restore the interruption status so that code higher up on the call stack can deal with it.

Only code that implements a thread’s interruption policy may swallow an interruption request. General-purpose task and
library code should never swallow interruption requests.

Activities that do not support cancellation but still call interruptible blocking methods will have to call them in a
loop, retrying when interruption is detected. In this case, they should save the interruption status locally and restore
it just before returning rather than immediately upon catching `InterruptedException`. Setting the interrupted status
too early could result in an infinite loop, because most interruptible blocking methods check the interrupted status on
entry and throw InterruptedException immediately if it is set. i.e.:

```java
public Task getNextTask(BlockingQueue<Task> queue) {
  boolean interrupted = false;
  try {
    while (true) {
      try {
        return queue.take();
      } catch (InterruptedException e) {
        interrupted = true;
        // fall through and retry
      }
    }
  } finally {
    if (interrupted)
      Thread.currentThread().interrupt();
  }
}
```

When `Future.get` throws `InterruptedException` or `TimeoutException` and you know that the result is no longer needed
by the program, cancel the task with `Future.cancel`.

### 7.1.6 Dealing with non-interruptible blocking

We can sometimes convince threads blocked in noninterruptible activities to stop by means similar to interruption, but
this requires greater awareness of why the thread is blocked.

* Synchronous socket I/O in `java.io`: The common form of blocking I/O in server applications is reading or writing to a
  socket. Unfortunately, the read and write methods in `InputStream` and `OutputStream` are not responsive to
  interruption, but closing the underlying socket makes any threads blocked in read or write throw a `SocketException`.
* Synchronous I/O in `java.nio`: Interrupting a thread waiting on an `InterruptibleChannel` causes it to
  throw `ClosedByInterruptException` and close the channel (and also causes all other threads blocked on the channel to
  throw `ClosedByInterruptException`). Closing an `InterruptibleChannel` causes threads blocked on channel operations to
  throw `AsynchronousCloseException`. Most standard `Channel`s implement `InterruptibleChannel`.
* Asynchronous I/O with Selector. If a thread is blocked in `Selector.select` (in `java.nio.channels`), calling close or
  wakeup causes it to return prematurely.
* Lock acquisition. If a thread is blocked waiting for an intrinsic lock, there is nothing you can do to stop it short
  of ensuring that it eventually acquires the lock and makes enough progress that you can get its attention some other
  way. However, the explicit `Lock` classes offer the `lockInterruptibly` method, which allows you to wait for a lock
  and still be responsive to interrupts.

## 7.2 Stopping a thread-based service

Provide lifecycle methods whenever a thread-owning service has a lifetime longer than that of the method that created
it.

### 7.2.2 `ExecutorService` shutdown

The two different termination options (`shutdown` and `shutdownNow`) offer a tradeoff between safety and responsiveness:
abrupt termination is faster but riskier because tasks may be interrupted in the middle of execution, and normal
termination is slower but safer because the `ExecutorService` does not shut down until all queued tasks are processed.
Other thread-owning services should consider providing a similar choiceof shutdown modes.

### 7.2.3 Poison pills

A _poison pill_ is a recognizable object placed on the queue that means “when you getthis, stop”.

### 7.2.5 Limitations of `shutdownNow`

When an `ExecutorService` is shut down abruptly with shutdownNow , it attempts to cancel the tasks currently in progress
and returns a list of tasks that were submitted but never started so that they can be logged or saved for later
processing.

There is no general way to find out which tasks started but did not complete.

There is no way of knowing the state of the tasks in progress at shutdown time unless the tasks themselves perform some
sort of checkpointing.

To know which tasks have not completed, you need to know not only which tasks didn’t start, but also which tasks were in
progress when the executor was shut down <sub><sup>**1**</sup></sub>.

## 7.3 Handling abnormal thread termination

The less familiar you are with the code being called, the more skeptical you should be about its behavior.

When you are calling unknown, untrusted code through an abstraction such as `Runnable` is one of the few times when you
might want to consider catching `RuntimeException` (although there is some controversy over the safety of this
technique; when a thread throws an unchecked).

### 7.3.1 Uncaught exception handlers

The Thread API also provides the UncaughtExceptionHandler facility, which lets you detect when a thread dies due to an
uncaught exception.

When a thread exits due to an uncaught exception, the JVM reports this event to an
application-provided `UncaughtExceptionHandler`; if no handler exists, the default behavior is to print the stack trace
to `System.err`.

In long-running applications, always use uncaught exception handlers for all threads that at least log the exception.

## 7.4 JVM shutdown

The JVM can shut down in either an orderly or abrupt manner. An orderly shutdown is initiated when the last “normal” (
nondaemon) thread terminates, someone calls `System.exit`, or by other platform-specific means (such as sending
a `SIGINT` or hitting <kbd>Ctrl</kbd> + <kbd>C</kbd> ). While this is the standard and preferred way for the JVM to shut
down, it can also be shut down abruptly by calling `Runtime.halt` or by killing the JVM process through the operating
system (such as sending a `SIGKILL`).

### 7.4.1 Shutdown hooks

In an orderly shutdown, the JVM first starts all registered _shutdown hooks_. Shutdown hooks are unstarted threads that
are registered with `Runtime.addShutdownHook`.

If the shutdown hooks or finalizers don't complete, then the orderly shutdown process “hangs” and the JVM must be shut
down abruptly. In an abrupt shutdown, the JVM is not required to do anything other than halt the JVM; shutdown hooks
will not run.

Shutdown hooks should be thread-safe: they must use synchronization when accessing shared data and should be careful
to avoid deadlock, just like any other concurrent code.

Shutdown hooks can be used for service or application cleanup, such as deleting temporary files or cleaning up resources
that are not automatically cleaned up by the OS; they must use synchronization when accessing shared data and should be
careful to avoid deadlock, they should not make assumptions about the state of the application or about why the JVM is
shutting down, and must therefore be coded extremely defensively. Finally, they should exit as quickly as possible,
since their existence delays JVM termination at a time when the user may be expecting the JVM to terminate quickly.

### 7.4.2 Daemon threads

Threads are divided into two types: normal threads and daemon threads.

Normal threads and daemon threads differ only in what happens when they exit. When a thread exits, the JVM performs an
inventory of running threads, and if the only threads that are left are daemon threads, it initiates an orderly
shutdown. When the JVM halts, any remaining daemon threads are abandoned—finally blocks are not executed, stacks are not
unwound—the JVM just exits.

Daemon threads are not a good substitute for properly managing the life-cycle of services within an application.

### 7.4.3 Finalizers

Since finalizers can run in a thread managed by the JVM, any state accessed by a finalizer will be accessed by more than
one thread and therefore must be accessed with synchronization.

Finalizers offer no guarantees on when or even if they run, and they impose a significant performance cost on objects
with nontrivial finalizers.

They are also extremely difficult to write correctly. In most cases, the combination of finally blocks and explicit
close methods does a better job of resource management than finalizers; the sole exception is when you need to manage
objects that hold resources acquired by native methods.

Avoid finalizers.

## Summary

End-of-lifecycle issues for tasks, threads, services, and applications can add complexity to their design and
implementation. Java does not provide a preemptive mechanism for cancelling activities or terminating threads. Instead,
it provides a cooperative interruption mechanism that can be used to facilitate cancellation, but it is up to you to
construct protocols for cancellation and use them consistently. Using `FutureTask` and the `Executor` framework
simplifies building cancellable tasks and services.

----

<sub><sup>**1. Unfortunately, there is no shutdown option in which tasks not yet started are returned to the caller but
tasks in progress are allowed to complete; such an option would eliminate this uncertain intermediate state.
**</sup></sub>
