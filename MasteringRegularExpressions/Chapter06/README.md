# Chapter 6: Crafting an Efficient Expression

## Common Optimizations

A smart regex implementation has many ways to optimize how quickly it produces the results you ask of it. Optimizations usually fall into two classes:

- **Doing something faster**: Some types of operations, such as `\d+` , are so common that the engine might have special-case handling set up to execute them faster than the general engine mechanics would.
- **Avoiding work** If the engine can decide that some particular operation is unneeded in producing a correct result, or perhaps that some operation can be applied to less text than originally thought, skipping those operations can result in a time savings. For example, a regex beginning with `\A` (start-of-line) can match only when started at the beginning of the string, so if no match is found there, the transmission need not bother checking from other positions.

## The Mechanics of Regex Application

Her e ar e the main steps taken in applying a regular expression to a target string:

- **Regex Compilation** The regex is inspected for errors, and if valid, compiled into an internal form.
- **Transmission Begins** The transmission “positions” the engine at the start of the target string.
- **Component Tests** The engine works through the regex and the text, moving from component to component in the regex. Here there are a few additional points to mention about backtracking for NFAs:
  * With components next to each other, as with the `S`, `u`, `b`, `j`, `e`. . . , of `Subject`, each component is tried in turn, stopping only if one fails.
  * With quantifiers, control jumps between the quantifier (to see whether the quantifier should continue to make additional attempts) and the component quantified (to test whether it matches).
  * There is some overhead when control enters or exits a set of capturing par entheses. The actual text matched by the parentheses must be remember ed so that `$1` and the like are supported. Since a set of parentheses may be “backtracked out of”, the state of the parentheses is part of the states used for backtracking, so entering and exiting capturing parentheses requires some modification of that state.
- **Finding a Match** If a match is found, a Traditional NFA “locks in” the current state and reports overall success. On the other hand, a POSIX NFA merely remembers the possible match if it is the longest seen so far, and continues with any saved states still available. Once no more states are left, the longest match that was seen is the one reported.
- **Transmission Bump-Along** If no match is found, the transmission bumps the engine along to the next character in the text, and the engine applies the regex all over again.
- **Overall Failure** If no match is found after having applied the engine at every character in the target string (and after the last character as well), overall failure must be reported.
