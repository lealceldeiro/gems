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

## 6.6 Adding to or Subtracting from a Date

### Problem

You need to add or subtract a fixed period to or from a date.

### Solution

Create a past or future date by using a locution such as `Local⁠Date.plus(Period.ofDays(N));`.

## 6.7 Handling Recurring Events

### Problem

You need to deal with recurring dates, for example, the third Wednesday of every month.

### Solution

Use the `TemporalAdjusters` class.

## 6.9 Interfacing with Legacy `Date` and `Calendar` Classes

### Problem

You need to deal with the old Date and Calendar classes.

### Solution

Assuming you have code using the original `java.util.Date` and `java.util.Calendar`, you can convert values as needed using conversion methods.
