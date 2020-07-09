# Chapter 1: Getting Started: Compiling and Running Java

## 1.1 Compiling and Running Java: Standard JDK

### Problem

You need to compile and run your Java program.

### Solution

```shell
# compile the java class (HelloWorld.java)
javac HelloWorld.java

# run the compiled java class (HelloWorld.class)
java HelloWorld

Hellow World!
```
From Java 11 you can directly do

```shell
java HelloWorld.java

Hello, World!
```

To specify the output directory (for the `.class` file), `-d` can be used. i.e:

```shell
javac HelloWorld.java -d out/
```

## 1.3 Compiling, Running, and Testing with an IDE

### Problem

It is cumbersome to use several tools for the various development tasks.

### Solution

Use an Integrated Development Environment (IDE), which combines editing, testing, compiling, running, debugging, and package management.

## 1.4 Exploring Java with JShell

### Problem

You want to try out Java expressions and APIs quickly, without having to create a file with

```java
public class X {
  public static void main(String[] args) {
    // …
  }
}
```

every time.

### Solution

Use JShell, Java’s REPL (Read-Evaluate-Print-Loop) interpreter.

> Starting with Java 11, JShell is included as a standard part of Java

## 1.5 Using CLASSPATH Effectively

### Problem

You need to keep your class files in a common directory, or you’re wrestling with CLASSPATH.

### Solution

Set CLASSPATH to the list of directories and/or JAR files that contain the classes you want.
