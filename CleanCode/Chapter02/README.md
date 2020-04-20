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

You also don’t need to prefix member variables with `m_` anymore. Your classes and functions should be small enough that you don’t need them. And you should be using an editing environment that highlights or colorizes members to make them distinct.

## Avoid Mental Mapping

Readers shouldn’t have to mentally translate your names into other names they already know. This problem generally arises from a choice to use neither problem domain terms nor solution domain terms.

> _One difference between a smart programmer and a professional programmer is that the professional understands that clarity is king. Professionals use their powers for good and write code that others can understand._

## Class Names

Classes and objects should have noun or noun phrase names like `Customer`, `WikiPage`, `Account`, and `AddressParser`. Avoid words like `Manager`, `Processor`, `Data`, or `Info` in the name of a class. A class name should not be a verb.

## Method Names

Methods should have verb or verb phrase names like `postPayment`, `deletePage`, or `save`. Accessors, mutators, and predicates should be named for their value and prefixed with `get`, `set`, and `is` according to the javabean standard.

When constructors are overloaded, use static factory methods with names that describe the arguments. For example,

```
Complex fulcrumPoint = Complex.FromRealNumber(23.0);
```

is generally better than

```
Complex fulcrumPoint = new Complex(23.0);
```

Consider enforcing their use by making the corresponding constructors private.

## Don’t Be Cute

Choose clarity over entertainment value. i.e.: the purpose of a function named `DeleteItems` is far more understandable than one named `HolyHandGrenade`.

Say what you mean. Mean what you say.

## Pick One Word per Concept

Pick one word for one abstract concept and stick with it. For instance, it’s confusing to have fetch , retrieve, and get as equivalent methods of different classes.

A consistent lexicon is a great boon to the programmers who must use your code.

## Don’t Pun

Avoid using the same word for two purposes. Using the same term for two different ideas is essentially a pun.

## Use Solution Domain Names

Use computer science (CS) terms, algorithm names, pattern names, math terms, and so forth. It is not wise to draw every name from the problem domain because we don’t want our coworkers to have to run back and forth to the customer asking what every name means when they already know the concept by a different name.

## Use Problem Domain Names

When there is no “programmer-eese” for what you’re doing, use the name from the problem domain. At least the programmer who maintains your code can ask a domain expert what it means.

## Add Meaningful Context

There are a few names which are meaningful in and of themselves—most are not. Instead, you need to place names in context for your reader by enclosing them in well-named classes, functions, or namespaces. When all else fails, then prefixing the name may be necessary as a last resort.

## Don’t Add Gratuitous Context

Shorter names are generally better than longer ones, so long as they are clear. Add no more context to a name than is necessary.

## Final Words

The hardest thing about choosing good names is that it requires good descriptive skills and a shared cultural background.
