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
