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
