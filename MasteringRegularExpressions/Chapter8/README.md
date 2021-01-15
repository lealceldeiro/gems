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

`\0octal`

`\x##`

`\u####`

`\cchar`

`[]` and `[^]: Classes (may contain class set operators)

`.` (*dot*): Almost any character (various meanings, changes with modes)

`\w`: 

`\d`: 

`\s`: 

`\W`: 

`\D`: 

`\S`:
