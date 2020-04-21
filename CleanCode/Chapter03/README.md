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
