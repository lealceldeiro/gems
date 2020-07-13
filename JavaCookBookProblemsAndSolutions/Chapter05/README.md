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
