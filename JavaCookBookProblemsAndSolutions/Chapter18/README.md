# Chapter 18: Using Java with Other Languages

## 18.1 Running an External Program from Java

### Problem

You want to run an external program from within a Java program.

### Solution

Use one of the `exec()` methods in the `java.lang.Runtime` class. Or set up a `ProcessBuilder` and call its `start()` method.

## 18.2 Running a Program and Capturing Its Output

### Problem

You want to run a program but also capture its output.

### Solution

Use the `Process` object’s `getInputStream()` read and copy the contents to `System.out` or wherever you want them.

## 18.3 Calling Other Languages via `javax.script`

### Problem

You want to invoke a script written in some other language from within your Java program, running in the JVM, with the ability to pass variables directly to/from the other language.

### Solution

If the script you want is written in any of the two-dozen-plus supported languages, use `javax.script`. Those languages include awk, Perl, Python, Ruby, BeanShell, PNuts, Ksh/Bash, R (Renjin), and several implementations of JavaScript.

## 18.7 Calling Other Languages via Native Code or Calling Java from Native Code

### Problem

You wish to call native C/C++ functions from Java or the other way around: you need to call Java from C/C++ code.

### Solution

Use JNI (Java Native Interface).
