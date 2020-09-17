# Chapter 2: Extended Introductory Examples

## Matching Text with Regular Expressions

**`(?:)`**: *Non-Capturing Parentheses*. Group but do not capture (Supported by Perl and many other languages).

In Perl regular expressions, `\b` normally matches a word boundary, but within a character class, it matches a backspace.

Perl can check a string in a variable against a regex using the construct **`$variable = ̃ m/`** `regex` **`/`** . The `m` indicates that a match is requested, while the slashes delimit (and are not part of) the regular expression. The whole test, as a unit, is either `true` or `false`.

Among the more useful shorthands that Perl and many other flavors of regex provide are:
`\t`: a tab character

`\n`: a newline character

`\r`: a carriage-return character

`\s`: matches any “whitespace” character (space, tab, newline, formfeed, and such)

`\S`: anything not `\s`

`\w`: `[a-zA-Z0-9_]` (useful as in `\w+`, ostensibly to match a word)

`\W`: anything not `\w`, i.e., `[ˆa-zA-Z0-9_]`

`\d`: `[0-9]` , i.e., a digit

`\D`: anything not `\d`, i.e., `[ˆ0-9]`
