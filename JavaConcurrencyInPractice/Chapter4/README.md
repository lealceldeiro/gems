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
