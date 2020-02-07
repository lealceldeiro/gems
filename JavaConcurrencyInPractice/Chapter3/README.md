# Chapter 3: Sharing Objects

## 3.1 Visibility

In the absence of synchronization, the compiler, processor, and runtime can do some downright weird things to the order in which operations appear to execute. Attempts to reason about the order in which memory actions “must” happen in insufficiently synchronized multithreaded programs will almost certainly be incorrect.

Always use the proper synchronization whenever data is shared across threads.

### 3.1.1 Stale data

Stale data can cause serious and confusing failures such as unexpected exceptions, corrupted data structures, inaccurate computations, and infinite loops.

### 3.1.2 Nonatomic 64-bit operations

Even if you don’t care about stale values, it is not safe to use shared mutable `long` and `double` variables in multithreaded programs unless they are declared `volatile` or guarded by a lock.

### 3.1.3 Locking and visibility

Locking is not just about mutual exclusion; it is also about memory visibility. To ensure that all threads see the most up-to-date values of shared mutable variables, the reading and writing threads must synchronize on a common lock.

### 3.1.4 Volatile variables

When a field is declared `volatile`, the compiler and runtime are put on notice that this variable is shared and that operations on it should not be reordered with other memory operations. Volatile variables are not cached in registers or in caches where they are hidden from other processors, so a read of a volatile variable always returns the most recent write by any thread.

Use `volatile` variables only when they simplify implementing and verifying your synchronization policy; avoid using volatile variables when veryfing correctness would require subtle reasoning about visibility. Good uses of `volatile` variables include ensuring the visibility of their own state, that of the object they refer to, or indicating that an important lifecycle event (such as initialization or shutdown) has occurred.

Locking can guarantee both visibility and atomicity; volatile variables can only guarantee visibility.

You can use volatile variables only when all the following criteria are met:

* Writes to the variable do not depend on its current value, or you can ensure hat only a single thread ever updates the value;
* The variable does not participate in invariants with other state variables; and
* ocking is not required for any other reason while the variable is being accessed.

## 3.2 Publication and escape

_Publishing_ an object means making it available to code outside of its current scope, such as by storing a reference to it where other code can find it, returning it from a nonprivate method, or passing it to a method in another class.

An object that is published when it should not have been is said to have _escaped_.

Publishing one object may indirectly publish others.

Any object that is reachable from a published object by following some chain of nonprivate field references and method calls has also
been published.

From the perspective of a class `C`, an alien method is one whose behavior is not fully specified by `C`. This includes methods in other classes as well as overrideable methods (neither `private` nor `final`) in `C` itself. Passing an object to an alien method must also be considered publishing that object. Since you can’t know what code will actually be invoked, you don’t know that the alien method won’t publish the object or retain a reference to it that might later be used from another thread.

An object or its internal state is also published when an inner class instance is pusblished, because the inner class instances contain a hidden reference to the enclosing instance. i.e.:
```
public class ThisEscape {
  public ThisEscape(EventSource source) {
    source.registerListener(
      new EventListener() {
        public void onEvent(Event e) {
          doSomething(e);
        }
      }
    );
  }
}
```

### 3.2.1 Safe construction practices

Do not allow the `this` reference to escape during construction. i.e.:
```
public class SafeListener {
  private final EventListener listener;
  
  private SafeListener() {
    listener = new EventListener() {
      public void onEvent(Event e) {
        doSomething(e);
      }
    };
  }
  
  public static SafeListener newInstance(EventSource source) {
    SafeListener safe = new SafeListener();
    source.registerListener(safe.listener);
    return safe;
  }
}
```

## 3.3 Thread confinement

If data is only accessed from a single thread, no synchronization is needed. This technique, _thread confinement_, is one of the
simplest ways to achieve thread safety.

Thread confinement is an element of your program’s design that must be enforced by its implementation. The language and core libraries provide mechanisms that can help in maintaining thread confinement—local variables and the `ThreadLocal` class—but even with these, it is still the programmer’s responsibility to ensure that thread-confined objects do not escape from their intended thread.

### 3.3.1 Ad-hoc thread confinement

_Ad-hoc thread confinement_ describes when the responsibility for maintaining thread confinement falls entirely on the implementation.

Because of its fragility, ad-hoc thread confinement should be used sparingly; if possible, use one of the stronger forms of thread confinment (stack confinement or `ThreadLocal`) instead.

### 3.3.2 Stack confinement

_Stack confinement_ is a special case of thread confinement in which an object can only be reached through local variables.

### 3.3.3 ThreadLocal

ThreadLocal allows you to associate a per-thread value with a value-holding object and provides `get` and `set` accessor methods that maintain a separate copy of the value for each thread that uses it, so a `get` returns the most recent value passed to `set` _from the currently executing thread_.

Like global variables, thread-local variables can detract from reusability and introduce hidden couplings among classes, and should therefore be used with care.

## 3.4 Immutability

Immutable objects are always thread-safe.

An object is immutable if:

* Its state cannot be modified after construction
* All its fields are final
* It is properly constructed (the `this` reference does not escape during construction)

### 3.4.1 Final fields

Just as it is a good practice to make all fields `private` unless they need greater visibility, it is a good practice to make all fields `final` unless they need to be mutable.

### 3.4.2 Example: Using `volatile` to publish immutable objects

Whenever a group of related data items must be acted on atomically, consider creating an immutable holder class for them. Race conditions in accessing or updating multiple related variables can be eliminated by using an immutable object to hold all the variables.

## 3.5 Safe publication

### 3.5.1 Improper publication: when good objects go bad

When synchronization is not used to make an object instance visible to other threads, we say the object was _not properly published_.

### 3.5.2 Immutable objects and initialization safety

Because immutable objects are so important, the Java Memory Model offers a special guarantee of _initialization safety_ for sharing immutable objects.

_Immutable_ objects can be used safely by any thread without additional synchronization, even when synchronization is not used to publish them.

### 3.5.3 Safe publication idioms

To publish an object safely, both the reference to the object and the object’s state must be made visible to other threads at the same time. A properly constructed object can be safely published by:

* Initializing an object reference from a static initializer;
* Storing a reference to it into a volatile field or AtomicReference;
* Storing a reference to it into a final field of a properly constructed object; or
* Storing a reference to it into a field that is properly guarded by a lock

The thread-safe library collections offer the following safe publication guarantees:

* Placing a key or value in a `Hashtable`, `SynchronizedMap`, or `ConcurrentMap` safely publishes it to any thread that retrieves it from the `Map` (whether directly or via an iterator);
* Placing an element in a `Vector`, `CopyOnWriteArrayList`, `CopyOnWriteArraySet`, `SynchronizedList`, or `SynchronizedSet` safely publishes it to any thread that retrieves it from the collection;
* Placing an element on a `BlockingQueue` or a `ConcurrentLinkedQueue` safely publishes it to any thread that retrieves it from the queue.

### 3.5.4 Effectively immutable objects
