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

Each exception that you throw should provide enough context to determine the source and location of an error.

Create informative error messages and pass them along with your exceptions. Mention the operation that failed and the type of failure. If you are logging in your application, pass along enough information to be able to log the error in your `catch`.

## Define Exception Classes in Terms of a Caller’s Needs

We can classify them by their source or their type. However, when we define exception classes in an application, our most important concern should be how they are caught.

Often a single exception class is fine for a particular area of code. The information sent with the exception can distinguish the errors. Use different classes only if there are times when you want to catch one exception and allow the other one to pass through.

## Define the Normal Flow

In the presence of the Special Case Pattern, you create a class or configure an object so that it handles a special case for you. When you do, the client code doesn’t have to deal with exceptional behavior. That behavior is encapsulated in the special case object.

## Don’t Return Null

When we return `null`, we are essentially creating work for ourselves and foisting problems upon our callers.

If you are calling a `null`-returning method from a third-party API, consider wrapping that method with a method that either throws an exception or returns a special case object.

## Don’t Pass Null

Returning `null` from methods is bad, but passing `null` into methods is worse. Unless you are working with an API which expects you to pass `null`, you should avoid passing `null` in your code whenever possible.

## Conclusion

Clean code is readable, but it must also be robust. These are not conflicting goals. We can write robust clean code if we see error handling as a separate concern, something that is viewable independently of our main logic. To the degree that we are able to do that, we can reason about it independently, and we can make great strides in the maintainability of our code.
