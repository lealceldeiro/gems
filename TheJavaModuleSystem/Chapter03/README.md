# Defining modules and their properties

Modular JAR and module descriptor: A modular JAR is just a plain JAR, except for one small detail. Its root directory contains a module descriptor: a `module-info.class` file.

Module declaration: A module descriptor is compiled from a module declaration. By convention, this is a `module-info.java` file in the project’s root source folder. The declaration is the pivotal element of modules and thus the module system.

Declaration vs. description: The declaration is source code and the description is bytecode, but they’re just different forms of the same idea: something that defines a module’s properties. The context often leaves only one option, so it’s usually clear which form is meant.

In addition to the `module` keyword, a declaration starts by giving the module a name. This has to be an identifier, meaning it must adhere to the same rules as, for example, a package name. Module names are usually lowercase and hierarchically structured with dots. It’s important that the name is

- Globally unique
- Stable

Dependencies: Dependencies are declared with requires directives, which consist of the keyword followed by a module name. The directive states that the declared module depends on the named one and requires it during compilation and at run time.

Exported packages: The keyword exports is followed by the name of a package the module contains. Only exported packages are usable outside the module; all others are strongly encapsulated within it.

### Module types

- Application modules — A non-JDK module; the modules Java developers create for their own projects, be they libraries, frameworks, or applications. These are found on the module path. For the time being, they will be modular JARs.
- Initial module — Application module where compilation starts (for `javac`) or containing the main method (for `java`).
- Root modules — Where the JPMS starts resolving dependencies. In addition to containing the main class or the code to compile, the initial module is also a root module. In tricky situations it can become necessary to define root modules beyond the initial one.
- Platform modules — Modules that make up the JDK. These are defined by the Java SE Platform Specification (prefixed with `java.`) as well as JDK-specific modules (prefixed with `jdk.`). They’re stored in optimized form in a modules file in the runtime’s libs directory.
- Incubator modules — Nonstandard platform modules whose names always start with `jdk.incubator`. They contain experimental APIs that could benefit from being tested by adventurous developers before being set in stone.
- System modules — In addition to creating a run-time image from a subset of platform modules, `jlink` can also include application modules. The platform and application modules found in such an image are collectively called its *system modules*. To list them, use the `java` command in the image’s bin directory and call `java --list-modules`.
- Observable modules — All platform modules in the current runtime as well as all application modules specified on the command line; modules that the JPMS can use to fulfill dependencies. Taken together, these modules make up the *universe of observable modules*.
- Base module — The distinction between application and platform modules exists only to make communication easier. To the module system, all modules are the same, except one: the platform module `java.base`, the so-called base module, plays a particular role.
- Explicit modules — Platform modules and most application modules that have module descriptors given to them by the module’s creator.
- Automatic modules — Named modules without a module description (plain JARs on the module path). These are application modules created by the runtime, not a developer.
- Named modules —The set of explicit modules and automatic modules. These modules have a name, either defined by a descriptor or inferred by the JPMS.
- Unnamed modules — Modules that aren’t named (class path content) and hence aren’t explicit.
