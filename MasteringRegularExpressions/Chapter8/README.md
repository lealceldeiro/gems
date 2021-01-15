# Chapter 8: Java

## Java's Regex Flavor

`java.util.regex` is powered by a Traditional NFA

Certain aspects of the flavor are modified by a variety of match modes, turned on via flags to the various methods and factories, or turned on and off via `(?`*`mods - mods`*`)` and `(?`*`mods - mods`*`:)` modifiers embedded within the regular expression itself.

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

