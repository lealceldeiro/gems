# Chapter 10: Input and Output: Reading, Writing, and Directory Tricks

## 10.2 Reading a Text File

### Problem

How do I open and read a text file and then either process it a line at a time, or get a collection of all the lines?

### Solution

Use the `Files::lines()` method, which returns a `Stream` of `Strings`.

i.e.:

```java
Files.lines(Path.of("myFile.txt")).forEach(System.out::println);
```

Or, use `Files.newBufferedReader()`, `Files.newBufferedWriter()`, `Files.newInputStream()`, and `Files.newOutputStream()`.

Or, construct a `FileReader` or a `FileInputStream`. Once you have that, construct a `BufferedReader`, and use the older `while ((line == readLine()) != null)` pattern.
