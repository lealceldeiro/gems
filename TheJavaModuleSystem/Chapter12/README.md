# Chapter 12: Reflection in a modular world

Drawbacks of using `exports` directives for code that’s primarily supposed to be used reflectively:

  - Only allows access to public members, which often requires making implementation details public.
  - Allows other modules to compile code against those exposed classes and members.
  - Qualified exports may couple you to an implementation instead of a specification.
  - Marks the package as being part of a module’s public API.

### The opens directive

A package can be opened by adding a directive `opens ${package}` to the module declaration. At compile time, opened packages are strongly encapsulated: there’s no difference between them being opened or not opened. At run time, opened packages are fully accessible, including nonpublic classes, methods, and fields.

`opens` was designed specifically for the use case of reflection and behaves very differently from `exports`:

  - It allows access to all members, thus not impacting any decisions regarding visibility
  - It prevents compilation against code in opened packages and only allows access at run time.
  - It marks the package as being designed for use by a reflection-based framework.

### Qualifying opens

The `opens` directive can be qualified by following it up with `to ${modules}`, where `${modules}` is a comma-separated list of module names (no placeholders are allowed). To the modules named in an `opens to` directive, the package will be exactly as accessible as with a regular `opens` directive. To all other modules, the package will be as strongly encapsulated as if there were no opens at all.

### `--add-opens`

The option `--add-opens ${module}/${package}=${reflecting-module}` opens `${package}` of `${module}` to `${reflecting-module}`. Code in `${reflecting-module}` can hence access all types and members, public and nonpublic ones, in `${package}`, but other modules can’t.

When `${reading-module}` is set to `ALL-UNNAMED`, all code from the class path, or more precisely from the unnamed module, can access that package. When migrating to Java 9+, an application will always use that placeholder — only once the application code runs in modules then open packages can be limited to specific modules.

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

### `AccessibleObject::trySetAccessible`

To check accessibility without causing an exception to be thrown, the `AccessibleObject::trySetAccessible` method, added in Java 9, can be used. At its core, it does the same thing as `setAccessible(true)`: it tries to make the underlying member accessible, but uses its return value to indicate whether it worked. If accessibility was granted, it returns `true`; otherwise it returns `false`.

### Module and ModuleDescriptor types

Java 9 introduced the new type `java.lang.Module`, which represents a module at run time. A `Module` instance allows to do the following:

  - Analyze the module’s name, annotations, `exports`/`opens` directives, and service uses
  - Access resources the module contains
  - Modify the module by exporting and opening packages or adding reads edges and services uses (if the modifying code is in the same module)
  - Some of these pieces of information are only available on the equally new type `java.lang.module.ModuleDescriptor`, returned by `Module::getDescriptor`.

### Module layer

A module layer comprises a fully resolved graph of named modules as well as the class loader(s) used to load the modules’ classes. Each class loader has an unnamed module associated with it (accessible with `ClassLoader::getUnnamedModule`). A layer also references one or more parent layers—modules in a layer can read modules in the ancestor layers, but not the other way around.

### Boot layer

Indeed they do. When launching, the JVM creates an initial layer, the boot layer, which contains the application and platform modules that were resolved based on the command-line options.
