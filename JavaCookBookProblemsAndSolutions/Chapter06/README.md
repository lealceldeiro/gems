# Chapter 6: Dates and Times

## 6.1 Finding Today’s Date

### Problem

You want to find today’s date and/or time.

### Solution

Invoke the appropriate builder to obtain a `LocalDate`, `LocalTime`, or `LocalDateTime` object and call its `toString()` method.

In full-scale applications, it’s recommended to pass a `Clock` instance into all the `now()` methods.

## 6.2 Formatting Dates and Times

### Problem

You want to provide better formatting for date and time objects.

### Solution

Use `java.time.format.DateTimeFormatter`.
