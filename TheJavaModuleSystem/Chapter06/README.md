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

