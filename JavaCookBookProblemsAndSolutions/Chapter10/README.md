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

## 10.14 Reading/Writing Binary Data

### Problem

You need to read or write binary data, as opposed to text.

### Solution

Use a `DataInputStream` or `DataOutputStream`.

## 10.15 Reading and Writing JAR or ZIP Archives

### Problem

You need to create and/or extract from a JAR archive or a file in the well-known *ZIP* archive format, as established by PkZip and used by Unix zip/unzip and *WinZip*.

### Solution

Use the `ZipFile` and `ZipEntry` classes and the stream classes to which they provide access.

## 10.16 Finding Files in a Filesystem-Neutral Way with `getResource()` and `getResourceAsStream()`

### Problem

You want to load objects or files without referring to their absolute location in the filesystem. You might want to do this for one of the following reasons:

* You are in a server (Java EE) environment.
* You want to be independent of file paths.
* You want to read a file in a unit test.
* You expect users to deploy the resource “somewhere” on the CLASSPATH (possibly even inside a JAR file).

### Solution

Use `getClass()` or `getClassLoader()` and either `getResource()` or `getResourceAsStream()`.

## 10.17 Getting File Information: Files and Path

### Problem

You need to know all you can about a given file on disk.

### Solution

Use `java.nio.file.Files` methods.

## 10.18 Creating a New File or Directory

### Problem

You need to create a new file on disk but not write any data into it; you need to create a directory before you can create files in it.

### Solution

For an empty file, use a `java.nio.file.Files` object’s `createFile(Path)` method. Use the Files class’s `createDirectory()` or `createDirectories()` method to create a directory.

## 10.21 Creating a Transient/Temporary File

### Problem

You need to create a file with a unique temporary filename and/or or arrange for a file to be deleted when your program is finished.

### Solution

Use the `java.nio.file.Files createTempFile()` or `createTempDirectory()` method. Use one of several methods to ensure your file is deleted on exit.

## 10.23 Getting the Directory Roots

### Problem

You want to know about the top-level directories, such as `C:\` and `D:\` on Windows.

### Solution

Use the static method `FileSystems.getDefault().getRootDirectories()`, which returns an `Iterable` of `Path` objects, one for each root directory.

## 10.24 Using the FileWatcher Service to Get Notified About File Changes

### Problem

You want to be notified when some other application updates one or more of the files in which you are interested.

### Solution

Use the `java.nio.file.FileWatchService` to get notified of changes to files automatically, instead of having to examine the files periodically.
