# Chapter 1: Introduction

## 1.1 A (very) brief history of concurrency

Threads allow multiple streams of program control flow to coexist within a process. They share process-wide resources such as memory and file handles, but each thread has its own program counter, stack, and local variables. Threads also provide a natural decomposition for exploiting hardware parallelism on multi-processor systems; multiple threads within the same program can be scheduled simultaneously on multiple CPUs.

Threads are sometimes called lightweight processes, and most modern operating systems treat threads, not processes, as the basic units of scheduling.

## 1.3 Risks of threads

### 1.3.1 Safety hazards

Thread safety can be unexpectedly subtle because, in the absence of sufficient synchronization, the ordering of operations in multiple threads is unpredictable and sometimes surprising.

In the absence of synchronization, the compiler, hardware, and runtime are allowed to take substantial liberties with the timing and ordering of actions, such as caching variables in registers or processor-local caches where they are temporarily (or even permanently) invisible to other threads.

### 1.3.2 Liveness hazards

The use of threads introduces additional safety hazards not present in single-threaded programs. Similarly, the use of threads introduces additional forms of _liveness failure_ that do not occur in single-threaded programs.

While _safety_ means “nothing bad ever happens”, liveness concerns the complementary goal that “something good eventually happens”. A liveness failure occurs when an activity gets into a state such that it is permanently unable to make forward progress (deadlock, starvation, livelock)

### 1.3.3 Performance hazards

Performance issues subsume a broad range of problems, including poor service time, responsiveness, throughput, resource consumption, or scalability.

## 1.4 Threads are everywhere

Frameworks introduce concurrency into applications by calling application components from framework threads. Components invariably access application state, thus requiring that _all_ code paths accessing that state be thread-safe.

The facilities described below all cause application code to be called from threads not managed by the application:

* Timer
* Servlets and JavaServer Pages (JSPs)
* Remote Method Invocation
* Swing and AWT
