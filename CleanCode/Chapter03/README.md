# Chapter 3: Functions

## Small!

The first rule of functions is that they should be small. The second rule of functions is that _they should be smaller than that_.

Functions should hardly ever be 20 lines long.

## Blocks and Indenting

Blocks within if statements, else statements, while statements, and so on should be one line long. Probably that line should be a function call. Not only does this keep the enclosing function small, but it also adds documentary value because the function called within the block can have a nicely descriptive name.

## Do One Thing

Functions should do one thing. They should do it well. They should do it only.

If a function does only those steps that are one level below the stated name of the function, then the function is doing one thing.

Another way to know that a function is doing more than “one thing” is if you can extract another function from it with a name that is not merely a restatement of its implementation.

### Sections within Functions

This is an obvious symptom of doing more than one thing. Functions that do one thing cannot be reasonably divided into sections.

## One Level of Abstraction per Function

In order to make sure our functions are doing “one thing,” we need to make sure that the statements within our function are all at the same level of abstraction.

### Reading Code from Top to Bottom: The Stepdown Rule

We want to be able to read the program as though it were a set of TO paragraphs, each of which is describing the current level of abstraction and referencing subsequent TO paragraphs at the next level down.

Example, for this function:

```
public static String renderPageWithSetupsAndTeardowns(PageData pageData, boolean isSuite) {
  if (isTestPage(pageData)) {
    includeSetupAndTeardownPages(pageData, isSuite);
  }

  return pageData.getHtml();
}
```

the The Stepdown Rule paragraphs would be something like

>_To include the setups and teardowns, we include setups, then we include the test page content, and then we include the teardowns._
>
>--> _To include the setups, we include the suite setup if this is a suite, then we include the regular setup._
>
>----> _To include the suite setup, we search the parent hierarchy for the “SuiteSetUp” page and add an include statement with the path of that page._
>
>------> _To search the parent..._

## Switch Statements

`switch` statements can be tolerated if they appear only once, are used to create polymorphic objects, and are hidden behind an inheritance relationship so that the rest of the system can’t see them.

## Use Descriptive Names

Don’t be afraid to make a name long. A long descriptive name is better than a short enigmatic name. A long descriptive name is better than a long descriptive comment.

Choosing descriptive names will clarify the design of the module in your mind and help you to improve it.

Be consistent in your names. Use the same phrases, nouns, and verbs in the function names you choose for your modules.

## Function Arguments

The ideal number of arguments for a function is zero (niladic). Next comes one (monadic), followed closely by two (dyadic). Three arguments (triadic) should be avoided where possible. More than three (polyadic) requires very special justification—and then shouldn’t be used anyway.

The argument is at a different level of abstraction than the function name and forces you to know a detail that may not be particularly important at that point.

Arguments are even harder from a testing point of view. Imagine the difficulty of writing all the test cases to ensure that all the various combinations of arguments work properly.

Output arguments are harder to understand than input arguments.

Using an output argument instead of a return value for a transformation is confusing. If a function is going to transform its input argument, the transformation should appear as the return value.

## Flag Arguments

Flag arguments are ugly. Passing a boolean into a function is a truly terrible practice. It immediately complicates the signature of the method, loudly proclaiming that this function does more than one thing. It does one thing if the flag is true and another if the flag is false!

### Dyadic Functions

Dyads aren’t evil, and you will certainly have to write them. However, you should be aware that they come at a cost and should take advantage of what mechanims may be available to you to convert them into monads.

### Argument Objects

When a function seems to need more than two or three arguments, it is likely that some of those arguments ought to be wrapped into a class of their own. i.e., `double x, double y` conceptually form a `Point`:

```
Circle makeCircle(double x, double y, double radius);
Circle makeCircle(Point center, double radius);
```

### Verbs and Keywords

In the case of a monad function, the function name and argument should form a very nice verb/noun pair. i.e.: `write(name)` or `writeField(name)`.

## Have No Side Effects

Side effects are lies. Your function promises to do one thing, but it also does other hidden things.

Temporal couplings are confusing, especially when hidden as a side effect. If you must have a temporal coupling, you should make it clear in the name of the function.

### Output Arguments

Arguments are most naturally interpreted as inputs to a function.

Many times output arguments force you to check the function signature, and that is equivalent to a double-take. It’s a cognitive break and should be avoided.

In general output arguments should be avoided. If your function must change the state of something, have it change the state of its owning object.

## Command Query Separation

Functions should either do something or answer something, but not both. Either your function should change the state of an object, or it should return some information about that object. Doing both often leads to confusion.

For example, the following function `public boolean set(String attribute, String value);` sets the value of a named attribute and returns `true` if it is successful and `false` if no such attribute exists.

This leads to odd statements like `if (set("username", "unclebob"))`. From the point of view of the reader it’s hard to infer the meaning from the call because it’s not clear whether the word "`set`" is a verb or an adjective.

The author intended `set` to be a verb, but in the context of the if statement it _feels_ like an adjective. So the statement reads as "If the username attribute was previously set to unclebob" and not "set the username attribute to unclebob and if that worked then...".

The solution is to separate the command from the query so that the ambiguity cannot occur.
```
if (attributeExists("username")) {
  setAttribute("username", "unclebob");
  // ...
}
```

## Prefer Exceptions to Returning Error Codes

Returning error codes from command functions is a subtle violation of command query separation. It promotes commands being used as expressions in the predicates of `if` statements.

When you return an error code, you create the problem that the caller must deal with the error immediately.

### Extract Try/Catch Blocks

It is better to extract the bodies of the `try` and `catch` blocks out into functions of their own. This provides a nice separation that makes the code easier to understand and modify.

### Error Handling Is One Thing

Functions should do one thing. Error handing is one thing. Thus, a function that handles errors should do nothing else.

### The `Error.java` Dependency Magnet

Classes (most of the time `enum`s) which hold error codes are _dependency magnet_ (many other classes depend on them) and should be avoided. It is better to use exceptions because, for example, they can be added without forcing any recompilation or redeployment.

## Don’t Repeat Yourself

Duplication may be the root of all evil in software. Many principles and practices have been created for the purpose of controlling or eliminating it. Object-oriented programming serves to concentrate code into base classes that would otherwise be redundant. Structured programming, Aspect Oriented Programming, Component Oriented Programming, are all, in part, strategies for eliminating duplication.

## Structured Programming
