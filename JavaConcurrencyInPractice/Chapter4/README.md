# Chapter 4: Composing Objects

## 4.1 Designing a thread-safe class

The design process for a thread-safe class should include these three basic elements:

* Identify the variables that form the object’s state;
* Identify the invariants that constrain the state variables;
* Establish a policy for managing concurrent access to the object’s state.

The state of an object with _n_ primitive fields is just the _n-tuple_ of its field values. If the object has fields that are references to other objects, its state will encompass fields from the referenced objects as well.

The _synchronization policy_ defines how an object coordinates access to its state without violating its invariants or postconditions. It specifies what combination of immutability, thread confinement, and locking is used to maintain thread safety, and which variables are guarded by which locks.

### 4.1.1 Gathering synchronization requirements

If an operation has invalid state transitions, it must be made atomic.

When multiple variables participate in an invariant, the lock that guards them must be held for the duration of any operation that accesses the related variables.

You cannot ensure thread safety without understanding an object’s invariants and postconditions. Constraints on the valid values or state transitions for state variables can create atomicity and encapsulation requirements.

### 4.1.2 State-dependent operations

Operations with state-based preconditions are called state-dependent.

In a single-threaded program, if a precondition does not hold, the operation has no choice but to fail. But in a concurrent program, the precondition may become true later due to the action of another thread.

## 4.1.3 State ownership

When defining which variables form an object’s state, we want to consider only the data that object _owns_.

Ownership is not embodied explicitly in the language, but is instead an element of class design

In many cases, ownership and encapsulation go together—the object encapsulates the state it owns and owns the state it encapsulates.

A class usually does not own the objects passed to its methods or constructors, unless the method is designed to explicitly transfer ownership of objects passed in (such as the synchronized collection wrapper factory methods).

## 4.2 Instance confinement

Encapsulation simplifies making classes thread-safe by promoting _instance confinement_, often just called _confinement_.

Encapsulating data within an object confines access to the data to the object’s methods, making it easier to ensure that the data is always accessed with the appropriate lock held.

The following code snippet shows how confinement and locking can work together to make a class thread-safe even when its component state variables are not.

```
// NOTE: if Person is mutable, additional synchronization will be needed when accessing a Person retrieved from a PersonSet
@ThreadSafe
public class PersonSet {
  @GuardedBy("this")
  private final Set<Person> mySet = new HashSet<>();
  
  public synchronized void addPerson(Person p) {
    mySet.add(p);
  }
  public synchronized boolean containsPerson(Person p) {
    return mySet.contains(p);
  }
}
```

Instance confinement is one of the easiest ways to build thread-safe classes because a class that confines its state can be analyzed for thread safety without having to examine the whole program. It also allows flexibility in the choice of locking strategy and allows different state variables to be guarded by different locks.

### 4.2.1 The Java monitor pattern

An object following the Java monitor pattern encapsulates all its mutable state and guards it with the object’s own intrinsic lock.

The primary advantage of the Java monitor pattern is its simplicity and it is merely a convention; any lock object could be used to guard an object’s state so long as it is used consistently.

There are advantages to using a private lock object instead of an object’s intrinsic lock: Making the lock object private
encapsulates the lock so that client code cannot acquire it, whereas a publicly accessible lock allows client code to participate in its synchronization policy—correctly or incorrectly.

## 4.3 Delegating thread safety

If the components of our class are already thread-safe, _sometimes_ we need to add an additional layer of thread safety, but _not always_.

### 4.3.2 Independent state variables

We can delegate thread safety to more than one underlying state variable as long as those underlying state variables are _independent_, meaning that the composite class does not impose any invariants involving the multiple state variables.

### 4.3.3 When delegation fails

If a class is composed of multiple independent thread-safe state variables and has no operations that have any invalid state transitions, then it can delegate thread safety to the underlying state variables.

### 4.3.4 Publishing underlying state variables

The conditions under where we can publish an object’s underlying state variables so that other classes can modify them as well, depends on what invariants the class imposes on those variables.

If a state variable is thread-safe, does not participate in any invariants that constrain its value, and has no prohibited state transitions for any of its operations, then it can safely be published.

## 4.4 Adding functionality to existing thread-safe classes

The safest way to add a new atomic operation is to modify the original class to support the desired operation, but this is not always possible because you may not have access to the source code or may not be free to modify it. If you can modify the original class, you need to understand the implementation’s synchronization policy so that you can enhance it in a manner consistent with its original design.

Another approach is to extend the class, assuming it was designed for extension. Extension is more fragile than adding code directly to a class, because the implementation of the synchronization policy is now distributed over multiple, separately maintained source files. If the underlying class were to change its synchronization policy by choosing a different lock to guard its state variables, the subclass would subtly and silently break, because it no longer used the right lock to control concurrent access to the base class’s state.

### 4.4.1 Client-side locking

A third strategy is to extend the functionality of the class without extending the class itself by placing extension code in a “helper” class.

Client-side locking entails guarding client code that uses some object X with the lock X uses to guard its own state. In order to use client-side locking, you must know what lock X uses. Example (_ put-if-absent with client-side locking_):

```
@ThreadSafe
public class ListHelper<E> {
  public List<E> list = Collections.synchronizedList(new ArrayList<E>());
  // ...
  public boolean putIfAbsent(E x) {
    synchronized (list) {
      boolean absent = !list.contains(x);
      if (absent) {
        list.add(x);
      }
      return absent;
    }
  }
}
```
If extending a class to add another atomic operation is fragile because it distributes the locking code for a class over multiple classes in an object hierarchy, client-side locking is even more fragile because it entails putting locking code for class `C` into classes that are totally unrelated to `C`.

### 4.4.2 Composition

There is a less fragile alternative for adding an atomic operation to an existing class: _composition_. Example:

```
@ThreadSafe
public class ImprovedList<T> implements List<T> {
  private final List<T> list;
  
  public ImprovedList(List<T> list) { this.list = list; }
  
  public synchronized boolean putIfAbsent(T x) {
    boolean contains = list.contains(x);
    if (contains)
    list.add(x);
    return !contains;
  }
  
  public synchronized void clear() { list.clear(); }
  
  // ... similarly delegate other List methods
}
```

## 4.5 Documenting synchronization policies

Document a class’s thread safety guarantees for its clients; document its synchronization policy for its maintainers.

Each use of `synchronized`, `volatile`, or any thread-safe class reflects a _synchronization policy_ defining a strategy for ensuring the integrity of data in the face of concurrent access. That policy is an element of your program’s design, and should be documented.

### 4.5.1 Interpreting vague documentation
