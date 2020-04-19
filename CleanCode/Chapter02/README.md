# Chapter 2: Meaningful Names

## Use Intention-Revealing Names

The name of a variable, function, or class, should answer all the big questions. It should tell you why it exists, what it does, and how it is used. If a name requires a comment, then the name does not reveal its intent.

## Avoid Disinformation

Programmers must avoid leaving false clues that obscure the meaning of code.

Spelling similar concepts similarly is _information_. Using inconsistent spellings is _disinformation_.

## Make Meaningful Distinctions

If names must be different, then they should also mean something different.

Distinguish names in such a way that the reader knows what the differences offer.

## Use Pronounceable Names

If you can’t pronounce it, you can’t discuss it. This matters because programming is a social activity.

## Use Searchable Names

Single-letter names and numeric constants have a particular problem in that they are not easy to locate across a body of text. In this regard, longer names trump shorter names, and any searchable name trumps a constant in code.

## Avoid Encodings

Encoded names are seldom pronounceable and are easy to mis-type.

### Hungarian Notation

Nowadays Hungarian Notation and other forms of type encoding are simply impediments. They make it harder to change the name or type of a variable, function, or class. They make it harder to read the code. And they create the possibility that the encoding system will mislead the reader.

### Member Prefixes
