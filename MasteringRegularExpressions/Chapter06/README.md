# Chapter 6: Crafting an Efficient Expression

## Common Optimizations

A smart regex implementation has many ways to optimize how quickly it produces the results you ask of it. Optimizations usually fall into two classes:

- **Doing something faster**: Some types of operations, such as `\d+` , are so common that the engine might have special-case handling set up to execute them faster than the general engine mechanics would.
- **Avoiding work** If the engine can decide that some particular operation is unneeded in producing a correct result, or perhaps that some operation can be applied to less text than originally thought, skipping those operations can result in a time savings. For example, a regex beginning with `\A` (start-of-line) can match only when started at the beginning of the string, so if no match is found there, the transmission need not bother checking from other positions.

### The Mechanics of Regex Application

Here are the main steps taken in applying a regular expression to a target string:

- **Regex Compilation** The regex is inspected for errors, and if valid, compiled into an internal form.
- **Transmission Begins** The transmission “positions” the engine at the start of the target string.
- **Component Tests** The engine works through the regex and the text, moving from component to component in the regex. Here there are a few additional points to mention about backtracking for NFAs:
  * With components next to each other, as with the `S`, `u`, `b`, `j`, `e`. . . , of `Subject`, each component is tried in turn, stopping only if one fails.
  * With quantifiers, control jumps between the quantifier (to see whether the quantifier should continue to make additional attempts) and the component quantified (to test whether it matches).
  * There is some overhead when control enters or exits a set of capturing par entheses. The actual text matched by the parentheses must be remember ed so that `$1` and the like are supported. Since a set of parentheses may be “backtracked out of”, the state of the parentheses is part of the states used for backtracking, so entering and exiting capturing parentheses requires some modification of that state.
- **Finding a Match** If a match is found, a Traditional NFA “locks in” the current state and reports overall success. On the other hand, a POSIX NFA merely remembers the possible match if it is the longest seen so far, and continues with any saved states still available. Once no more states are left, the longest match that was seen is the one reported.
- **Transmission Bump-Along** If no match is found, the transmission bumps the engine along to the next character in the text, and the engine applies the regex all over again.
- **Overall Failure** If no match is found after having applied the engine at every character in the target string (and after the last character as well), overall failure must be reported.

### Pre-Application Optimizations

- Compile caching
- Pre-check of required character/substring optimization
- Length-cognizance optimization

### Optimizations with the Transmission

- Start of string/line anchor optimization
  * This optimization recognizes that any regex that begins with `^` can match only when applied where `^` can match, and so need be applied at those locations only. Similar optimizations involve `\A`, and for repeated matches, `\G`.
- Implicit-anchor optimization
  * If a regex begins with `.+` or `.+`, and has no global alternation, an implicit `^` can be prepended to the regex. This allows the start of string/line anchor optimization to be used, which can provide a lot of savings.
- End of string/line anchor optimization
  * This optimization recognizes that some regexes ending with `$` or other end anchors have matches that start within a certain number of characters from the end of the string.
- Initial character/class/substring discrimination optimization
  * A more generalized version of the pre-check of required character/string optimization, this optimization uses the same information (that any match by the regex must begin with a specific character or literal substring) to let the transmission use a fast substring check so that it need apply the regex only at appropriate spots in the string.
- Embedded literal string check optimization
  * This is almost exactly like the initial string discrimination optimization, but is mor e advanced in that it works for literal strings embedded a known distance into any match.
- Length-cognizance transmission optimization

### Optimizations of the Regex Itself
- Literal string concatenation optimization
- Simple quantifier optimization
- Needless parentheses elimination
- Needless character class elimination
- Character following lazy quantifier optimization
- "Excessive" backtracking detection
- Exponential (a.k.a., super-linear) short-circuiting
- State-suppression with possessive quantifiers
- Small quantifier equivalence
- Need cognizance

## Techniques for Faster Expressions

### Common Sense Techniques

- Avoid recompiling
- Use non-capturing parentheses if possible in the situation
- Don’t add superfluous parentheses
- Don’t use superfluous character classes
- Use leading anchors

### Expose Literal Text

- “Factor out” required components from quantifiers. Example: Using `xx+` instead of `x+` exposes `x` as being required. The same logic applies to the rewriting of `-{5,7}` as `------{0,2}`
- “Factor out” required components from the front of alternation. Example: Using `th(?:is|at)` rather than `(?:this|that)` exposes that `th` is required.

### Expose Anchors

- Expose `^` and `\G` at the front of expressions. Example: `^(?:abc|123)` and  `^abc|^123` are logically the same expression, but many more
regex engines can apply the *Start of string/line anchor optimization* with the first than the second.
- Expose `$` at the end of expressions (conceptually the same as the previous one)

### Lazy Versus Greedy: Be Specific

### Split Into Multiple Regular Expressions

### Mimic Initial-Character Discrimination

Example: for `Jan|Feb|...|Dec`, use **`(?=[JFMASOND])(?:`**`Jan|Feb|...|Dec`**`)`** (The leading `[JFMASOND]` represents letters that can begin the month names in
English).

- Don’t do this with *Tcl*
- Don’t do this with *PHP*

### Use Atomic Grouping and Possessive Quantifiers

Great care must be taken when applying this optimization.

### Lead the Engine to a Match

- Put the most likely alter native first
- Distribute into the end of alternation
