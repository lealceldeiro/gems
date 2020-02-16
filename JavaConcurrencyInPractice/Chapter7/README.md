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
