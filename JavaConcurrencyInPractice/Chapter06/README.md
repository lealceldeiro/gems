# Chapter 6: Task Execution

## 6.1 Executing tasks in threads

The first step in organizing a program around task execution is identifying sensible _task boundaries_.

Ideally, tasks are independent activities: work that doesn't depend on the state, result, or side effects of other
tasks. Independence facilitates concurrency, as independent tasks can be executed in parallel if there are adequate
processing resources.

Choosing good task boundaries, coupled with a sensible _task execution policy_ can help achieve _good throughput_, _good
responsiveness_ and _graceful degradation_ in server applications.

### 6.1.1 Executing tasks sequentially

In server applications, sequential processing rarely provides either good throughput or good responsiveness. There are
exceptions—such as when tasks are few and long-lived, or when the server serves a single client that makes only a single
request at a time—but most server applications do not work this way

### 6.1.2 Explicitly creating threads for tasks

Under light to moderate load, the thread-per-task approach is an improvement over sequential execution. As long as the
request arrival rate does not exceed the server’s capacity to handle requests, this approach offers better
responsiveness and throughput.

### 6.1.3 Disadvantages of unbounded thread creation

For production use, however, the thread-per-task approach has some practical drawbacks, especially when a large number
of threads may be created:

* Thread lifecycle overhead.
* Resource consumption.
* Stability.

Unbounded thread creation may appear to work just fine during prototyping and development, with problems surfacing only
when the application is deployed and under heavy load.

## 6.2 The Executor framework

Thread pools offer the benefit of the usage of _bounded queues_ to prevent an overloaded application from running out of
memory for thread management, and `java.util.concurrent` provides a flexible thread pool implementation as part of the
_Executor_ framework.

Using an Executor is usually the easiest path to implementing a producer-consumer design in your application.

### 6.2.2 Execution policies

An execution policy specifies the “what, where, when, and how” of task execution, including:

* In what thread will tasks be executed?
* In what order should tasks be executed (FIFO, LIFO, priority order)?
* How many tasks may execute concurrently?
* How many tasks may be queued pending execution?
* If a task has to be rejected because the system is overloaded, which task
  should be selected as the victim, and how should the application be noti-
  fied?
* What actions should be taken before or after executing a task?

Whenever you see code of the form `new Thread(runnable).start()` and you think you might at some point want a more
flexible execution policy, seriously consider replacing it with the use of an `Executor`.

### 6.2.3 Thread pools

A thread pool manages a homogeneous pool of worker threads and is tightly bound to a _work queue_ holding tasks waiting
to
be executed.

You can create a thread pool by calling one of the static factory methods in `Executors`:

* `newFixedThreadPool`: A fixed-size thread pool creates threads as tasks are submitted, up to the maximum pool size,
  and then attempts to keep the pool size constant (adding new threads if a thread dies due to an unexpected Exception).
* `newCachedThreadPool`: A cached thread pool has more flexibility to reap idle threads when the current size of the
  pool exceeds the demand for processing, and to add new threads when demand increases, but places no bounds on the size
  of the pool.
* `newSingleThreadExecutor`: A single-threaded executor creates a single worker thread to process tasks, replacing it if
  it dies unexpectedly. Tasks are guaranteed to be processed sequentially according to the order imposed by the task
  queue (FIFO, LIFO, priority order).
* `newScheduledThreadPool`: A fixed-size thread pool that supports delayed and periodic task execution, similar
  to `Timer`.

### 6.2.4 Executor lifecycle

To address the issue of execution service lifecycle, the `ExecutorService` interface extends `Executor` , adding a
number of methods for lifecycle management (as well as some convenience methods for task submission).

The lifecycle implied by `ExecutorService` has three states—_running_, _shutting down_, and _terminated_.

### 6.2.5 Delayed and periodic tasks

You can construct a `ScheduledThreadPoolExecutor` through its constructor or through the `newScheduledThreadPool`
factory.

`ScheduledThreadPoolExecutor` deals properly with ill-behaved tasks; there is little reason to use `Timer` in Java 5.0
or later.

## 6.3 Finding exploitable parallelism

In most server applications, there is an obvious task boundary: a single client request. But sometimes good task
boundaries are not quite so obvious, as in many desktop applications.

### 6.3.2 Result-bearing tasks: `Callable` and `Future`

`Runnable` and `Callable` describe abstract computational tasks. Tasks are usually finite: they have a clear starting
point and they eventually terminate.

The lifecycle of a task executed by an `Executor` has four phases: _created_, _submitted_, _started_, and _completed_.

In the `Executor` framework, tasks that have been submitted but not yet started can always be cancelled, and tasks that
have started can sometimes be cancelled if they are responsive to interruption.

`Future` represents the lifecycle of a task and provides methods to test whether the task has completed or been
cancelled, retrieve its result, and cancel the task.

The behavior of `Future.get` varies depending on the task state (not yet started, running, completed). It returns
immediately or throws an `Exception` if the task has already completed, but if not it blocks until the task completes.

Submitting a `Runnable` or `Callable` to an `Executor` constitutes a safe publication of the `Runnable` or `Callable`
from the submitting thread to the thread that will eventually execute the task.

Similarly, setting the result value for a `Future` constitutes a safe publication of the result from the thread in which
it was computed to any thread that retrieves it via get.

### 6.3.4 Limitations of parallelizing heterogeneous tasks

Without finding finer-grained parallelism among similar tasks, this approach will yield diminishing returns.

A further problem with dividing heterogeneous tasks among multiple workers is that the tasks may have disparate sizes

The real performance payoff of dividing a program’s workload into tasks comes when there are a large number of
independent, homogeneous tasks that can be processed concurrently.

### 6.3.5 `CompletionService`: `Executor` meets `BlockingQueue`

`CompletionService` combines the functionality of an `Executor` and a `BlockingQueue`. You can submit `Callable` tasks
to it for execution and use the queue-like methods take and poll to retrieve completed results, packaged as `Future`s,
as they become available.

`ExecutorCompletionService` implements `CompletionService`, delegating the computation to an `Executor`.

### 6.3.7 Placing time limits on tasks

The primary challenge in executing tasks within a time budget is making sure that you don’t wait longer than the time
budget to get an answer or find out that one is not forthcoming. `Future.get` supports this requirement: it returns as
soon as the result is ready, but throws `TimeoutException` if the result is not ready within the timeout period.

A secondary problem when using timed tasks is to stop them when they run out of time, so they do not waste computing
resources by continuing to compute a result that will not be used. If a timed `Future.get` completes with
a `TimeoutException`, you can cancel the task through the `Future`. If the task is written to be cancellable, it can be
terminated early so as not to consume excessive resources.

## Summary

Structuring applications around the execution of tasks can simplify development and facilitate concurrency. The Executor
framework permits you to decouple task submission from execution policy and supports a rich variety of execution
policies; whenever you find yourself creating threads to perform tasks, consider using an Executor instead. To maximize
the benefit of decomposing an application into tasks, you must identify sensible task boundaries. In some applications,
the obvious task boundaries work well, whereas in others some analysis may be required to uncover finer-grained
exploitable parallelism.
