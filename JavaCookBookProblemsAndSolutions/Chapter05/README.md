# Chapter 5: Numbers

## 5.1 Checking Whether a String Is a Valid Number

### Problem

You need to check whether a given string contains a valid number, and, if so, convert it to binary (internal) form.

### Solution

To accomplish this, use the appropriate wrapper class’s conversion routine and catch the `NumberFormatException`.

## 5.3 Taking a Fraction of an Integer Without Using Floating Point

### Problem

You want to multiply an integer by a fraction without converting the fraction to a floating-point number.

### Solution

Multiply the integer by the numerator and divide by the denominator.

This technique should be used only when efficiency is more important than clarity because it tends to detract from the readability—and therefore the maintainability—of your code.

You should beware of the possibility of numeric overflow and avoid this optimization if you cannot guarantee that the multiplication by the numerator will not overflow.

## 5.4 Working with Floating-Point Numbers

### Problem

You want to be able to compare and round floating-point numbers.

### Solution

Compare with the `INFINITY` constants, and use `isNaN()` to check for `NaN` (not a number).

Compare floating values with an epsilon value.

Round floating point values with `Math.round()` or custom code.

## 5.5 Formatting Numbers

### Problem

You need to format numbers.

### Solution

Use a `NumberFormat` subclass.

## 5.6 Converting Among Binary, Octal, Decimal, and Hexadecimal

### Problem

You want to display an integer as a series of bits—for example, when interacting with certain hardware devices—or in some alternative number base (binary is base 2, octal is base 8, decimal is 10, hexadecimal is 16). You want to convert a binary number or a hexadecimal value into an integer.

### Solution

The class `java.lang.Integer` provides the solutions. Most of the time you can use `Integer.parseInt(String input, int radix)` to convert from any type of number to an `Integer`, and `Integer.toString(int input, int radix)` to go the other way.

## 5.7 Operating on a Series of Integers

### Problem

You need to work on a range of integers.

### Solution

For a contiguous set, use `IntStream::range` and `rangeClosed`, or the older for loop.

For discontinuous ranges of numbers, use a `java.util.BitSet`.

## 5.8 Formatting with Correct Plurals

### Problem

You’re printing something like *"We used " + n + " items"*, but in English, *“We used 1 items”* is ungrammatical. You want *“We used 1 item”.

### Solution

Use a [`ChoiceFormat`](https://docs.oracle.com/javase/8/docs/api/java/text/ChoiceFormat.html) or a conditional statement.

Use Java’s ternary operator `(condition ? trueValue : falseValue)` in a string concatenation. Both zero and plurals get an “s” appended to the noun in English (“no books, one book, two books”), so we test for `n == 1`.

i.e.:

Given

```java
double[] limits = { 0, 1, 2 };
int[] data = { -1, 0, 1, 2, 3 };
```

`ChoiceFormat` can be used to get a *plurized* format:

```java

String[] formats = { "reviews", "review", "reviews"};
ChoiceFormat pluralizedFormat = new ChoiceFormat(limits, formats);

for (int i : data) {
    System.out.println("Found " + i + " " + pluralizedFormat.format(i));  // Found -1 reviews
                                                                          // Found 0 reviews
                                                                          // Found 1 review
                                                                          // Found 2 reviews
                                                                          // Found 3 reviews
}
```

and to get a *quantized* format:

```java
ChoiceFormat quantizedFormat = new ChoiceFormat("0#no reviews|1#one review|1<many reviews");

for (int i : data) {
    System.out.println("Found " + quantizedFormat.format(i));             // Found no reviews
                                                                          // Found no reviews
                                                                          // Found one review
                                                                          // Found many reviews
                                                                          // Found many reviews
}
```

In addition to `ChoiceFormat`, the same result can be achieved with a `MessageFormat`.

## 5.9 Generating Random Numbers

### Problem

You need to generate pseudorandom numbers in a hurry.

### Solution

Use `java.lang.Math.random()` to generate random numbers. There is no claim that the random values it returns are very good random numbers, however. Like most software-only implementations, these are Pseudorandom Number Generators (PRNGs), meaning that the numbers are not totally random, but devised from an algorithm. That said, they are adequate for casual use.

This method only generates double values. If you need integers, construct a `java.util.Random` (or [ThreadLocalRandom](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadLocalRandom.html) or [SecureRandom](https://docs.oracle.com/javase/8/docs/api/java/security/SecureRandom.html)) object and call its nextInt() method; if you pass it an integer value, this will become the upper bound.
