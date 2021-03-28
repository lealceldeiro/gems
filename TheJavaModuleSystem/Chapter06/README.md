# Chapter 6: Compatibility challenges when moving to Java 9 or later

Splitting packages no longer works in Java 9+ and later.

Classes on the class path in a package that’s distributed with Java are effectively invisible:

- If Java contains a class with the same fully qualified name, that one will be loaded.
- If the Java built-in version of the package doesn’t contain the required class, the result is the compile error or NoClassDefFoundError that I showed earlier. And that happens regardless of whether the class is present on the class path.

### 6.1.3 Dropping in Third-party Implementations of JEE Modules

Both the compiler and runtime offer the `--upgrade-module-path` option, which accepts a list of directories, formatted like the ones for the module path. When the module system creates the module graph, it searches those directories for artifacts and uses them to replace upgradeable modules. The six JEE modules are always upgradeable:

- `java.activation`
- `java.corba`
- `java.transaction`
- `java.xml.bind`
- `java.xml.ws`
- `java.xml.ws.annotation`

## 6.2 Casting to `URLClassLoader`

From Java 9 on, the application class loader is the new type `AppClassLoader` (and its supertype is the also new `BuiltinClassLoader`). That means the occasional `(URLClassLoader) getClass().getClassLoader()` sequence will no longer execute successfully.

## 6.3 Updated run-time image directory layout

- The JDK code is now modularized and should hence be delivered in individual modules instead of monolithic JARs like `rt.jar` and `tools.jar`
- With a modularized Java code base and a tool like `jlink`, run-time images can be created from any set of modules.

Overall, the new layout is much simpler:

- A single *bin* directory and no duplicate binaries
- A single *lib* directory
A single directory, *conf*, to contain all files meant for configuration

![stucture-jdk9](https://user-images.githubusercontent.com/15990580/112745869-2ace9900-8fb4-11eb-86f6-4c5b5d6b73fb.png)

The URL gotten for system resources, for example from `ClasLoader::getSystemResource`, has also changed. It used to be of the following form, where `${path}` is something like `java/lang/String.class`: `jar:file:${java-home}/lib/rt.jar!${path}`

It now looks like this: `jrt:/${module}/${path}`

Furthermore, the `Class::getResource*` and `ClassLoader::getResource*` methods no longer read JDK-internal resources. Instead, to access module-internal resources, `Module::getResourceAsStream` must be used or a JRT file system as follows should be created:

```java
FileSystem fs = FileSystems.getFileSystem(URI.create("jrt:/"));
fs.getPath("java.base", "java/lang/String.class"));
```

## 6.4 Selecting, replacing, and extending the platform

### 6.4.1	No More Compact Profiles

Compact profiles were created as an interim solution when it became apparent the module system wouldn’t be released with Java 8. With the module system in play, much more flexible run-time images can be created with `jlink`, and compact profiles are no longer needed.

The Java 9+ compiler will hence only accept `-profile` if compiling for Java 8. To compile against a specific selection of modules, the `--limit-modules` option can be used.

These are the modules needed to get the same APIs as the three compact profiles:

- *Compact1 profile*: `java.base`, `java.logging`, `java.scripting`
- *Compact2 profile*: `java.base`, `java.logging`, `java.scripting`, `java.rmi`, `java.sql`, `java.xml`
- *Compact3 profile*: `java.base`, `java.logging`, `java.scripting`, `java.rmi`, `java.sql`, `java.xml`, `java.compiler`, `java.instrument`, `java.management`, `java.naming`, `java.prefs`, `java.security.jgss`, `java.security.sasl`, `java.sql.rowset`, `java.xml.crypto`

Instead of relying on a fixed selection, sometimes is better to use `jlink` to create an image with only the platform modules needed.

### 6.4.2 Extension Mechanism Removed

Before Java 9, the extension mechanism let us add classes to the JDK without having to place them on the class path. It loaded them from various directories: from directories named by the system property `java.ext.dirs`, from `lib/ext` in the JRE, or from a platform-specific system-wide directory. Java 9 removes this feature, and the compiler and runtime will exit with an error if the JRE directory exists or the system property is set.

Alternatives are as follows:

- The `java` and `javac` option `--patch-module` injects content into modules.
- The `java` and `javac` option `--upgrade-module-path` replaces an upgradeable platform module with another one.
- The extending artifacts can be placed on the class path.

### 6.4.3 Endorsed Standards Override Mechanism Removed

Before Java 9, the endorsed standards override mechanism let us replace certain APIs with custom implementations. It loaded them from the directories named by the system property java.endorsed.dirs or the lib/endorsed directory in the JRE. Java 9 removes this feature, and the compiler and runtime will exit with an error if the JRE directory exists or the system property is set. The alternatives are the same as for the extension mechanism.

### 6.4.4 Some Boot Class Path Options Removed

The `-Xbootclasspath` and `-Xbootclasspath/p` options were removed. The following options instead should be used instead:

- The `javac` option `--system` specifies an alternate source of system modules.
- The `javac` option `--release` specifies an alternate platform version.
- The `java` and `javac` option `--patch-module` injects content into modules in the initial module graph.

### 6.4.6 JRE Version Selection Removed

Before Java 9, you could use the `-version:N` option on java (or the corresponding manifest entry) to launch the application with a JRE of version *N*. In Java 9, the feature was removed: the Java launcher quits with an error for the command-line option and prints a warning for the manifest entry while otherwise ignoring it.
