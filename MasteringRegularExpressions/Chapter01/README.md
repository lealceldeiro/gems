# Chapter 1: Introduction to Regular Expressions

## Egrep Metacharacters

**`^`** (caret): *start of line*

**`$`** (dollar): *end of line*

**`[]`**: *character class* -- lets you list the characters you want to allow in a point in the match. It can match just a single character in the target text.

  - Example: `gr[ea]y` matches "`g`, followed by `r`, followed by either an `e` or an `a`, all followed by `y`"
  - The rules about which characters are and aren’t metacharacters (and exactly what they mean) are different inside a character class
    - Example: within a character class, the *character-class metacharacter* **`-`** (dash) indicates a range of characters
      - Example: `<H[1-6]>` is identical to `<H[123456]>`
      - If `-` is the first character listed inside the character class (or after `[^`) the `-` is interpreted as a literal character
    - Example: If `^` is the first listed character inside the character class, it "negates" the list of accepted characteres (but, only when it is immediately after the class’s opening bracket)
      - Example: `[^1-6]` matches a character that’s not 1 through 6
    
**`.`** (dot or point):  shorthand for a character class that *matches any character*

**`|`**: *or*, matches any one of several subexpressions

**`()`** (parentheses): matches either expression it separates. Used to limit scope of `|` (plus some more additional uses)

Ignoring differences in capitalization is not a part of the regular-expression language, but is a related useful feature many tools (and programming languages) provide. *egrep*’s command-line option `-i` tells it to do a case-insensitive match.

Some versions of egrep offer limited support for word recognition: namely the ability to match the boundary of a word (where a word begins or ends). If some version happens to support them, the *metasequences* `\<` and `\>` could be used. You can think of them as word-based versions of `^` and `$` that match the position at the start and end of a word, respectively.

Knowing the target text well is an important part of wielding regular expressions effectively.
