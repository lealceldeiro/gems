# Chapter 6: Task Execution

## 6.1 Executing tasks in threads

The first step in organizing a program around task execution is identifying sensible _task boundaries_.

Ideally, tasks are independent activities: work that doesn’t depend on the state, result, or side effects of other tasks. Independence facilitates concurrency, as independent tasks can be executed in parallel if there areadequate processing resources.

Choosing good task boundaries, coupled with a sensible _task execution policy_ can help achieve _good throughput_, _good responsiveness_ and _graceful degradation_ in server applications.

### 6.1.1 Executing tasks sequentially

