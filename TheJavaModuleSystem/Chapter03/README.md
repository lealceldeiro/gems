# Chapter 3: Defining modules and their properties

Modular JAR and module descriptor: A modular JAR is just a plain JAR, except for one small detail. Its root directory contains a module descriptor: a `module-info.class` file.

Module declaration: A module descriptor is compiled from a module declaration. By convention, this is a `module-info.java` file in the project’s root source folder. The declaration is the pivotal element of modules and thus the module system.

Declaration vs. description: The declaration is source code and the description is bytecode, but they’re just different forms of the same idea: something that defines a module’s properties. The context often leaves only one option, so it’s usually clear which form is meant.

In addition to the `module` keyword, a declaration starts by giving the module a name. This has to be an identifier, meaning it must adhere to the same rules as, for example, a package name. Module names are usually lowercase and hierarchically structured with dots. It’s important that the name is

- Globally unique
- Stable

### Dependencies

Dependencies are declared with requires directives, which consist of the keyword followed by a module name. The directive states that the declared module depends on the named one and requires it during compilation and at run time.

### Exported packages

The keyword exports is followed by the name of a package the module contains. Only exported packages are usable outside the module; all others are strongly encapsulated within it.

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

### Readability edge

When a module *customer* requires a module *bar* in its declaration, then at run time *customer* will *read bar* or, conversely, *bar* will be readable by *customer*. The connection between the two modules is called a *readability edge*, or *reads edge* for short.

![reads-edge](https://user-images.githubusercontent.com/15990580/111142655-957fdd80-858d-11eb-9bf9-f3f06cef13fa.png)

The module system checks whether the universe of observable modules contains all required dependencies, direct and transitive, and reports an error if something’s missing. There must be no ambiguity: no two artifacts can claim they’re the same module. This is particularly interesting in the case where two versions of the same module are present—because the module system has no concept of versions, it treats this as a duplicate module. Accordingly, it reports an error if it runs into this situation. There must be no static dependency cycles between modules. At run time, it’s possible and even necessary for modules to access each other, but these must not be compile dependencies. Packages should have a unique origin, so no two modules must contain types in the same package. If they do, this is called a split package, and the module system will refuse to compile or launch such configurations. This is particularly interesting in the context of migration because some existing libraries and frameworks split packages on purpose.

Launching an application with a missing transitive dependency won’t work. Even if the initial module doesn’t directly depend on it, some other module does, so it will be reported as missing.

A split package occurs when two modules contain types in the same package. Interestingly, the compiler shows an error only if the module under compilation can access the split package in the other module. That means the split package must be exported.

### Accessibility

A type `Drink` in a module *bar* is accessible to code in a module *customer* if all of the following conditions are fulfilled:

- `Drink` is public.
- `Drink` belongs to a package that bar exports.
- *customer* reads *bar*.

### Public API

In nontechnical terms, a module’s public API is everything that can’t be changed without causing compile errors in code that uses it. More technically speaking, a module’s public API consists of the following:

- Names of all public types in exported packages
- Names and type names of public and protected fields
- Names, argument type names, and return type names of all public and protected methods (called method signatures)

### Module path

The module path is a list whose elements are artifacts or directories that contain artifacts. Depending on the OS, module path elements are separated by `:` (Unix-based) or `;` (Windows). It’s used by the module system to locate required modules that aren’t found among the platform modules. Both javac and java as well as other module-related commands can process it — the command-line options are `--module-path` and `-p`.

Only the module path processes artifacts as modules. All platform modules in the current runtime as well as all application modules specified on the command line are called observable, and together they make up the universe of observable modules.

### Annotation processors

Java 9 suggests to separate by concerns and use `--class-path` or `--module-path` for application JARs and `--processor-path` or `--processor-module-path` for processor JARs. For unmodularized JARs, the distinction between the application and processor paths is optional: placing everything on the class path is valid, but for modules it’s binding; processors on the module path won’t be used.

### Module graph

In a module graph, modules (as nodes) are connected according to their dependencies (with directed edges). The edges are the basis for readability. The graph is constructed during module resolution and available at run time via the reflection API.

### `--add-modules`

The option `--add-modules ${modules}`, available on `javac` and `java`, takes a comma-separated list of module names and defines them as root modules beyond the initial module. (Root modules form the initial set of modules from which the module graph is built by resolving their dependencies). This enables users to add modules (and their dependencies) to the module graph that would otherwise not show up because the initial module doesn’t directly or indirectly depend on them.

The `--add-modules` option has three special values: `ALL-DEFAULT`, `ALL-SYSTEM`, and `ALL-MODULE-PATH`. The first two only work at run time and are used for some edge cases. The last one can be useful, though: with it, all modules on the module path become root modules, and hence all of them make it into the module graph.

### `--add-reads`

The compiler-time and run-time option `--add-reads`*`${module}=${targets}`* adds reads edges from *`${module}`* to all modules in the comma-separated list *`${targets}`*. This allows *`${module}`* to access all public types in packages exported by those modules even though it has no requires directives mentioning them. If *`${targets}`* includes `ALL-UNNAMED`, *`${module}`* can read the class-path content.

## Summary

- Modules come in two forms:
  * The ones shipped with the Java runtime are *platform modules*. They’re merged into a `modules` file in the runtime’s `libs` directory. A JDK also holds them in raw form as JMOD files in the `jmods` directory. Only `java.base`, the base module, is explicitly known to the module system.
  * Library, framework, and application developers create *modular* JARs, which are plain JARs containing a *module descriptor* `module-info.class`. These are called *application modules*, with the one containing the `main` method being the *initial module*.
- The module descriptor is compiled from a *module declaration* `module-info.java` that developers (and tools) can edit. It defines a module’s properties:
  * Its name, which should be globally unique due to the reverse-domain naming scheme
  * Its dependencies, which are stated with `requires` directives that refer to other modules by name
  * Its API, which is defined by exporting selected packages with `exports` directives
- Dependency declarations and the *readability* edges the module system is creating from them are the basis for *reliable configuration*. It’s achieved by making sure, among other things, that all modules are present exactly once and no dependency cycles exist between them. This allows you to catch application-corrupting or crashing problems earlier.
- Readability edges and package exports together are the basis for *strong encapsulation*. Here the module system ensures that only public types in exported packages are accessible and only to modules that read the exporting one. This prevents accidental dependencies on transitive dependencies and enables you to make sure outside code can’t easily depend on types you designed as being internal to a module.
- Accessibility limitations apply to reflection as well.
- The *application modules*, specified on the module path, and the *platform modules*, contained in the runtime, make up the *universe of observable modules*.
- Module resolution verifies that the configuration is reliable (all dependencies present, no ambiguities, and so on) and results in the module graph — a close representation within the module system of how we all see artifact dependencies. Only modules that make it into the module graph are available at run time.
