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

## 6.3 Converting Among Dates/Times, YMDHMS, and Epoch Seconds

### Problem

You need to convert among dates/times, YMDHMS, epoch seconds, or some other numeric value.

### Solution

Use the appropriate date/time factory or retrieval methods.

## 6.4 Parsing Strings into Dates

### Problem

You need to convert user input into `java.time` objects.

### Solution

Use a `parse()` method.

## 6.5 Difference Between Two Dates

### Problem

You need to compute the difference between two dates.

### Solution

Use the static method `Period.between()` to find the difference between two `LocalDates`.
