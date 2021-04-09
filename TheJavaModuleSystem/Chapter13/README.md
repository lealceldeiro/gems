# Chapter 13: Module versions: What’s possible and what’s not

The `javac` and `jar` commands let us record a module’s versions with the `--module-version ${version}` option. It embeds the given version in the module declaration, where it can be read with command-like tools (for example, `jar --describe-module`) and the reflection API (`ModuleDescriptor::rawVersion`). Stack traces also show module versions.

If a module knows its own version and another module is compiled against it, the compiler will record the version in the second module’s descriptor. This information is only available on the `Requires` instances returned by `ModuleDescriptor::requires`.

The module system doesn’t act on version information in any way. Instead of trying to select a specific version for a module if the module path contains several, it quits with an error message. This keeps the expensive version-selection algorithm out of the JVM and the Java standard.

The module system has no out-of-the-box support for running multiple versions of the same module. The underlying reason is the class-loading mechanism, which assumes that each class loader knows at most one class for any given name. If running multiple versions is needed, then more than one class loader is  needed to accompplish it.

OSGi does exactly that by creating a single class loader for every JAR. Creating a similarly general solution is a challenging task, but a simpler variant, customized to an exact problem, is feasible. To run multiple versions of the same module, layers with associated class loaders must be created so that conflicting modules are separated.
