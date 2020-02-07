# Chapter 3: Sharing Objects

## 3.1 Visibility

In the absence of synchronization, the compiler, processor, and runtime can do some downright weird things to the order in which operations appear to execute. Attempts to reason about the order in which memory actions “must” happen in insufficiently synchronized multithreaded programs will almost certainly be incorrect.

Always use the proper synchronization whenever data is shared across threads.

### 3.1.1 Stale data

Stale data can cause serious and confusing failures such as unexpected exceptions, corrupted data structures, inaccurate computations, and infinite loops.

### 3.1.2 Nonatomic 64-bit operations
