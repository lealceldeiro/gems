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
