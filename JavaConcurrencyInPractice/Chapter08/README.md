# Chapter 8: Applying Thread Pools

## 8.1 Implicit couplings between tasks and execution policies

While the Executor framework offers substantial flexibility in specifying and modifying execution policies, not all tasks are compatible with all execution policies.

Types of tasks that require specific execution policies include:

* Dependent tasks
* Tasks that exploit thread confinement
* Response-time-sensitive tasks
* Tasks that use ThreadLocal

Thread pools work best when tasks are homogeneous and independent. Mixing long-running and short-running tasks risks “clogging” the pool unless it is very large; submitting tasks that depend on other tasks risks deadlock unless the pool is unbounded.

Some tasks have characteristics that require or preclude a specific execution policy. Tasks that depend on other tasks require that the thread pool be large enough that tasks are never queued or rejected; tasks that exploit thread confinement require sequential execution. Document these requirements so that future maintainers do not undermine safety or liveness by substituting an incompatible execution policy.

### 8.1.1 Thread starvation deadlock

If tasks that depend on other tasks execute in a thread pool, they can deadlock.

In a single-threaded executor, a task that submits another task to the same executor and waits for its result will always deadlock.

The same thing can happen in larger thread pools if all threads are executing tasks that are blocked waiting for other tasks still on the work queue.

This is called _thread starvation deadlock_, and can occur whenever a pool task initiates an unbounded blocking wait for some resource or condition that can succeed only through the action of another pool task, unless you can guarantee that the pool is large enough.

Whenever you submit to an `Executor` tasks that are not independent, be aware of the possibility of thread starvation deadlock, and document any pool sizing or configuration constraints in the code or configuration file where the `Executor` is configured.

### 8.1.2 Long-running tasks

Thread pools can have responsiveness problems if tasks can block for extended periods of time.

If the pool size is too small relative to the expected steady-state number of long-running tasks, eventually all the pool threads will be running long-running tasks and responsiveness will suffer.

One technique that can mitigate the ill effects of long-running tasks is for tasks to use timed resource waits instead of unbounded waits.

If a thread pool is frequently full of blocked tasks, this may also be a sign that the pool is too small.

## 8.2 Sizing thread pools

The ideal size for a thread pool depends on the types of tasks that will be submitted and the characteristics of the deployment system. Thread pool sizes should rarely be hard-coded; instead pool sizes should be provided by a configuration mechanism or computed dynamically by consulting `Runtime.availableProcessors`.

If you have different categories of tasks with very different behaviors, consider using multiple thread pools so each can be tuned according to its workload.

For compute-intensive tasks, an N<sub>_cpu_</sub>-processor system usually achieves optimum utilization with a thread pool of N<sub>_cpu_</sub> + 1 threads.

Given these definitions:

N<sub>cpu</sub> = number of CPUs

U<sub>cpu</sub> = target CPU utilization, 0 ≤ U cpu ≤ 1

W/C = ratio of wait time to compute time

The optimal pool size for keeping the processors at the desired utilization is:

N<sub>threads</sub> = N<sub>cpu</sub> ∗ U<sub>cpu</sub> ∗ (1 + W/C)

The number of CPUs using Runtime can be determined as follow: 

`int N_CPUS = Runtime.getRuntime().availableProcessors();`

Other resources that can contribute to sizing constraints are memory, file handles, socket handles, and database connections. To calculating pool size constraints for these types of resources just add up how much of that resource each task requires and divide that into the total quantity available. The result will be an upper bound on the pool size.

## 8.3 Configuring `ThreadPoolExecutor`

`ThreadPoolExecutor` provides the base implementation for the executors returned by the `newCachedThreadPool`, `newFixedThreadPool`, and `newScheduledThreadExecutor` factories in `Executors`.

### 8.3.1 Thread creation and teardown

The maximum pool size is the upper bound on how many pool threads can be active at once. A thread that has been idle for longer than the keep-alive time becomes a candidate for reaping and can be terminated if the current pool size exceeds the core size.

