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


The coexistence of the class path and the module path and their respective treatment of plain and modular artifacts is the key to incremental migrations of large applications to the module system.

## 1.6	Goals of the module system

### 1.6.1	Reliable Configuration: Leaving no Jar Behind

The fact that dependencies can be found missing at launch time, as opposed to only when the first class is needed, is a big win.

Together, this makes a system’s configuration more reliable than it used to be, because only well-formed launch configurations will pass these tests. If they do, the JVM can turn the conceptual dependency graph into a module graph, which replaces the ball of mud with a structured view of the running system, much like we may have it.

### 1.6.2	Strong Encapsulation: Making Module-Internal Code Inaccessible

Another key goal of the module system is to enable modules to strongly encapsulate their internals and export only specific functionality.

### 1.6.3	Automated Security and Improved Maintainability

The strong encapsulation of module-internal APIs can greatly improve security and maintainability. It helps with security because critical code is effectively hidden from code that doesn’t require its use. It also makes maintenance easier, because a module’s public API can more easily be kept small.

### 1.6.4	Improved Startup Performance

With clearer bounds of where code is used, existing optimization techniques can be used more effectively.

### 1.6.5	Scalable Java Platform

With the JDK being modularized, we can *cherry-pick the functionality* we need and create JREs consisting of only the required modules.

### 1.6.6	Non-Goals

- The JPMS has no concept of versions: it won’t distinguish two different versions of the same module
- The JPMS offers no mechanism to search for or download existing modules from a centralized repository or to publish new ones.
- It’s also not the goal of the JPMS to model a dynamic module graph, where individual artifacts can show up or disappear at run time.

# Summary

- A software system can be visualized as a graph, which often shows (un)desired properties of the system.
- On the level of JARs, Java used to have no understanding of that graph. This led to various problems, among them JAR hell, manual security, and poor maintainability.
- The Java Platform Module System exists to make Java understand the JAR graph, which brings artifact-level modularity to the language. The most important goals are reliable configuration and strong encapsulation as well as improved security, maintainability, and performance.
- This is achieved by introducing modules: basically, JARs with an additional descriptor. The compiler and runtime interpret the described information in order to build the graph of artifact dependencies and provide the promised benefits.
