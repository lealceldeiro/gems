# Chapter 11: Performance and Scalability

## 11.1 Thinking about performance

Improving performance means doing more work with fewer resources.

When the performance of an activity is limited by availability of a particular resource, we say it is bound by that resource: CPU-bound, database-bound, etc.

In using concurrency to achieve better performance, we are trying to do two things: utilize the processing resources we have more effectively, and enable our program to exploit additional processing resources if they become available.

### 11.1.1 Performance versus scalability

Scalability describes the ability to improve throughput or capacity when additional computing resources (such as additional CPUs, memory, storage, or I/O bandwidth) are added.

### 11.1.2 Evaluating performance tradeoffs

Most optimizations are premature: _they are often undertaken before a clear set of requirements is available_.

Avoid premature optimization. First make it right, then make it fast—if it is not already fast enough.

Before deciding that one approach is “faster” than another, ask yourself some questions:

* What do you mean by “faster”?
* Under what conditions will this approach actually be faster? Under light or heavy load? With large or small data sets? Can you support your answer with measurements?
* How often are these conditions likely to arise in your situation? Can you support your answer with measurements?
* Is this code likely to be used in other situations where the conditions may be different?
* What hidden costs, such as increased development or maintenance risk, are you trading for this improved performance? Is this a good tradeoff?

Measure, don’t guess.

* The free _perfbar_ application can provide a good picture of how busy the CPUs are.

## 11.2 Amdahl’s law

Amdahl’s law describes how much a program can theoretically be sped up by additional computing resources, based on the proportion of parallelizable and serial components.

If `F` is the fraction of the calculation that must be executed serially, then Amdahl’s law says that on a machine with `N` processors, we can achieve a speedup of at most: _`Speedup`_ `<=` `1 / (F + ((1 - F) / N))`.

All concurrent applications have some sources of serialization.

### 11.2.2 Applying Amdahl’s law qualitatively

When evaluating an algorithm, thinking “in the limit” about what would happen with hundreds or thousands of processors can offer some insight into where scaling limits might appear.

## 11.3 Costs introduced by threads

Scheduling and interthread coordination have performance costs; for threads to offer a performance improvement, the performance benefits of parallelization must outweigh the costs introduced by concurrency.

### 11.3.1 Context switching

The `vmstat` command on Unix systems and the `perfmon` tool on Windows systems report the number of context switches and the percentage of time spent in the kernel. High kernel usage (over 10%) often indicates heavy scheduling activity, which may be caused by blocking due to I/O or lock contention.

### 11.3.2 Memory synchronization

The visibility guarantees provided by synchronized and volatile may entail using special instructions called memory barriers that can flush or invalidate caches, flush hardware write buffers, and stall execution pipelines.

Memory barriers may also have indirect performance consequences because they inhibit other compiler optimizations; most operations cannot be reordered with memory barriers.

It is not needed to worry excessively about the cost of uncontended synchronization. The basic mechanism is already quite fast, and JVMs can perform additional optimizations that further reduce or eliminate the cost. Instead, the focus should be on optimization efforts on areas where lock contention actually occurs.

### 11.3.3 Blocking

Uncontended synchronization can be handled entirely within the JVM; contended synchronization may require OS activity, which adds to the cost.

Suspending a thread because it could not get a lock, or because it blocked on a condition wait or blocking I/O operation, entails two additional context switches and all the attendant OS and cache activity: the blocked thread is switched out before its quantum has expired, and is then switched back in later after the lock or other resource becomes available.

## 11.4 Reducing lock contention

The principal threat to scalability in concurrent applications is the exclusive resource lock.

Two factors influence the likelihood of contention for a lock: how often that lock is requested and how long it is held once acquired.

If the product of these factors is sufficiently small, then most attempts to acquire the lock will be uncontended, and lock contention will not pose a significant scalability impediment. If, however, the lock is in sufficiently high demand, threads will block waiting for it.

There are three ways to reduce lock contention:

* Reduce the duration for which locks are held;
* Reduce the frequency with which locks are requested; or
* Replace exclusive locks with coordination mechanisms that permit greater concurrency

### 11.4.1 Narrowing lock scope (“Get in, get out”)

An effective way to reduce the likelihood of contention is to hold locks as briefly as possible. This can be done by moving code that doesn’t require the lock out of synchronized blocks, especially for expensive operations and potentially blocking operations such as I/O.

Because the cost of synchronization is nonzero, breaking one synchronized block into multiple synchronized blocks (correctness permitting) at some point becomes counterproductive in terms of performance.

### 11.4.2 Reducing lock granularity

The other way to reduce the fraction of time that a lock is held (and therefore the likelihood that it will be contended) is to have threads ask for it less often. This can be accomplished by lock splitting and lock striping, which involve using separate locks to guard multiple independent state variables previously guarded by a single lock.

### 11.4.3 Lock striping

Splitting a heavily contended lock into two is likely to result in two heavily contended locks. While this will produce a small scalability improvement by enabling two threads to execute concurrently instead of one, it still does not dramatically improve prospects for concurrency on a system with many processors.

Lock splitting can sometimes be extended to partition locking on a variable-sized set of independent objects, in which case it is called _lock striping_.

One of the downsides of lock striping is that locking the collection for exclusive access is more difficult and costly than with a single lock.

### 11.4.4 Avoiding hot fields

Common optimizations such as caching frequently computed values can introduce “hot fields” that limit scalability.

### 11.4.5 Alternatives to exclusive locks

A third technique for mitigating the effect of lock contention is to forego the use of exclusive locks in favor of a more concurrency-friendly means of managing shared state, such as the concurrent collections, read-write locks, immutable objects and atomic variables.

### 11.4.6 Monitoring CPU utilization

If the CPUs are not fully utilized, it may be because of:

* Insufficent load
* I/O-bound
* Externally bound
* Lock contention

### 11.4.7 Just say no to object pooling

Even taking into account its reduced garbage collection overhead, object pooling has been shown to be a performance loss 14 for all but the most expensive objects (and a serious loss for light- and medium-weight objects) in single-threaded programs.
