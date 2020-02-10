# Chapter 5: Building Blocks

## 5.1 Synchronized collections

The _synchronized collection_ classes include `Vector` and `Hashtable` , part of the original JDK, as well as their cousins added in JDK 1.2, the synchronized wrapper classes created by the `Collections.synchronizedXxx` factory methods. These classes achieve thread safety by encapsulating their state and synchronizing every public method so that only one thread at a time can access the collection state.

### 5.1.1 Problems with synchronized collections

With a synchronized collection, some compound actions (such as iteration, navigation, and conditional operations such as put-if-absent) are still technically thread-safe even without client-side locking, but they may not behave as you might expect when other threads can concurrently modify the collection.

### 5.1.2 Iterators and `ConcurrentModificationException`

The iterators returned by the synchronized collections are not designed to deal with concurrent modification, and they are fail-fast—meaning that if they detect that the collection has changed since iteration began, they throw the unchecked `ConcurrentModificationException`.

### 5.1.3 Hidden iterators
