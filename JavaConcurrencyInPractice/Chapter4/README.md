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
