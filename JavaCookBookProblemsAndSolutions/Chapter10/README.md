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

## 10.3 Reading from the Standard Input or from the Console/Controlling Terminal

### Problem

You want to read from the program’s standard input or directly from the program’s controlling terminal or console terminal.

### Solution

For the standard input, read bytes by wrapping a `BufferedInputStream()` around `System.in`. For reading text, use an `InputStreamReader` and a `BufferedReader`. For the console or controlling terminal, use Java’s `System.console()` method to obtain a `Console` object, and use its methods.

## 10.7 Scanning Input with Grammatical Structure

### Problem

You need to parse a file whose structure can be described as grammatical (in the sense of computer languages, not natural languages).

### Solution

Use one of many [parser generators](https://java-source.net/open-source/parser-generators).

## 10.11 Reading/Writing a Different Character Set

### Problem

You need to read or write a text file using a particular encoding.

### Solution

Convert the text to or from internal Unicode by specifying a converter when you construct an `InputStreamReader` or `PrintWriter`.

The `InputStreamReader` and `OutputStreamWriter` constructors are the only places where you can specify the name of an encoding to be used.

> The [native2ascii](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/native2ascii.html) command converts encoded files supported by the Java Runtime Environment (JRE) to files encoded in ASCII, using Unicode escapes (\uxxxx) notation for all characters that are not part of the ASCII character set. This process is required for properties files that contain characters not in ISO-8859-1 character sets. The tool can also perform the reverse conversion.
