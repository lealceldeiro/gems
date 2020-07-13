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

## 4.4 Replacing the Matched Text

### Problem

Having found some text using a `Pattern`, you want to replace the text with different text, without disturbing the rest of the string.

### Solution

The `Matcher` class provides several methods for replacing just the text that matched the pattern. In all these methods, you pass in the replacement text:

* `replaceAll(newString)`: Replaces all occurrences that matched with the new string
* `replaceFirst(newString)`: As above but only the first occurence
* `appendReplacement(StringBuffer, newString)`: Copies up to before the first match, plus the given `newString`
* `appendTail(StringBuffer)`: Appends text after the last match (normally used after `appendReplacement`)

## 4.7 Controlling Case in Regular Expressions

### Problem

You want to find text regardless of case.

### Solution

Compile the `Pattern` passing in the flags argument `Pattern.CASE_INSENSITIVE` to indicate that matching should be case-independent. If your code might run in different locales, then you should add `Pattern.UNICODE_CASE`. This flag (and others) are passed to the [`Pattern.compile(String regex, int flags)`](https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html#compile(java.lang.String,%20int)) method, like this:

```java
Pattern reCaseInsenstiveUnicode = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
reCaseInsenstiveUnicode.matches(input);        // will match case-insensitively
```
This flag must be passed when you create the `Pattern` because `Pattern` objects are immutable.

If more than one flag is needed, they can be or’d together using the bitwise *or* operator `|`.

## 4.8 Matching Accented, or Composite, Characters

### Problem

You want characters to match regardless of the form in which they are entered.

### Solution

Compile the `Pattern` with the flags argument **`Pattern.CANON_EQ`** for canonical equality.

## 4.9 Matching Newlines in Text

### Problem

You need to match newlines in text.

### Solution

Use `\n` or `\r` in your regex pattern. See also the flags constant `Pattern.MULTILINE`, which makes newlines match as beginning-of-line and end-of-line (`\^` and `$`).
