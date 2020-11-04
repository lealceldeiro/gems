# Chapter 4: The Mechanics of Expression Processing


### Testing the Engine Type

#### Traditional NFA or not?

If lazy quantifiers are supported, it’s almost certainly a Traditional NFA. i.e.: in Java it can be checked with the following code

```java
Matcher matcher = Pattern.compile("nfa|nfa not").matcher("nfa not");
if (matcher.find()) {
    System.out.println(matcher.group());
}
```

if only `nfa` matches, it’s a Traditional NFA . If the entire `nfa not` matches, it’s either a POSIX NFA or a DFA.

#### DFA or POSIX NFA?

Capturing parentheses and backreferences are not supported by a DFA, so that can be one hint, but there are systems that are a hybrid mix between the two engine types, and so may end up using a DFA if there are no capturing parentheses.

i.e.: apply `X(.+)+X` to a string like `=XX======================`, as with this `egrep` command:

```shell
echo =XX========================================= | egrep 'X(.+)+X'
```

or in Java

```java
Matcher matcher = Pattern.compile("X(.+)+X").matcher("=XX=========================================");
if (matcher.find()) {
    System.out.println(matcher.group());
}
```

If it takes a long time to finish, it’s an NFA (and if not a Traditional NFA as per the test in the previous section, it must be a POSIX NFA).

If it finishes quickly, it’s either a DFA or an NFA with some advanced optimization.

If it display a warning message about a stack overflow or long match aborted, it’s an NFA.

## Match Basics

There are only two all-encompassing rules:

- The match that begins earliest (leftmost) wins.

- The standard quantifiers (`*`, `+`, `?`, and `{m,n}`) are greedy.

### Engine Pieces and Parts

- Literal text

- Character classes, dot, Unicode properties, and the like

- Capturing parentheses

- Anchors
