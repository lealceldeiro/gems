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

Some of the Matcher methods are:

* `match()`: Used to compare the entire string against the pattern; this is the same as the routine in `java.lang.String`.
* `lookingAt()`: Used to match the pattern only at the beginning of the string.
* `find()`: Used to match the pattern in the string (not necessarily at the first character of the string), starting at the beginning of the string or, if the method was previously called and succeeded, at the first character not matched by the previous match.

## 4.3 Finding the Matching Text

### Problem

You need to find the text that the regex matched.

### Solution

After a successful call to one of the preceding methods, you can use these information methods on the Matcher to get information on the match:

* `start()`, `end()`: Returns the character position in the string of the starting and ending characters that matched.
* `groupCount()`: Returns the number of parenthesized capture groups, if any; returns 0 if no groups were used.
* `group(int i)`: Returns the characters matched by group `i` of the current match, if `i` is greater than or equal to zero and less than or equal to the return value of `groupCount()`. Group `0` is the entire match, so `group(0)` (or just `group()`) returns the entire portion of the input that matched.
