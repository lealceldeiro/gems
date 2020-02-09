# Chapter 4: Composing Objects

## 4.1 Designing a thread-safe class

The design process for a thread-safe class should include these three basic elements:

* Identify the variables that form the object’s state;
* Identify the invariants that constrain the state variables;
* Establish a policy for managing concurrent access to the object’s state.

The state of an object with _n_ primitive fields is just the _n-tuple_ of its field values. If the object has fields that are references to other objects, its state will encompass fields from the referenced objects as well.

The _synchronization policy_ defines how an object coordinates access to its state without violating its invariants or postconditions. It specifies what combination of immutability, thread confinement, and locking is used to maintain thread safety, and which variables are guarded by which locks.

### 4.1.1 Gathering synchronization requirements
