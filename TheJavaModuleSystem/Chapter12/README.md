# Chapter 12: Reflection in a modular world

Drawbacks of using `exports` directives for code that’s primarily supposed to be used reflectively:

  - Only allows access to public members, which often requires making implementation details public.
  - Allows other modules to compile code against those exposed classes and members.
  - Qualified exports may couple you to an implementation instead of a specification.
  - Marks the package as being part of a module’s public API.

`exports` directives aren’t a good fit for making classes available for reflection, because custom application classes designed to be used with reflection-based frameworks are rarely suited to be part of a module’s public API; with qualified `exports`, a coupling betwwen the custom appliation modules and an implementation is forced instead of a standard; and exports don’t support deep reflection over nonprivate fields and methods.

### The opens directive

A package can be opened by adding a directive `opens ${package}` to the module declaration. At compile time, opened packages are strongly encapsulated: there’s no difference between them being opened or not opened. At run time, opened packages are fully accessible, including nonpublic classes, methods, and fields.

`opens` was designed specifically for the use case of reflection and behaves very differently from `exports`:

  - It allows access to all members, thus not impacting any decisions regarding visibility
  - It prevents compilation against code in opened packages and only allows access at run time.
  - It marks the package as being designed for use by a reflection-based framework.

By default, `exports` shouldn’t be used, but rather `opens` directives, to open packages for reflection.

The `opens` directive has the same syntax as `exports`, but works differently: an opened package isn’t accessible at compile time; and all types and members, including nonpublic ones, in an opened package are accessible at run time. These properties are closely aligned with the requirements of reflection-based frameworks, which makes `opens` directives the default choice when preparing modules for reflection.

### Qualifying opens

The `opens` directive can be qualified by following it up with `to ${modules}`, where `${modules}` is a comma-separated list of module names (no placeholders are allowed). To the modules named in an `opens to` directive, the package will be exactly as accessible as with a regular `opens` directive. To all other modules, the package will be as strongly encapsulated as if there were no opens at all.

Because it’s usually exceedingly obvious which frameworks reflect over which packages, it’s questionable whether qualifying open directives adds much value.

### `--add-opens`

The option `--add-opens ${module}/${package}=${reflecting-module}` opens `${package}` of `${module}` to `${reflecting-module}`. Code in `${reflecting-module}` can hence access all types and members, public and nonpublic ones, in `${package}`, but other modules can’t.

When `${reading-module}` is set to `ALL-UNNAMED`, all code from the class path, or more precisely from the unnamed module, can access that package. When migrating to Java 9+, an application will always use that placeholder — only once the application code runs in modules then open packages can be limited to specific modules.

The command-line option `--add-opens` has the same syntax as `--add-exports` and works like a qualified opens. Opening platform modules from the command line to access their internals is common during a migration to Java 9+, but it can also be used to break into other application modules if there's an absolutely need for it.

### Open module

By putting the keyword `open` before `module` in the module declaration, an open module is created. Example:

```java
open module ${module-name} {
    requires ${module-name};
    exports ${package-name};
    // no opens allowed
}
```

An open module opens all packages it contains as if each of them were used in an `opens` directive. Consequently, it doesn’t make sense to manually open further packages, which is why the compiler doesn’t accept `opens` directives in an open module.

It should be carefully evaluated whether opening a whole module is really necessary or could be remedied. Ideally, open modules are mostly used during modularization before refactoring a module to a cleaner state that exposes less internals.

If the reflecting framework is split into a standard and its implementations (as with JPA and Hibernate, EclipseLink, and so forth), it’s technically possible to only open a package to the standard, which can then use the reflection API to open it to a specific implementation. This isn’t yet widely implemented, though, so for the time being, qualified opens need to name the specific implementation modules.

### `AccessibleObject::trySetAccessible`

To check accessibility without causing an exception to be thrown, the `AccessibleObject::trySetAccessible` method, added in Java 9, can be used. At its core, it does the same thing as `setAccessible(true)`: it tries to make the underlying member accessible, but uses its return value to indicate whether it worked. If accessibility was granted, it returns `true`; otherwise it returns `false`.

### About code that reflects over modules

Reflection is bound by the same accessibility rules as regular code. Regarding having to read the module that is being accessed, the reflection API makes things easier by implicitly adding a reads edge. Regarding exported or opened packages, there’s nothing the author of the reflecting code can do about it if a module owner didn’t prepare their module for it. (The only solution would be the `--add-opens` command-line option.)

Exceptions that are thrown due to strong encapsulation should be properly handled, so users are provided with an informative error message, possibly linking to the documentation.

Using variable handles should be considered instead of the reflection API. They provide more type safety, are more performant, and give the means to express the need for access in the bootstrap API by requiring `Lookup` instances.

A `Lookup` instance offers everybody using it the same accessibility as the module that created it. So when the users of a framework create a `Lookup` instance in their module and pass it to the framework, the framework code can access their module internals.

### `Module` and `ModuleDescriptor` types

Java 9 introduced the new type `java.lang.Module`, which represents a module at run time. `Module` and `ModuleDescriptor` are part of the reflection API and give access to all information regarding a module. It can be used to analyze the actual module graph at run time.

A `Module` instance allows to do the following:

  - Analyze the module’s name, dependencies, annotations, `exports`/`opens` directives, and service uses
  - Access resources the module contains
  - Modify the module by exporting and opening packages or adding reads edges and services uses (if the modifying code is in the same module)
  - Some of these pieces of information are only available on the equally new type `java.lang.module.ModuleDescriptor`, returned by `Module::getDescriptor`.

It’s generally not possible to modify other modules, with the exception that every module to which another module’s package was opened can open that package to a third module.

### Module layer

A module layer comprises a fully resolved graph of named modules as well as the class loader(s) used to load the modules’ classes. Each class loader has an unnamed module associated with it (accessible with `ClassLoader::getUnnamedModule`). A layer also references one or more parent layers—modules in a layer can read modules in the ancestor layers, but not the other way around.

### Boot layer

When launching, the JVM creates an initial layer, the boot layer, which consists of three class loaders and contains the application and platform modules that were initially resolved based on the command-line options. It can be accessed with the static method `ModuleLayer::boot`, and the returned `ModuleLayer` instance can be used to analyze the entire module graph.

### `ModuleLayer`

At run time, layers are represented by `java.lang.ModuleLayer` instances. They can be queried for the three things a layer is made up of:

  - The modules:
    - The method `modules()` returns the modules the layer contains as a `Set<Module>`.
    - The method `findModule(String)` searches the layer itself and all its ancestor layers for a module with the specified name. It returns an `Optional<Module>` because it may not find it.
    - The layer’s parents are returned as `List<ModuleLayer>` by the `parents()` method.
    - Each module’s class loader can be determined by calling `findLoader(String)` with a module’s name.

Example:

```java
Class<?> type = // ... any class
ModuleLayer layer = type
    .getModule()
    .getLayer();  // returns null if the type comes from an unnamed module or a dynamic module that doesn’t belong to a layer
```

Class loaders are the way to dynamically load code into a running program. This doesn’t change with the module system, but it does provide a modular wrapper around class loaders with layers. A layer encapsulates a class loader and a module graph, and creating the latter exposes the loaded modules to all the consistency checks and accessibility rules that the module system offers. Layers can hence be used to provide reliable configuration and strong encapsulation for the loaded modules.
