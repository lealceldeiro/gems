# Chapter 4: Comments

The proper use of comments is to compensate for our failure to express ourself in code.

The older a comment is, and the farther away it is from the code it describes, the more likely it is to be just plain wrong. The reason is simple. Programmers can’t realistically maintain them.

Truth can only be found in one place: the code. Only the code can truly tell you what it does. It is the only source of truly accurate information.

## Comments Do Not Make Up for Bad Code

Clear and expressive code with few comments is far superior to cluttered and complex code with lots of comments. Rather than spend your time writing the comments that explain the mess you’ve made, spend it cleaning that mess.

## Explain Yourself in Code

There are certainly times when code makes a poor vehicle for explanation. Unfortunately, many programmers have taken this to mean that code is seldom, if ever, a good means for explanation. This is patently false. Example:

The following comment can be completed replaced

```
// Check to see if the employee is eligible for full benefits
if ((employee.flags & HOURLY_FLAG) &&
    (employee.age > 65))
```
by a well named function like this:

```
if (employee.isEligibleForFullBenefits())
```

## Good Comments

Some comments are necessary or beneficial. BUT, the only truly good comment is the comment you found a way not to write.

### Legal Comments

Copyright and authorship statements are necessary and reasonable things to put into a comment at the start of each source file.

### Informative Comments

It is sometimes useful to provide basic information with a comment. For example

```
// format matched kk:mm:ss EEE, MMM dd, yyyy
Pattern timeMatcher = Pattern.compile("\\d*:\\d*:\\d* \\w*, \\w* \\d*, \\d*");
```

### Explanation of Intent

Sometimes a comment goes beyond just useful information about the implementation and provides the intent behind a decision. i.e.:

```
public int compareTo(Object o) {
  if(o instanceof WikiPagePath) {
    // ...
  }

  return 1; // we are greater because we are the right type.
}
```

### Clarification

Sometimes it is just helpful to translate the meaning of some obscure argument or return value into something that’s readable. In general it is better to find a way to make that argument or return value clear in its own right; but when its part of the standard library, or in code that you cannot alter, then a helpful clarifying comment can be useful.

### Warning of Consequences

Sometimes it is useful to warn other programmers about certain consequences. For example, here is a comment that explains why a particular test case is turned off:

```
// Don't run unless you have some time to kill.
public void _testWithReallyBigFile() {
  writeLinesToFile(10000000);
  // ...
}
```
