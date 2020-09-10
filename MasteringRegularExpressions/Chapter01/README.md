# Chapter 1: Introduction to Regular Expressions

## Egrep Metacharacters

**`^`** (caret): *start of line*.

**`$`** (dollar): *end of line*.

**`[]`**: *character class* -- lets you list the characters you want to allow in a point in the match. It can match just a single character in the target text.

  - Example: `gr[ea]y` matches "`g`, followed by `r`, followed by either an `e` or an `a`, all followed by `y`".
  - The rules about which characters are and aren’t metacharacters (and exactly what they mean) are different inside a character class.
    - Example: within a character class, the *character-class metacharacter* **`-`** (dash) indicates a range of characters.
      - Example: `<H[1-6]>` is identical to `<H[123456]>`.
      - If `-` is the first character listed inside the character class (or after `[^`) the `-` is interpreted as a literal character.
    - Example: If `^` is the first listed character inside the character class, it "negates" the list of accepted characteres (but, only when it is immediately after the class’s opening bracket).
      - Example: `[^1-6]` matches a character that’s not 1 through 6.
    
**`.`** (dot or point):  shorthand for a character class that *matches any character*.

**`|`**: *or*, matches any one of several subexpressions.

**`()`** (parentheses): matches either expression it separates. Used to limit scope of `|` and to group multiple characters into larger units to which you can apply quantifiers like question mark and star. With tools that support *backreferencing*, parentheses "remember" the text that the subexpression inside them matches, and the special metasequence `\1` represents that text later in the regular expression, whatever it happens to be at the time. Use `\1`, `\2`, `\3`, etc., to refer to the first, second, third, etc. sets.

Backreferencing is a regular-expression feature that allows you to match new text that is the same as some text matched earlier in the expression.

Ignoring differences in capitalization is not a part of the regular-expression language, but is a related useful feature many tools (and programming languages) provide. *egrep*’s command-line option `-i` tells it to do a case-insensitive match.

Some versions of egrep offer limited support for word recognition: namely the ability to match the boundary of a word (where a word begins or ends). If some version happens to support them, the *metasequences* `\<` and `\>` could be used. You can think of them as word-based versions of `^` and `$` that match the position at the start and end of a word, respectively.

---

**`?`** (question mark): *optional*. It is placed after the character that is allowed to appear at that point in the expression, but whose existence isn’t actually required to still be considered a successful match. It can attach to a parenthesized expression.
  - Example: `4(th)?` will match `4th` and `4`.

**`+`** (plus): *one or more of the immediately-preceding item*.

**`*`** (star): *any number, including none, of the immediately-preceding item*.

`?`, `+` and `*` are called *quantifiers* because they influence the quantity of what they govern.

---

**`{`** <sub><sup>min, max</sup></sub> **`}`**: *interval* quantifier.
  - Example: `{3,12}` matches up to 12 times if possible, but settles for three of the immediately-preceding item.

Not many versions of egrep support this notation yet, but many other tools do.

---

**`\`** (backslash): *escape*. Used to "escape" all the normal metacharacters. When a metacharacter is escaped, it loses its special meaning and becomes a literal character.
  - Example: The metasequence to match an actual period is a period preceded by a backslash: `ega\.att\.com` will match `ega.att.com`.

---

Knowing the target text well is an important part of wielding regular expressions effectively.

## Expanding the Foundation
