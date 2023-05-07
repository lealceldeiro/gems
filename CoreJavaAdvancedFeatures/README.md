# Core Java: Advanced Features

Main notes taken from the book [Core Java: Advanced Features](https://www.amazon.com/Core-Java-Advanced-Features-Oracle-dp-0137871074/dp/0137871074)

Source code at https://horstmann.com/corejava/corejava.zip

## Chapter 1. Streams

If you have an `Iterable` that is not a collection, you can turn it into a stream by calling

```java
StreamSupport.stream(iterable.spliterator(), false);
```

If you have an Iterator and want a stream of its results, use

```java
StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
```

If you want to take one action if the `Optional` has a value and another action if it doesn't, use `ifPresentOrElse`.
i.e.:

```java
optionalValue.ifPresentOrElse(v -> System.out.println("Found " + v), () -> logger.warning("No match"));
```

You can substitute an alternative `Optional` for an empty `Optional` with the `or` method. The alternative is computed
lazily. i.e.:

```java
Optional<String> result = optionalString.or(() -> /*Supply an Optional*/ alternatives.stream().findFirst());
```

Method parameters of type `Optional` are questionable. They make the call unpleasant in the common case where the
requested value is present. Instead, consider two overloaded versions of the method, with and without the parameter. (On
the other hand, returning an `Optional` is fine. It is the proper way to indicate that a function may not have a result)
.

If you want to reduce the stream results to a sum, count, average, maximum, or minimum, use one of the
`summarizing`(`Int`|`Long`|`Double`) methods. These methods take a function that maps the stream objects to numbers and
yield a result of type (`Int`|`Long`|`Double`)`SummaryStatistics`, simultaneously computing the sum, count, average,
maximum, and minimum. i.e.:

```java
IntSummaryStatistics summary = stream.collect(Collectors.summarizingInt(String::length));
double averageWordLength = summary.getAverage();
double maxWordLength = summary.getMax();
```

**Downstream Collectors examples**

- Count how many locales there are for each country:

```java
Map<String, Long> countryToLocaleCounts = locales.collect(groupingBy(Locale::getCountry, counting()));
```

- Compute the average of populations per state in a stream of cities:

```java
Map<String, Integer> stateToCityPopulation = cities.collect(groupingBy(City::state, averagingInt(City::population)));
```

- Largest city per state:

```java
Map<String, Optional<City>> stateToLargestCity = cities.collect(groupingBy(City::state, maxBy(Comparator.comparing(City::population))));
```

The `collectingAndThen` collector adds a final processing step behind a collector.
Example: know how many distinct results there are, we collect them into a `Set` and then compute the size:

```java
Map<Character, Integer> stringCountsByStartingLetter = strings.collect(groupingBy(s -> s.charAt(0), collectingAndThen(toSet(), Set::size)));
```

The mapping collector does the opposite. It applies a function to each collected element and passes the results to a
downstream collector. Example, group strings by their first character; then within each group, produce the lengths and
collect them in a set:

```java
Map<Character, Set<Integer>> stringLengthsByStartingLetter = strings.collect(groupingBy(s -> s.charAt(0), mapping(String::length, toSet())));
```

There is a flatMapping method as well, for use with functions that return streams.

## Chapter 2. Input and Output

The abstract classes `InputStream` and `OutputStream` are the basis for a hierarchy of input/output (I/O) classes.

`InputStream` and `OutputStream` classes let you read and write individual bytes and arrays of bytes.

`DataInputStream` and `DataOutputStream` let you read and write all the primitive Java types in binary format.

`ZipInputStream` and `ZipOutputStream` let you read and write files in the familiar ZIP compression format.

For Unicode text, on the other hand, you can use subclasses of the abstract classes `Reader` and `Writer`. The basic
methods of the `Reader` and `Writer` classes are similar to those of `InputStream` and `OutputStream`.

```java
// returns either a UTF-16 code unit (as an integer between 0 and 65535) or -1 when you have reached the end of the file
abstract int read()

// it's called with a Unicode code unit
abstract void write(int c)
```

The `OutputStreamWriter` class turns an output stream of Unicode code units into a stream of bytes, using a chosen
character encoding. Conversely, the `InputStreamReader` class turns an input stream that contains bytes (specifying
characters in some character encoding) into a reader that emits Unicode code units.

Example: make an input reader that reads keystrokes from the console and converts them to Unicode, assuming the default
character encoding used by the host system:

```java
var in = new InputStreamReader(System.in);
```

This one specified the character encoding:

```java
var in = new InputStreamReader(new FileInputStream("data.txt"), StandardCharsets.UTF_8);
```

The `Reader` and `Writer` classes have only basic methods to read and write individual characters. As with streams, you
use subclasses for processing strings and numbers.

For text output, use a `PrintWriter`. That class has methods to print strings and numbers in text format. In order to
print to a file, construct a `PrintStream` from a file name and a character encoding:

```java
var out = new PrintWriter("employee.txt", StandardCharsets.UTF_8);
```

If the writer is set to *autoflush* mode, all characters in the buffer are sent to their destination whenever `println`
is called (Print writers are always buffered).

By default, *autoflushing* is not enabled. You can enable or disable *autoflushing* by using the
`PrintWriter(Writer writer, boolean autoFlush)` constructor:

```java
var out = new PrintWriter(new OutputStreamWriter(new FileOutputStream("employee.txt"), StandardCharsets.UTF_8),
                          true); // autoflush
```

The easiest way to process arbitrary text is the `Scanner` class.

You can read a short text file into a string like this: `String content = Files.readString(path, charset);`

You can read a file as a sequence of lines like this: `List<String> lines = Files.readAllLines(path, charset);`

You can process a large file like this:

```java
try (Stream<String> lines = Files.lines(path, charset)) {
   // use lines here
}
```

You can also use a scanner to read tokens-strings that are separated by a delimiter.
The default delimiter is whitespace, but it can be changed to any other delimiter (including a regular expression).

Example:

```java
Scanner sc = new Scanner(System.in);
in.useDelimiter("\\W+");
while (in.hasNext()) {
  String word = in.next();
}
```

Alternatively, you can obtain a stream of all tokens: `Stream<String> words = in.tokens();`.

The `RandomAccessFile` class lets you read or write data anywhere in a file. Example:

```java
var in = new RandomAccessFile("employee.dat", "r");     // r: read-only
var inOut = new RandomAccessFile("employee.dat", "rw"); // rw: read and write
```

Example, store employee records in a random-access file:

```java
// read data
long n = 3;
in.seek((n - 1) * RECORD_SIZE); // position the file pointer to the third record
var e = new Employee();
e.readData(in);

// modify the record and save it back into the same location
in.seek((n - 1) * RECORD_SIZE); // set the file pointer back to the beginning of the record
e.writeData(out);
```

To determine the total number of bytes in a file, use the `length` method.
The total number of records is the length divided by the size of each record.

```java
long nbytes = in.length(); // length in bytes
int nrecords = (int) (nbytes / RECORD_SIZE);
```
