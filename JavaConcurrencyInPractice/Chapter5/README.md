# Chapter 5: Building Blocks

## 5.1 Synchronized collections

The _synchronized collection_ classes include `Vector` and `Hashtable` , part of the original JDK, as well as their cousins added in JDK 1.2, the synchronized wrapper classes created by the `Collections.synchronizedXxx` factory methods. These classes achieve thread safety by encapsulating their state and synchronizing every public method so that only one thread at a time can access the collection state.

### 5.1.1 Problems with synchronized collections

With a synchronized collection, some compound actions (such as iteration, navigation, and conditional operations such as put-if-absent) are still technically thread-safe even without client-side locking, but they may not behave as you might expect when other threads can concurrently modify the collection.

### 5.1.2 Iterators and `ConcurrentModificationException`

The iterators returned by the synchronized collections are not designed to deal with concurrent modification, and they are fail-fast—meaning that if they detect that the collection has changed since iteration began, they throw the unchecked `ConcurrentModificationException`.

### 5.1.3 Hidden iterators

The greater the distance between the state and the synchronization that guards it, the more likely that someone will forget to use proper synchronization when accessing that state.

Just as encapsulating an object’s state makes it easier to preserve its invariants, encapsulating its synchronization makes it easier to enforce its synchronization policy.

## 5.2 Concurrent collections

Synchronized collections achieve their thread safety by serializing **<sup><sub>1</sup></sub>** all access to the collection’s state. The cost of this approach is poor concurrency; when multiple threads contend for the collection-wide lock, throughput suffers. The concurrent collections, on the other hand, are designed for concurrent access from multiple threads.

Replacing synchronized collections with concurrent collections can offer dramatic scalability improvements with little risk.

### 5.2.1 `ConcurrentHashMap`

`ConcurrentHashMap` , along with the other concurrent collections, further improve on the synchronized collection classes by providing iterators that do not throw `ConcurrentModificationException` , thus eliminating the need to lock the collection during iteration.

The iterators returned by `ConcurrentHashMap` are _weakly consistent_ instead of _fail-fast_. A weakly consistent iterator can tolerate concurrent modification, traverses elements as they existed when the iterator was constructed, and may (but is not guaranteed to) reflect modifications to the collection after the construction of the iterator.

Because it has so many advantages and so few disadvantages compared to `Hashtable` or `SynchronizedMap` , replacing synchronized Map implementations with `ConcurrentHashMap` in most cases results only in better scalability. Only if your application needs to lock the map for exclusive access is `ConcurrentHashMap` not an appropriate drop-in replacement.

### 5.2.3 `CopyOnWriteArrayList`

`CopyOnWriteArrayList` is a concurrent replacement for a synchronized `List` that offers better concurrency in some common situations and eliminates the need to lock or copy the collection during iteration (similarly, `CopyOnWriteArraySet` is a
concurrent replacement for a synchronized `Set`).

The _copy-on-write_ collections derive their thread safety from the fact that as long as an effectively immutable object is properly published, no further synchronization is required when accessing it. They implement mutability by creating and republishing a new copy of the collection every time it is modified. The iterators returned by the _copy-on-write_ collections do not throw `ConcurrentModificationException` and return the elements exactly as they were at the time the iterator was created, regardless of subsequent modifications.

The copy-on-write collections are reasonable to use only when iteration is far more common than modification.

## 5.3 Blocking queues and the producer-consumer pattern

Blocking queues support the _producer-consumer_ design pattern.

A producer-consumer design separates the identification of work to be done from the execution of that work by placing work items on a “to do” list for later processing, rather than processing them immediately as they are identified.

The producer-consumer pattern simplifies development because it removes code dependencies between producer and consumer classes, and simplifies workload management by decoupling activities that may produce or consume data at different or variable rates.

Bounded queues are a powerful resource management tool for building reliable applications: they make your program more robust to overload by throttling activities that threaten to produce more work than can be handled.

Build resource management into your design early using blocking queues—it is a lot easier to do this up front than to retrofit it later.

### 5.3.2 Serial thread confinement

For mutable objects, producer-consumer designs and blocking queues facilitate _serial thread confinement_ for handing off ownership of objects from producers to consumers.

A thread-confined object is owned exclusively by a single thread, but that ownership can be “transferred” by publishing it safely where only one other thread will gain access to it and ensuring that the publishing thread does not access it after the handoff.

### 5.3.3 Deques and work stealing

Just as blocking queues lend themselves to the producer-consumer pattern, deques lend themselves to a related pattern called _work stealing_.

A producer-consumer design has one shared work queue for all consumers; in a work stealing design, every consumer has its own deque. If a consumer exhausts the work in its own deque, it can steal work from the _tail_ of someone else’s deque.

## 5.4 Blocking and interruptible methods

When a method can throw `InterruptedException` , it is telling you that it is a blocking method, and further that if it is _interrupted_, it will make an effort to stop blocking early.

