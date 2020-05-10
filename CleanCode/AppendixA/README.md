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
