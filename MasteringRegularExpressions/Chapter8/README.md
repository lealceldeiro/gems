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

`(?=)` and `(?!)`, `(?<=)` and `(?<!)`: Lookaround. Lookahead constructs (`(?=)` and `(?!)`) can employ arbitrary regular expressions, but lookbehind (`(?<=)` and `(?<!)`) is restricted to subexpressions whose possible matches are finite in length. This means, for example, that `?` is allowed within lookbehind, but `*` and `+` are not.

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

## Using [`java.util.regex`](https://docs.oracle.com/javase/8/docs/api/index.html?java/util/regex/package-summary.html)

A `java.util.regex.Pattern` object is, in short, a compiled regular expression that can be applied to any number of strings.

A `java.util.regex.Matcher` object is an individual instance of that regex being applied to a specific target string.

`java.util.regex.MatchResult` encapsulates the data from a successful match. Match data is available from the matcher itself until the next match attempt, but can be saved by extracting it as a `MatchResult`.

`java.util.regex.PatternSyntaxException` is thrown when an attempt is made to use an ill-formed regular expression (one such as `[oops)` that’s not syntactically correct). It extends `java.lang.IllegalAgumentException` and is unchecked.

Example:

```java
public static void main(String[] args) {
    String myText = "this is my 1st test string";
    String myRegex = "\\d+\\w+"; // This provides for `\d+\w+`

    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(myRegex);
    java.util.regex.Matcher matcher = pattern.matcher(myText);

    if (matcher.find()) {  // use `find` to launch the actual match attempt
        // query the results using `group`, `start` and `end`
        String matchedText = matcher.group();
        int matchedFrom = matcher.start();
        int matchedTo = matcher.end();

        System.out.println("Matched [" + matchedText + "] " + "from " + matchedFrom + " to " + matchedTo + ".");
        // using Java 8.0.282, prints: Matched [1st] from 11 to 14.
    } else {
        System.out.println("Didn't match");
    }
}
```

A regular-expression `Pattern` object is created with `Pattern.compile`. The first argument is a string to be interpreted as a regular expression. The compile-time options can be provided as a second argument.

Here's a snippet that creates a pattern from the string in the variable `myRegex`, to be applied in a case-insensitive manner:

```java
Pattern pattern = Pattern.compile(myRegex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
```

A call to `Pattern.compile` can throw two kinds of exceptions: an invalid regular expression throws `PatternSyntaxException`, and an invalid option value throws `IllegalArgumentException`.

`Pattern#matcher` accepts a single argument: the string to search. It doesn't actually apply the regex, but prepares the pattern to be applied to a specific string. It returns a `Matcher` object.

`Matcher#find()` applies the matcher's regex to the current region of the matcher's target text, returning a `boolean` indicating whether a match is found. If called multiple times, the next match is returned each time. This no-argument form of `find` respects the current region.

`Matcher#find(int offset)` attempts the match starting at that *offset* number of characters from the beginning of the matcher's target text. It throws `IndexOutOfBoundsException` if the offset is negative or larger than the length of the target text. This form does not respect the current region, going so far as to first reset the region to its “whole text” default (when it internally invokes the `reset` method)

`Matcher#matches()` returns a `boolean` indicating whether the matcher's regex exactly matches the current region of the target text.

`Matcher#lookingAt()` returns a `boolean` indicating whether the matcher’s regex matches within the current region of the target text, starting from the beginning of the region. This is similar to the `matches` method except that the entire region doesn’t need to be matched, just the beginning.

### Querying Match Results

The following matcher methods return information about a successful match. They throw `IllegalStateException` if the matcher’s regex hasn’t yet been applied to its target text, or if the previous application was not successful.

The methods that accept a `num` argument (referring to a set of capturing parentheses) throw `IndexOutOfBoundsException` when an invalid `num` is given.

The `start` and `end` methods, which return character offsets, do so without regard to the region — their return values are offsets from the start of the *text*, not necessarily from the start of the *region*.

