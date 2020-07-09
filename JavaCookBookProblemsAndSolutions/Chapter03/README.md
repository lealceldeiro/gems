# Chapter 3: Strings and Things

Java 11 and 12 added several new `String` methods, including `indent(int n)`, `stripLeading()` and `stripTrailing()`, `Stream<T> lines()`, `isBlank()`, and `transform()`.

## 3.1 Taking Strings Apart with Substrings or Tokenizing

### Problem
You want to break a string apart, either by indexing positions or by using fixed token characters (e.g., break on spaces to get words).

### Solution

For substrings, use the String object’s `substring()` method. For tokenizing, construct a `StringTokenizer` around your string and call its methods `hasMoreTokens()` and `nextToken()`.

Or, use regular expressions.

## 3.2 Putting Strings Together with `StringBuilder`

### Problem

You need to put some String pieces (back) together.

### Solution

Use string concatenation: the `+` operator. The compiler *implicitly* constructs a `StringBuilder` for you and uses its `append()` methods (unless all the string parts are known at compile time).

Better yet, construct and use a `StringBuilder` yourself.
