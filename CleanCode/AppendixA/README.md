# Apendix A: Concurrency

## Possible Paths of Execution

### Number of Paths

For the simple case of `N` byte-code generated instructions in a sequence, no looping or conditionals, and `T` threads, the total number of possible execution paths is equal to `(NT)!/N!`<sup>`T`</sup>.

## Knowing Your Library

### Nonthread-Safe Classes

There are some classes that are inherently not thread safe. Here are a few examples:

* `SimpleDateFormat`
* Database Connections
* Containers in `java.util`
* Servlets

## Dependencies Between Methods Can Break Concurrent Code

### Server-Based Locking

In general you should prefer server-based locking for these reasons:

* It reduces repeated code: Client-based locking forces each client to lock the server properly. By putting the locking code into the server, clients are free to use the object and not worry about writing additional locking code.
* It allows for better performance—You can swap out a thread-safe server for a non-thread safe one in the case of single-threaded deployment, thereby avoiding all overhead.
* It reduces the possibility of error: All it takes is for one programmer to forget to lock properly.
* It enforces a single policy: The policy is in one place, the server, rather than many places, each client.
* It reduces the scope of the shared variables: The client is not aware of them or how they are locked. All of that is hidden in the server. When things break, the number of places to look is smaller.
* In case you do not own the server code, an ADAPTER can be used to change the API and add locking.
* OR better yet, use the thread-safe collections with extended interfaces.

## Deadlock

There are four conditions required for deadlock to occur:

### Mutual exclusion

Mutual exclusion occurs when multiple threads need to use the same resources and those resources

* Cannot be used by multiple threads at the same time.
* Are limited in number.

### Lock & wait

Once a thread acquires a resource, it will not release the resource until it has acquired all of the other resources it requires and has completed its work.

### No preemption

One thread cannot take resources away from another thread. Once a thread holds a resource, the only way for another thread to get it is for the holding thread to release it.

### Circular wait

This is also referred to as the deadly embrace. Imagine two threads, `T1` and `T2`, and two resources, `R1` and `R2`. `T1` has `R1`, `T2` has `R2`. `T1` also requires `R2`, and `T2` also requires `R1`.

All four of these conditions must hold for deadlock to be possible. Break any one of these conditions and deadlock is not possible.
