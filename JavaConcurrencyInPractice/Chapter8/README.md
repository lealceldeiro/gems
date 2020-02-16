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

## 8.3 Configuring ThreadPoolExecutor