_Interruption_ is a _cooperative_ mechanism. One thread cannot force another to stop what it is doing and do something else; when thread `A` interrupts thread `B`, `A` is merely requesting that `B` stop what it is doing when it gets to a convenient stopping point—if it feels like it.

When your code calls a method that throws `InterruptedException` , then your method is a blocking method too, and must have a plan for responding to interruption. For library code, there are basically two choices:

 * Propagate the `InterruptedException`
 * Restore the interrupt
 
There is one thing you should _not_ do with `InterruptedException`—catch it and do nothing in response. The only situation in which it is acceptable to swallow an interrupt is when you are extending `Thread` and therefore control all the code higher up on the call stack.

## 5.5 Synchronizers

A _synchronizer_ is any object that coordinates the control flow of threads based on its state. Blocking queues can act as synchronizers; other types of synchronizers include semaphores, barriers, and latches.

### 5.5.1 Latches

A _latch_ is a synchronizer that can delay the progress of threads until it reaches its terminal state. It acts as a gate: until the latch reaches the terminal state the gate is closed and no thread can pass, and in the terminal state the gate opens, allowing all threads to pass. i.e.: `CountDownLatch`.

### 5.5.2 `FutureTask`

`FutureTask` also acts like a latch.

The behavior of `Future.get` depends on the state of the task. If it is completed, get returns the result immediately, and otherwise blocks until the task transitions to the completed state and then returns the result or throws an exception.

### 5.5.3 Semaphores

_Counting semaphores_ are used to control the number of activities that can access a certain resource or perform a given action at the same time.

A binary semaphore can be used as a mutex with nonreentrant locking semantics; whoever holds the sole permit holds the mutex.

Semaphores are useful for implementing resource pools such as database connection pools.

A Semaphore can also be used to turn any collection into a blocking bounded collection. The semaphore is initialized to the desired maximum size of the collection. The `add` operation acquires a permit before adding the item into the underlying collection. If the underlying `add` operation does not actually add anything, it releases the permit immediately. Similarly, a successful `remove` operation releases a permit, enabling more elements to be added.

### 5.5.4 Barriers

_Barriers_ are similar to latches in that they block a group of threads until some event has occurred. The key difference is that with a barrier, all the threads must come together at a barrier point _at the same time_ in order to proceed.

Latches are for waiting for _events_; barriers are for waiting for other _threads_.

`CyclicBarrier` allows a fixed number of parties to rendezvous repeatedly at a barrier point and is useful in parallel iterative algorithms that break down a problem into a fixed number of independent subproblems.

If a barrier is considered _broken_ (a call to `await` times out or a thread blocked in `await` is interrupted) all outstanding calls to `await` terminate with `BrokenBarrierException`. If a barrier is successfully passed, `await` returns a unique arrival index for each thread, which can be used to “elect” a leader that takes some special action in the next iteration.

## 5.6 Building an efficient, scalable result cache

```
public interface Computable<A, V> {
  V compute(A arg) throws InterruptedException;
}

public class ExpensiveFunction implements Computable<String, BigInteger> {
  public BigInteger compute(String arg) {
    // after deep thought...
    return new BigInteger(arg);
  }
}

public class Memoizer<A, V> implements Computable<A, V> {
	private final ConcurrentMap<A, Future<V>> cache	= new ConcurrentHashMap<A, Future<V>>();
	private final Computable<A, V> c;
 
	public Memoizer(Computable<A, V> c) { this.c = c; }
 
	public V compute(final A arg) throws InterruptedException {
		while (true) {
			Future<V> f = cache.get(arg);
			if (f == null) {
				Callable<V> eval = new Callable<V>() {
					public V call() throws InterruptedException {
						return c.compute(arg);
					}
				};
				FutureTask<V> ft = new FutureTask<V>(eval);
				f = cache.putIfAbsent(arg, ft);
				if (f == null) { f = ft; ft.run(); }
			}
			try {
				return f.get();
			} catch (CancellationException e) {
				cache.remove(arg, f);
			} catch (ExecutionException e) {
				throw launderThrowable(e.getCause());
			}
		}
	}
}

@ThreadSafe
public class Factorizer implements Servlet {
	private final Computable<BigInteger, BigInteger[]> c =	new Computable<BigInteger, BigInteger[]>() {
		public BigInteger[] compute(BigInteger arg) {
			return factor(arg);
		}
	};
	private final Computable<BigInteger, BigInteger[]> cache	= new Memoizer<BigInteger, BigInteger[]>(c);
 
	public void service(ServletRequest req,
		ServletResponse resp) {
		try {
			BigInteger i = extractFromRequest(req);
			encodeIntoResponse(resp, cache.compute(i));
		} catch (InterruptedException e) {
			encodeError(resp, "factorization interrupted");
		}
	}
}
```
-----

<sup><sub>1. Serializing access to an object has nothing to do with object serialization (turning an object into a byte stream); serializing access means that threads take turns accessing the object exclusively, rather than doing so concurrently.</sup></sub>
