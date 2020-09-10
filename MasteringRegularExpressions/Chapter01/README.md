# Chapter 1: Introduction to Regular Expressions

## Egrep Metacharacters

**`^`** (caret): *start of line*

**`$`** (dollar): *end of line*

**`[]`**: *character class* -- lets you list the characters you want to allow in a point in the match

  - Example: `gr[ea]y` matches "`g`, followed by `r`, followed by either an `e` or an `a`, all followed by `y`"
  - Within a character class, the *character-class metacharacter* **`-`** (dash) indicates a range of characters
    - Example: `<H[1-6]>` is identical to `<H[123456]>`
    - If `-` is the first character listed inside the character class (or after `[^`) the `-` is interpreted as a literal character
  - If `^` is the first listed character inside the character class, it "negates" the list of accepted characteres (but, only when it is immediately after the class’s opening bracket)
    - Example: `[^1-6]` matches a character that’s not 1 through 6
    
**`.`** (dot or point):  shorthand for a character class that *matches any character*

**`|`**: *or*, matches any one of several subexpressions

Knowing the target text well is an important part of wielding regular expressions effectively.
