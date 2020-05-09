# Apendix A: Concurrency

## Possible Paths of Execution

### Number of Paths

For the simple case of `N` byte-code generated instructions in a sequence, no looping or conditionals, and `T` threads, the total number of possible execution paths is equal to `(NT)!/N!`<sup>`T`</sup>.

## Knowing Your Library

### Nonthread-Safe Classes

There are some classes that are inherently not thread safe. Here are a few examples:

* `SimpleDateFormat`
* Database Connections
* Containers in `java.util`
* Servlets