### 8.3.2 Managing queued tasks

`ThreadPoolExecutor` allows you to supply a `BlockingQueue` to hold tasks awaiting execution. There are three basic approaches to task queueing: unbounded queue, bounded queue, and synchronous handoff. The choice of queue interacts with other configuration parameters such as pool size.

Bounded queues help prevent resource exhaustion but introduce the question of what to do with new tasks when the queue is full. (There are a number of possible _saturation policies_ for addressing this problem).

For very large or unbounded pools, you can also bypass queueing entirely and instead hand off tasks directly from producers to worker threads using a `SynchronousQueue`.

The `newCachedThreadPool` factory is a good default choice for an `Executor`, providing better queuing performance than a fixed thread pool. A fixed size thread pool is a good choice when you need to limit the number of concurrent tasks for resource-management purposes, as in a server application that accepts requests from network clients and would otherwise be vulnerable to overload.

Bounding either the thread pool or the work queue is suitable only when tasks are independent. With tasks that depend on other tasks, bounded thread pools or queues can cause thread starvation deadlock; instead, use an unbounded pool configuration like `newCachedThreadPool`.

### 8.3.3 Saturation policies

When a bounded work queue fills up, the _saturation policy_ comes into play.

The saturation policy for a `ThreadPoolExecutor` can be modified by calling `setRejectedExecutionHandler`.

Several implementations of `RejectedExecutionHandler` are provided, each implementing a different saturation policy: `AbortPolicy`, `CallerRunsPolicy`, `DiscardPolicy`, and `DiscardOldestPolicy`.

The default policy, _abort_, causes execute to throw the unchecked `RejectedExecutionException`.

The _discard policy_ silently discards the newly submitted task if it cannot be queued for execution.

The _discard-oldest_ policy discards the task that would otherwise be executed next and tries to resubmit the
new task.

The _caller-runs_ policy implements a form of throttling that neither discards tasks nor throws an exception, but instead tries to slow down the flow of new tasks by pushing some of the work back to the caller.

Example of creating a fixed-size thread pool with the caller-runs saturation policy:

```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
  N_THREADS,
  N_THREADS,
  0L,
  TimeUnit.MILLISECONDS,
  new LinkedBlockingQueue<Runnable>(CAPACITY)
);
executor.setRejectedExecutionHandler( new ThreadPoolExecutor.CallerRunsPolicy());
```

### 8.3.4 Thread factories

Whenever a thread pool needs to create a thread, it does so through a _thread factory_.

Specifying a thread factory allows you to customize the configuration of pool threads.

### 8.3.5 Customizing `ThreadPoolExecutor` after construction

Most of the options passed to the `ThreadPoolExecutor` constructors can also be modified after construction via setters (such as the core thread pool size, maximum thread pool size, keep-alive time, thread factory, and rejected execution handler).

If the `Executor` is created through one of the factory methods in `Executors` (except `newSingleThreadExecutor`), you can cast the result to `ThreadPoolExecutor` to access the setters.

## 8.4 Extending `ThreadPoolExecutor`

`ThreadPoolExecutor` was designed for extension, providing several “hooks” for subclasses to override— beforeExecute, afterExecute, and terminated —that can be used to extend the behavior of `ThreadPoolExecutor`.

## 8.5 Parallelizing recursive algorithms

Sequential loop iterations are suitable for parallelization when each iteration is independent of the others and the work done in each iteration of the loop body is significant enough to offset the cost of managing a new task.

## Summary

The Executor framework is a powerful and flexible framework for concurrently executing tasks. It offers a number of tuning options, such as policies for creating and tearing down threads, handling queued tasks, and what to do with excess tasks, and provides several hooks for extending its behavior. As in most powerful frameworks, however, there are combinations of settings that do not work well together; some types of tasks require specific execution policies, and some combinations of tuning parameters may produce strange results.
