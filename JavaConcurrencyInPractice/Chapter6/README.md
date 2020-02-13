# Chapter 6: Task Execution

## 6.1 Executing tasks in threads

The first step in organizing a program around task execution is identifying sensible _task boundaries_.

Ideally, tasks are independent activities: work that doesn’t depend on the state, result, or side effects of other tasks. Independence facilitates concurrency, as independent tasks can be executed in parallel if there areadequate processing resources.

Choosing good task boundaries, coupled with a sensible _task execution policy_ can help achieve _good throughput_, _good responsiveness_ and _graceful degradation_ in server applications.

### 6.1.1 Executing tasks sequentially

In server applications, sequential processing rarely provides either good throughput or good responsiveness. There are exceptions—such as when tasks are few and long-lived, or when the server serves a single client that makes only a single request at a time—but most server applications do not work this way

### 6.1.2 Explicitly creating threads for tasks

Under light to moderate load, the thread-per-task approach is an improvement over sequential execution. As long as the request arrival rate does not exceed the server’s capacity to handle requests, this approach offers better responsiveness and throughput.

### 6.1.3 Disadvantages of unbounded thread creation

For production use, however, the thread-per-task approach has some practical drawbacks, especially when a large number of threads may be created:

* Thread lifecycle overhead.
* Resource consumption.
* Stability.

Unbounded thread creation may appear to work just fine during prototyping and development, with problems surfacing only when the application is deployed and under heavy load.

## 6.2 The Executor framework

Thread pools offer the benefit of the usage of _bounded queues_ to prevent an overloaded application from running out of memory for thread management, and `java.util.concurrent` provides a flexible thread pool implementation as part of the _Executor_ framework.

Using an Executor is usually the easiest path to implementing a producer-consumer design in your application.
