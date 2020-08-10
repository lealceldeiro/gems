# Chapter 14: Processing JSON Data

## 14.2 Parsing and Writing JSON with Jackson

### Problem

You want to read and/or write JSON using a full-function JSON API.

### Solution

Use Jackson, the full-blown JSON API.

## 14.3 Parsing and Writing JSON with `org.json`

### Problem

You want to read/write JSON using a midsized, widely used JSON API.

### Solution
Consider using the `org.json` API , also known as JSON-Java; it’s widely used and is also used in Android.

## 14.4 Parsing and Writing JSON with JSON-B

### Problem

You want to read/write JSON using a midsized, standards-conforming JSON API.

### Solution

Consider using JSON-B, the new Java standard (JSR-367).

## 14.5 Finding JSON Elements with JSON Pointer

### Problem

You have a JSON document and want to extract only selected values from it.

### Solution

Use `javax.json`’s implementation of JSON Pointer, the standard API for extracting selected elements from JSON.
