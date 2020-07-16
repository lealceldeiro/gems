# Chapter 10: Input and Output: Reading, Writing, and Directory Tricks

## 10.2 Reading a Text File

### Problem

How do I open and read a text file and then either process it a line at a time, or get a collection of all the lines?

### Solution

Use the `Files::lines()` method, which returns a `Stream` of `Strings` (or one if its variants).

Or, use `Files.newBufferedReader()`, `Files.newBufferedWriter()`, `Files.newInputStream()`, and `Files.newOutputStream()`.

Or, construct a `FileReader` or a `FileInputStream`. Once you have that, construct a `BufferedReader`, and use the older `while ((line == readLine()) != null)` pattern.

To read the entire contents of a file into single `String`, in Java 8+, use `String input = Files.readString(Path.of("myFile.txt")));`.

i.e.:

```java
// Using Path.lines
Files.lines(Path.of("myFile.txt")).forEach(System.out::println);

// Using Path.readAllLines
List<String> lines = Files.readAllLines(Path.of("myFile.txt"));
lines.forEach(System.out::println);

// Using BufferedReader.lines().forEach
new BufferedReader(new FileReader("myFile.txt")).lines().forEach(System.out::println);

// The old-fashioned way
BufferedReader is = new BufferedReader(new FileReader("myFile.txt"));
String line;
while ((line = is.readLine()) != null) {
    System.out.println(line);
}
```
