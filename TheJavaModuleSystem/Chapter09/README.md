# Chapter 9: Migration and modularization strategies

Before upgrading to Java 9, the code base should be running on Java 8; if not, that upgrade should be made first. If a preliminary analysis shows that some of the dependencies cause problems on Java 9+, they should be updated next. This ensures that one step at a time is taken, thus keeping complexity to a minimum.

There are several options to analyze migration problems:

  - Build on Java 9+, and apply quick fixes (`--add-modules`, `--add-exports`, `--add-opens`, `--patch-module`, and others) to get more information.
  - Use JDeps to find split packages and dependencies on internal APIs.
  - Search for specific patterns that cause problems, like casts to `URLClassLoader` and the use of removed JVM mechanisms.
  - After gathering this information, it’s important to properly evaluate it. What are the risks of the quick fixes? How hard is it to solve them properly? How important is the affected code for your project?

When a migration is started, an effort to continuously build the changes should be made, ideally from the same branch the rest of the team uses. This makes sure the Java 9+ efforts and regular development are well integrated.

Command-line options gives the ability to quickly fix the challenges faced when getting a build to work on Java 9+, but they should not be kept around too long. They make it easy to ignore problems until future Java releases exacerbate them. Instead, they should be replaced by a long-term solution.

Three modularization strategies exist. Which one applies to a project as a whole depends on its type and dependencies:

  - *Bottom-up* is for projects that only depend on modules. Here, module declarations can be created and all dependencies placed on the module path.
  - *Top-down* is for applications whose dependencies aren’t yet all modularized. They can create module declarations and place all direct dependencies on the module path — plain JARs are turned into automatic modules that can be depended on.
  - *Inside-out* is for libraries and frameworks whose dependencies aren’t yet all modularized. It works like top-down but has the limitation that only automatic modules that define an Automatic-Module-Name manifest entry can be used. Otherwise, the automatic module name is unstable across build setups and over time, which can lead to significant problems for users.

JDeps allows the automatic generation of module declarations with `jdeps --generate-module-info`. This is particularly relevant to large projects, where hand-writing module declarations would take a lot of time.

With the `jar` tool’s `--update` option, existing JARs can be modified: for example, to set `Automatic-Module-Name` or to add or overwrite a module descriptor. If a dependency’s JAR makes problems that aren’t otherwise fixable, this is the sharpest tool to resolve them.

By compiling and packaging source code for an older Java version and then adding the module descriptor (either in the JARs root directory or with `jar --version` to a Java 9+ specific subdirectory), modular JARs can be created that work on various Java versions and as a module if placed on a Java 9 module path.
