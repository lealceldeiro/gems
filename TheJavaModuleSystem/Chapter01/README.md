# Chapter 1: First piece of the puzzle

Each module (called a *part* too), has clear responsibilities and a well-defined contract it implements. It’s self-contained, it’s opaque to its clients, and it can be replaced by a different module as long as that one implements the same contract. Its few dependencies are APIs, not implementations.

This is what modularity is all about: achieving maintainability and flexibility as emergent properties of well-designed modules.

## 1.3	Complications before Java 9

**Unexpressed dependencies between jars**

**Shadowing classes with the same name**

Shadowing class: Because a class will be loaded from the first JAR on the class path that contains it, it makes all other classes of the same name unavailable—it’s said to shadow them.

**Conflicts between different versions of the same project**

Version conflicts arise when two required libraries depend on different, incompatible versions of a third library.

**Complex class loading**

**Weak encapsulation across jars**

**Security checks have to be handcrafted**

**Poor startup performance**

**Rigid java runtime**

Before Java 8, there was no way to install a subset of the JRE. All Java installations had support for, for example, XML, SQL, and Swing, which many use cases don’t require.

## 1.4	Bird's-eye view of the module system

Modules are the basic building block of the JPMS. Like JARs, they’re containers for types and resources; but unlike JARs, they have additional characteristics. These are the most fundamental ones:

  - A name, preferably one that’s globally unique
  - Declarations of dependencies on other modules
  - A clearly defined API that consists of exported packages

These are some of the more important platform modules:

  - `java.base` — The module without which no JVM program functions. Contains packages like `java.lang` and `java.util`.
  - `java.desktop` — Contains the Abstract Window Toolkit (AWT; packages `java.awt.*`), Swing (packages `javax.swing.*`), and more APIs, among them JavaBeans (package `java.beans.*`).
  - `java.logging` — Contains the package `java.util.logging`.
  - `java.rmi` — Remote Method Invocation (RMI).
  - `java.xml` — Contains most of the XML API: Java API for XML Processing (JAXP), Streaming API for XML (StAX), Simple API for XML (SAX), and the document object model (DOM).
  - `java.xml.bind` — Java Architecture for XML Binding (JAXB).
  - `java.sql` — Java Database Connectivity (JDBC).
  - `java.sql.rowset` — JDBC RowSet API.
  - `java.se` — References the modules making up the core Java SE API. This is a so-called aggregator module.
  - `java.se.ee` — References the modules making up the full Java SE API (another aggregator).

See a list of all modules contained in a JDK or JRE: `java --list-modules`

See a details for a single module: `java --describe-module <module_name>`. i.e.: `java --describe-module java.sql`

A fundamental aspect of the module system is: everything is a module! Or, more precisely, no matter how types and resources are presented to the compiler or the virtual machine, they will end up in a module. Modules are at the heart of the module system. Everything else can ultimately be traced back to them and their name, their declaration of dependencies, and the API they export.

## 1.5 First module

The only thing needed to create a module from a source code is to add a file called `module-info.java`, a module declaration, to the source folder and fill it with the module’s *name*, *dependencies* on other modules, and the *packages that make up its public API*. Example:

```java
module my.xml.app {
    requires java.base;  // requiring java.base isn’t actually necessary
    requires java.xml;
    exports my.xml.api;
}
```

Whenever a module first accesses a type in another module, the JPMS verifies that three requirements are met:

- The accessed type needs to be public.
- The module owning that type must have exported the package containing it.
- In the module graph, the accessing module must be connected to the owning one.
