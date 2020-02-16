# Chapter 7: Cancellation and Shutdown

## 7.1 Task cancellation

An activity is _cancellable_ if external code can move it to completion before its normal completion.

There is no safe way to preemptively stop a thread in Java, and therefore no safe way to preemptively stop a task. There are only cooperative mechanisms, by which the task and the code requesting cancellation follow an agreed-upon protocol.

### 7.1.1 Interruption

There is nothing in the API or language specification that ties interruption to any specific cancellation semantics, but in practice, using interruption for anything but cancellation is fragile and difficult to sustain in larger applications.

Each thread has a boolean _interrupted_ status; interrupting a thread sets its interrupted status to true.

Calling interrupt does not necessarily stop the target thread from doing what it is doing; it merely delivers the message that interruption has been requested.

Interruption is usually the most sensible way to implement cancellation.

### 7.1.2 Interruption policies

A task should not assume anything about the interruption policy of its executing thread unless it is explicitly designed to run within a service that has a specific interruption policy.

A thread should be interrupted only by its owner; the owner can encapsulate knowledge of the thread’s interruption policy in an appropriate cancellation mechanism such as a shutdown method.

Because each thread has its own interruption policy, you should not interrupt a thread unless you know what interruption means to that thread.

### 7.1.3 Responding to interruption

When you call an interruptible blocking method such as `Thread.sleep` or `BlockingQueue.put` there are two practical strategies for handling `InterruptedException`:

* Propagate the exception (possibly after some task-specific cleanup), making your method an interruptible blocking method, too; or
* Restore the interruption status so that code higher up on the call stack can deal with it.

Only code that implements a thread’s interruption policy may swallow an interruption request. General-purpose task and library code should never swallow interruption requests.

Activities that do not support cancellation but still call interruptible blocking methods will have to call them in a loop, retrying when interruption is detected. In this case, they should save the interruption status locally and restore it just before returning rather than immediately upon catching `InterruptedException`. Setting the interrupted status too early could result in an infinite loop, because most interruptible blocking methods check the interrupted status on entry and throw InterruptedException immediately if it is set. i.e.:
```
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

When `Future.get` throws `InterruptedException` or `TimeoutException` and you know that the result is no longer needed by the program, cancel the task with `Future.cancel`.

### 7.1.6 Dealing with non-interruptible blocking

We can sometimes convince threads blocked in noninterruptible activities to stop by means similar to interruption, but this requires greater awareness of why the thread is blocked.

* Synchronous socket I/O in `java.io`: The common form of blocking I/O in server applications is reading or writing to a socket. Unfortunately, the read and write methods in `InputStream` and `OutputStream` are not responsive to interruption, but closing the underlying socket makes any threads blocked in read or write throw a `SocketException`.
* Synchronous I/O in `java.nio`: Interrupting a thread waiting on an `InterruptibleChannel` causes it to throw `ClosedByInterruptException` and close the channel (and also causes all other threads blocked on the channel to throw `ClosedByInterruptException`). Closing an `InterruptibleChannel` causes threads blocked on channel operations to throw `AsynchronousCloseException`. Most standard `Channel`s implement `InterruptibleChannel`.
* Asynchronous I/O with Selector. If a thread is blocked in `Selector.select` (in `java.nio.channels`), calling close or wakeup causes it to return prematurely.
* Lock acquisition. If a thread is blocked waiting for an intrinsic lock, there is nothing you can do to stop it short of ensuring that it eventually acquires the lock and makes enough progress that you can get its attention some other way. However, the explicit `Lock` classes offer the `lockInterruptibly` method, which allows you to wait for a lock and still be responsive to interrupts.

### 7.1.7 Encapsulating nonstandard cancellation with `newTaskFor`
