# Chapter 2: Anatomy of a modular application

The most common way to modularize applications is by a separation of concerns.

A `requires` directive contains a module name and tells the JVM that the declaring module depends on the one given by the directive.

For the own module to be found, the module path has to be used, a concept paralleling the class path but, as the name suggests, expecting modular JARs instead of plain JARs. It will be scanned when the compiler searches for referenced modules. To define the module path, `javac` has a new option: `--module-path`, or `-p` for short. (The same line of thought is true for launching the application with the JVM. Accordingly, the same options, `--module-path` and `-p`, were added to java as well, where they function just the same)

All you need to do is to call `java`, specify the module path so java knows where to find the artifacts your application consists of, and tell it which module to launch. Resolving all dependencies, making sure there are no conflicts or ambiguous situations, and launching with just the right set of modules are handled by the module system.

Example:

```shell
java
 --module-path mods:libs
 --module monitor
```
