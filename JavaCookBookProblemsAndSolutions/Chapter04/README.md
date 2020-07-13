# Chapter 4: Pattern Matching with Regular Expressions

## 4.2 Using Regexes in Java: Test for a Pattern

### Problem

Test if a given pattern can match in a given string.

### Solution

Use the Java Regular Expressions Package, `java.util.regex`.

These are the normal steps for regex matching in a production program:

* Create a `Pattern` by calling the static method `Pattern.compile()`.
* Request a `Matcher` from the pattern by calling `pattern.matcher(CharSequence)` for each `String` (or other `CharSequence`) you wish to look through.
* Call (once or more) one of the finder methods in the resulting `Matcher`.
