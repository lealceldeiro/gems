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



-----

<sup><sub>1. Serializing access to an object has nothing to do with object serialization (turning an object into a byte stream); serializing access means that threads take turns accessing the object exclusively, rather than doing so concurrently.</sup></sub>
