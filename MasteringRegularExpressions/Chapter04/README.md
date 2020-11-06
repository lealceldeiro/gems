# Chapter 4: The Mechanics of Expression Processing


### Testing the Engine Type

#### Traditional NFA (Nondeterministic Finite Automaton) or not?

If lazy quantifiers are supported, it’s almost certainly a Traditional NFA. i.e.: in Java it can be checked with the following code

```java
Matcher matcher = Pattern.compile("nfa|nfa not").matcher("nfa not");
if (matcher.find()) {
    System.out.println(matcher.group());
}
```

if only `nfa` matches, it’s a Traditional NFA . If the entire `nfa not` matches, it’s either a POSIX NFA or a DFA.

#### DFA (Deterministic Finite Automaton) or POSIX NFA?

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

### DFA versus NFA : Differences in the pre-use compile

Before applying a regex to a search, both types of engines compile the regex to an internal form suited to their respective match algorithms. An NFA compile is generally faster, and requires less memory. There’s no real difference between a Traditional and POSIX NFA compile.

### DFA versus NFA : Differences in match speed

For simple literal-match tests in “normal” situations, both types match at about the same rate. A DFA’s match speed is generally unrelated to the particular regex, but an NFA’s is directly related.

A Traditional NFA must try every possible permutation of the regex before it can conclude that there’s no match. If it’s a Traditional NFA, it can at least stop if and when it finds a match.

A POSIX NFA, on the other hand, must always try every possible permutation of the regex to ensure that it has found the longest possible match, so it generally takes the same (possibly very long) amount of time to complete a successful match as it does to confirm a failure. Writing efficient expressions is doubly important for a POSIX NFA.

The need for optimizations is less pressing with a DFA since its matching is so fast to begin with, but for the most part, the extra work done during the DFA’s pre-use compile affords better optimizations than most NFA engines take the trouble to do.

Modern DFA engines often try to reduce the time and memory used during the compile by postponing some work until a match is attempted. It does, however, create cases where there can be a relationship among the regex, the text being checked, and the match speed.

### DFA versus NFA: Differences in what is matched

A DFA (or anything POSIX) finds the longest leftmost match. A Traditional NFA might also, or it might find something else. Any individual engine always treats the same regex/text combination in the same way, so in that sense, it’s not “random”, but other NFA engines may decide to do slightly different things.

### DFA versus NFA: Differences in capabilities

An NFA engine can support many things that a DFA cannot. Among them are:

- Capturing text matched by a parenthesized subexpression. Related features are backr efer ences and after-match information saying wher e in the matched text each parenthesized subexpression matched.

- Lookaround, and other complex zero-width assertions.

- Non-greedy quantifiers and ordered alternation.

- Possessive quantifiers and atomic grouping.

----

To get the most out of a utility, you need to understand which type of engine it uses, and craft your regular expressions appropriately. The most common type is the Traditional NFA, followed by the DFA.

One overriding rule regardless of engine type: matches starting sooner take precedence over matches starting later.

For the match attempt starting at any given spot:

- DFA Text-Directed Engines
  - Find the longest possible match
- NFA Regex-Directed Engines
  - Must “work through” a match. The soul of NFA matching is *backtracking*.
  - The metacharacters control the match: the standard quantifiers (star and friends) are greedy, while others may be lazy or possessive.
  - Alternation is ordered in a traditional NFA, but greedy with a POSIX NFA.
  - POSIX NFA Must find the longest match but, you must worry about efficiency.
  - Traditional NFA Is the most expressive type of regex engine, since you can use the regex-directed nature of the engine to craft exactly the match you want.
  
