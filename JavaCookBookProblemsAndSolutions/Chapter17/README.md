# Chapter 17: Reflection, or “A Class Named Class”

## 17.1 Getting a Class Descriptor

### Problem

You want to get a `Class` object from a class name or instance.

### Solution

If the type name is known at compile time, you can get the class instance using the compiler keyword `.class`, which works on any type that is known at compile time, even the eight primitive types.

Otherwise, if you have an object (an instance of a class), you can call the `java.lang.Object` method `getClass()`, which returns the Class object for the object’s class.

## 17.2 Finding and Using Methods and Fields

### Problem

You need to find arbitrary method or field names in arbitrary classes.

### Solution

Use the reflection package `java.lang.reflect`.

## 17.4 Loading and Instantiating a Class Dynamically

### Problem

You want to load classes dynamically, just like web servers load your servlets.

### Solution

Use `class.forName("ClassName");` and the class’s `newInstance()` method.

## 17.5 Constructing a Class from Scratch with a ClassLoader

### Problem

You need to load a class from a nonstandard location and run its methods.

### Solution

Examine the existing loaders such as `java.net.URLClassLoader`. If none is suitable, write and use your own `ClassLoader`.
