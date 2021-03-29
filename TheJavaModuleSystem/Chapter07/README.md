# Chapter 7: Recurring challenges when running on Java 9 or later

All public classes in `java.*` or `javax.*` packages are standardized. These packages are exported by `java.*` modules and are safe to depend on, so no changes are required.

Public classes in some `com.sun.*` packages are supported by Oracle. Such packages are exported by `jdk.*` modules, and depending on them limits the code base to specific JDK vendors.

A few select classes in `sun.*` packages are temporarily supported by Oracle until replacements are introduced in future Java versions. They’re exported by *jdk-unsupported*.

All other classes are unsupported and inaccessible. Using them is possible with command-line flags, but code that does so can break on JVMs with different minor versions or from different vendors; thus it’s generally inadvisable.

Some internal APIs have been removed, so there’s no way to continue using them even with command-line options.

Although strong encapsulation generally forbids access to internal APIs, an exception is made for code on the class path accessing JDK-internal APIs. This will ease migration considerably but also complicates the module system’s behavior:

  - During compilation, strong encapsulation is fully active and prevents access to JDK-internal APIs. If some APIs are required nevertheless, it’s possible to grant access with `--add-exports`.
  - At run time, static access to public classes in non-exported JDK packages is allowed by default on Java 9 to 11. This makes it more likely that existing applications will work out of the box, but that will change with future releases.
  - Reflective access to all JDK-internal APIs is permitted by default but will result in a warning either on first access to a package (default) or on each access (with `--illegal-access=warn`). The best way to analyze this is `--illegal-access=debug`, which includes a stack trace in each warning.
  - Stricter behavior for static and reflective access is possible with `--illegal-access=deny`, using `--add-exports` and `--add-opens` where necessary to access critically required packages. Working toward that target early on makes migration to future Java updates easier.

The module system forbids two modules (in the same layer) to contain the same package—exported or not. This isn’t checked for code on the class path, though, so an undiscovered package split between a platform module and class-path code is possible.

If a package is split between a module and the class path, the class-path portion is essentially invisible, leading to surprising compile-time and run-time errors. The best fix is to remove the split, but if that isn’t possible, the platform module in question can either be replaced with the splitting artifact with `--upgrade-module-path` (if it’s an upgradeable module) or patched with its content with `--patch-module`.
