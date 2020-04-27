# Chapter 7: Error Handling

Error handling is important, but if it obscures logic, it’s wrong.

## Use Exceptions Rather Than Return Codes

The approach of setting an error flag or returning an error code that the caller must handle, for error handling, clutters the caller and it easy to forget.

It is better to throw an exception when an error is encountered. The calling code is cleaner and its logic is not obscured by error handling.

## Write Your `try`-`catch`-`finally` Statement First

In a way, `try` blocks are like transactions. Your `catch` has to leave your program in a consistent state, no matter what happens in the `try`. For this reason it is good practice to start with a `try`-`catch`-`finally` statement when you are writing code that could throw exceptions. This helps you define what the user of that code should expect, no matter what goes wrong with the code that is executed in the try.

## Use Unchecked Exceptions

The price of checked exceptions is an Open/Closed Principle violation. If you throw a checked exception from a method in your code and the `catch` is three levels above, _you must declare that exception in the signature of each method between you and
the `catch`_. This means that a change at a low level of the software can force signature changes on many higher levels.

Checked exceptions can sometimes be useful if you are writing a critical library: You must catch them. But in general application development the dependency costs outweigh the benefits.

## Provide Context with Exceptions

