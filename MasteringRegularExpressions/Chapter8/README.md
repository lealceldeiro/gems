# Chapter 8: Java

## Java's Regex Flavor

`java.util.regex` is powered by a Traditional NFA

Certain aspects of the flavor are modified by a variety of match modes, turned on via flags to the various methods and factories, or turned on and off via `(?`*`mods - mods`*`)` and `(?`*`mods - mods`*`:)` modifiers embedded within the regular expression itself.

### Character Shorthands

`\x##` allows exactly two hexadecimal digits, e.g., `\x`**`FC`**`ber` matches `über`

`\a`: **Alert** (e.g., to sound the bell when “printed”) Usually maps to the ASCII <BEL> character, 007 octal

`\b` **Word boundary metacharacter**

`[\b]`: **Backspace** Usually maps to the ASCII <BS> character, 010 octal.

`\e`: **Escape character** Usually maps to the ASCII <ESC> character, 033 octal

`\f`: **Form feed** Usually maps to the ASCII <FF> character, 014 octal

`\n`: **Newline** With Java or any .NET language, always maps to the ASCII <LF> character, 012 octal, regardless of platform.

`\r`: **Carriage return** With Java or any .NET language, always maps to the ASCII <CR> character regardless of platform.

`\t`: **Normal horizontal tab** Maps to the ASCII <HT> character, 011 octal.

`\0octal`: **Octal escape**, example: `\07`, `\077`, `\0377`. It requires a leading zero, followed by one to three octal digits.

`\x##`: **Hex escapes**, example: `\xFF`

`\u####`: **Hex escapes**, example: `\uFFFF`. It allows exactly four hexadecimal digits.

`\cchar`: It's case sensitive, blindly *xor*ing the ordinal value of the following character with `0x40`. This behavior means that, i.e., `\cA` and `\ca` are different.

### Character Classes and Class-Like Constructs

`[]` and `[^]`: Classes (may contain class set operators)

`.` (*dot*): Almost any character (various meanings, changes with modes)

`\w`: **Part-of-word character**, exactly the same as `[a-zA-Z0-9_]`

`\d`: **Digit**, exactly the same as `[0-9]`

`\s`: **Whitespace character**, exactly the same as `[ \f\n\r\t\x0B]` (`\x0B` is the little-used ASCII VT character)

`\W`: **Non-word character**, exactly the same as `[^\w]`

`\D`: **Non-digit**, exactly the same as `[^\d]`

`\S`: **Non-whitespace character**, exactly the same as `[^\s]`.

`\p{`*`Prop`*`}` and `\P{`*`Prop`*`}`: It support Unicode properties and blocks, and some additional “Java properties.” Unicode scripts are not supported.

Some of the properties are:

| Class | Synonym and description                                                                                                                       |
|-------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| \p{L} | \p{Letter} – Things considered letters                                                                                                        |
| \p{M} | \p{Mark} – Various characters that are not meant to appear by themselves, but with other base characters (accent marks, enclosing boxes, ...) |
| \p{Z} | \p{Separator} – Characters that separate things, but have no visual representation (various kinds of spaces...)                               |
| \p{S} | \p{Symbol} – Various types of Dingbats and symbols                                                                                            |
| \p{N} | \p{Number} – Any kind of numeric character                                                                                                    |
| \p{P} | \p{Punctuation} – Punctuation characters                                                                                                      |
| \p{C} | \p{Other} – Catch-all for everything else (rarely used for normal characters)                                                                 |

### Anchors and Other Zero-Width Tests

`^` and `\A`: Start of line/string

`$`, `\z` and `\Z`: End of line/string

`\G` : Start of current match

`\b` and `\B`: Word boundary. The idea of these metacharacters of a “word character” is not the same as that of `\w` and `\W`. The word boundaries understand the properties of Unicode characters, while `\w` and `\W` match only ASCII characters.

`(?=)`, `(?!)`, `(?<=)` and `(?<!)`: Lookaround. Lookahead constructs can employ arbitrary regular expressions, but lookbehind is restricted to subexpressions whose possible matches are finite in length. This means, for example, that `?` is allowed within lookbehind, but `*` and `+` are not.

### Comments and Mode Modifiers

`(?`*`mods`*`-`*`mods`*`)`: Mode modifiers. Modifiers allowed:
  - `x`: free-spacing and comments regex mode. Applies even inside character classes. (`x`: on, `-x`: off)
  - `d`: Changes how *dot* and `^` match (`d`: on, `-d`: off)
  - `s`: dot-matches-all match mode (`s`: on, `-s`: off)
  - `m`: enhanced line-anchor match mode. Expands where `^` and `$` can match (`m`: on, `-m`: off)
  - `i`: case-insensitivity match mode for ASCII characters (`i` on, `-i`: off)
  - `u`: case-insensitive matching for non-ASCII characters (`u`: on, `-u`: off)

`(?`*`mods`*`-`*`mods`*`:)`: Mode-modified spans

From `#` until newline (only when enabled): Comments (may also be used within a character class)

`\Q`...`\E`: Literal-text mode (may also be used within a character class)

### Grouping and Capturing

`()` `\1` `\2`: Capturing parentheses

`(?:)`: Grouping-only parentheses

`(?>)`: Atomic grouping

`|`: Alternation

`*` `+` `?` `{n}` `{n,}` `{x,y}`: Greedy quantifiers

`*?` `+?` `??` `{n}?` `{n,}?` `{x,y}?`: Lazy quantifiers

`*+` `++` `?+` `{n}+` `{n,}+` `{x,y}+`: Possessive quantifiers


## Unicode Line Terminators

Java normally considers the following as line terminators:

| Character Codes | Nicknames     | Description                                |
|-----------------|---------------|--------------------------------------------|
| U+000A          | LF     `\n`   | ASCII Line Feed (“newline”)                |
| U+000D          | CR     `\r`   | ASCII Carriage Return                      |
| U+000D U+000A   | CR/LF  `\r\n` | ASCII Carriage Return / Line Feed sequence |
| U+0085          | NEL           | Unicode NEXT LINE                          |
| U+2028          | LS            | Unicode LINE SEPARATOR                     |
| U+2029          | PS            | Unicode PARAGRAPH SEPARATOR                |

The characters and situations that are treated specially by *dot*, `^`, `$`, and `\Z` change depending on which match modes:

| Match Mode | Affects    | Description                                                                                        |
|------------|------------|----------------------------------------------------------------------------------------------------|
| UNIX_LINES | `^ . $ \Z` | Revert to traditional newline-only line-terminator semantics.                                      |
| MULTILINE  | `^ $`      | Add embedded line terminators to list of locations after which `^` and before which `$` can match. |
| DOTALL     | `.`        | Line terminators no longer special to dot ; it matches any character.                              |

