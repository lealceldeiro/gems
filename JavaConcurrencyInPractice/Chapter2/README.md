# Chapter 2

Writing thread-safe code is, at its core, about managing access to _state_, and in particular to _shared, mutable state_.

By _shared_, we mean that a variable could be accessed by multiple threads; by _mutable_, we mean that its value could change during its lifetime. We may talk about thread safety as if it were about _code_, but what we are really trying to do is protect _data_ from uncontrolled concurrent access.

Whenever more than one thread accesses a given state variable, and one of them might write to it, they all must coordinate their access to it using synchronization.

If multiple threads access the same mutable state variable without appropriate synchronization, _your program is broken_. There are three ways to fix it:

* _Don’t share_ the state variable across threads;
* Make the state variable _immutable_; or
* Use _synchronization_ whenever accessing the state variable.

It is far easier to design a class to be thread-safe than to retrofit it for thread safety later.

When designing thread-safe classes, good object-oriented techniques—encapsulation, immutability, and clear specification of invariants—are your best friends.

## 2.1 What is thread safety?

A class is _thread-safe_ if it behaves correctly when accessed from multiple threads, regardless of the scheduling or interleaving of the execution of those threads by the runtime environment, and with no additional synchronization or other coordination on the part of the calling code.

No set of operations performed sequentially or concurrently on instances of a thread-safe class can cause an instance to be in an invalid state.

Thread-safe classes encapsulate any needed synchronization so that clients need not provide their own.

### 2.1.1 Example: a stateless servlet

Stateless objects are always thread-safe.

## 2.2 Atomicity

The possibility of incorrect results in the presence of unlucky timing is so important in concurrent programming that it has a name: a _race condition_.

<details><summary>_race condition_ or _data race_? </summary>The term _race condition_ is often confused with the related term _data race_, which arises when synchronization is not used to coordinate all access to a shared nonfinal field. You risk a data race whenever a thread writes a variable that might next be read by another thread or reads a variable that might have last been written by another thread if both threads do not use synchronization; code with data races has no useful defined semantics under the Java Memory Model. Not all race conditions are data races, and not all data races are race conditions, but they both can cause concurrent programs to fail in unpredictable ways.</details>

### 2.2.1 Race conditions

The most common type of race condition is _check-then-act_, where a potentially stale observation is used to make a decision on what to do next.

Using a potentially stale observation to make a decision or perform a computation is what characterizes most race conditions. This type of race condition is called _check-then-act_: you observe something to be true (file X doesn’t exist) and then take action based on that observation (create X); but in fact the observation could have become invalid between the time you observed it and the time you acted on it (someone else created X in the meantime), causing a problem (unexpected exception, overwritten data, file corruption).
