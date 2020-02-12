* _It’s the mutable state_: All concurrency issues boil down to coordinating access to mutable state. The less mutable state, the easier it is to ensure thread safety.
* _Make fields `final` unless they need to be mutable_.
* _Immutable objects are automatically thread-safe_: Immutable objects simplify concurrent programming tremendously. They are simpler and safer, and can be shared freely without locking or defensive copying.
* _Encapsulation makes it practical to manage the complexity_: You could write a thread-safe program with all data stored in global variables, but why would you want to? Encapsulating data within objects makes it easier to preserve their invariants; encapsulating synchronization within objects makes it easier to comply with their synchronization policy.
* _Guard each mutable variable with a lock_.
* _Guard all variables in an invariant with the same lock_.
* _Hold locks for the duration of compound actions_.
* _A program that accesses a mutable variable from multiple threads without synchronization is a broken program_.
* _Don’t rely on clever reasoning about why you don’t need to synchronize_.
* _Include thread safety in the design process—or explicitly document that your class is not thread-safe_.
* _Document your synchronization policy_.
