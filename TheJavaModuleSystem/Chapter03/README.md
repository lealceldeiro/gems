# Defining modules and their properties

Modular JAR and module descriptor: A modular JAR is just a plain JAR, except for one small detail. Its root directory contains a module descriptor: a `module-info.class` file.

Module declaration: A module descriptor is compiled from a module declaration. By convention, this is a `module-info.java` file in the project’s root source folder. The declaration is the pivotal element of modules and thus the module system.

Declaration vs. description: The declaration is source code and the description is bytecode, but they’re just different forms of the same idea: something that defines a module’s properties. The context often leaves only one option, so it’s usually clear which form is meant.

In addition to the `module` keyword, a declaration starts by giving the module a name. This has to be an identifier, meaning it must adhere to the same rules as, for example, a package name. Module names are usually lowercase and hierarchically structured with dots. It’s important that the name is

- Globally unique
- Stable

Dependencies: Dependencies are declared with requires directives, which consist of the keyword followed by a module name. The directive states that the declared module depends on the named one and requires it during compilation and at run time.