`group()` Returns the text matched by the previous regex application.

`groupCount()` returns the number of sets of capturing parentheses in the regex associated with the matcher. Numbers up to this value can be used as the `num` argument to the `group`, `start`, and `end` methods.

`group(int num)` returns the text matched by the `num`<sup>th</sup> set of capturing parentheses, or `null` if that set didn't participate in the match. A num of zero indicates the entire match, so `group(0)` is the same as `group()`.

`start(int num)` returns the absolute *offset*, in characters, from the start of the string to the start of where the `num`<sup>th,</sup> set of capturing parentheses matched. Returns `-1` if the set didn’t participate in the match.

`start()` returns the absolute offset to the start of the overall match. `start()` is the same as `start(0)`

`end(int num)` returns the absolute offset, in characters, from the start of the string to the end of where the `num`<sup>th</sup> set of capturing parentheses matched. Returns `-1` if the set didn’t participate in the match.

`end()` returns the absolute offset to the end of the overall match. `end()` is the same as `end(0)`.

`toMatchResult()` Added in Java 1.5.0, it returns a `MatchResult` object encapsulating data about the most recent match. It has the same `group`, `start`, `end`, and `groupCount` methods, listed above, as the `Matcher` class. A call to `toMatchResult` throws `IllegalStateException` if the matcher hasn't attempted a match yet, or if the previous match attempt was not successful.

Example:

```java
public static void main(String[] args) {
    String url = "http://regex.info/blog";
    String regex = "(?x) ^(https?):// ([^/:]+) (?:(\\d+))?";
    Matcher matcher = Pattern.compile(regex).matcher(url);

    if (matcher.find()) {
        System.out.printf(
                "Overall [%s] (from %s to %s)%nProtocol [%s] (from %s to %s)%nHostname [%s] (from %s to %s)%n",
                matcher.group(), matcher.start(), matcher.end(),
                matcher.group(1), matcher.start(1), matcher.end(1),
                matcher.group(2), matcher.start(2), matcher.end(2)
        );
    }

    // Group #3 might not have participated, so we must be careful here
    if (matcher.group(3) == null)
        System.out.println("No port; default of '80' is assumed");
    else {
        System.out.printf("Port is [%s] (from %s to %s)%n",
                          matcher.group(3), matcher.start(3), matcher.end(3));
    }
}

// prints using Java 8.0.282:
// Overall [http://regex.info] (from 0 to 17)
// Protocol [http] (from 0 to 4)
// Hostname [regex.info] (from 7 to 17)
// No port; default of ’80’ is assumed
```

In-Place Search and Replace example (replace of the same length): replace (complete) uppercase words by the lowercase words

```java
public static void main(String[] args) {
    StringBuilder text = new StringBuilder("It’s SO very RUDE to shout!");
    Matcher m = Pattern.compile("\\b[\\p{Lu}\\p{Lt}]+\\b").matcher(text);
    
    while (m.find()) {
        text.replace(m.start(), m.end(), m.group().toLowerCase());
    }
    System.out.println(text);  // It’s so very rude to shout!
}
```


In-Place Search and Replace example (replace of different length): replace (complete) uppercase words by the lowercase words

```java
public static void main(String[] args) {
    StringBuilder text = new StringBuilder("It’s SO very RUDE to shout!");
    Matcher m = Pattern.compile("\\b[\\p{Lu}\\p{Lt}]+\\b").matcher( text );
    int matchPointer = 0;  // First search begins at the start of the string
    
    while (m.find(matchPointer)) {
        matchPointer = m .end();  // Next search starts from where this one ended
        text.replace(m.start(), m.end(), "<b>" + m.group().toLowerCase() + "</b>");
        matchPointer += 7;  // Account for having added ’<b>’ and ’</b>’
    }
    System.out.println( text );  // It’s <b>so</b> very <b>rude</b> to shout!
}
```
