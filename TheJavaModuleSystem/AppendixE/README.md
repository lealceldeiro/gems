# Appendix E: Targeting multiple Java versions with multi-release JARs

Multi-release JARs (MR-JARs) are specially prepared JARs that contain bytecode for several major Java versions. How that bytecode is loaded depends on the JVM version:

  - Java 8 and earlier load version-unspecific class files.
  - Java 9 and later load version-specific class files if they exist, or otherwise fall back to version-unspecific ones.

To prepare for an MR-JAR, you need to split source files by the Java version they target, compile each set of sources for the corresponding version, and place the resulting `.class` files into separate folders. When packaging them with `jar`, you add the baseline class files as usual (directly or with `-C`) and use the new option `--release ${release}` for each other bytecode set.

How does an MR-JAR work? It stores version-unspecific class files in its root and version-specific files in `META-INF/versions/${version}`. JVMs of version 8 and earlier don’t know anything about `META-INF/versions` and load the classes from the package structure in the JAR’s root. Consequently, it’s not possible to distinguish between versions before 9.

Newer JVMs, however, first look into `META-INF/versions` and, only if they don’t find a class there, into the JAR’s root. They search backward from their own version, meaning a Java 10 JVM looks for code in `META-INF/versions/10`, then `META-INF/versions/9`, and then the root directory. These JVMs thus shadow version-unspecific class files with the newest version-specific ones they support.

In addition to the folders in `META-INF/versions`, an MR-JAR can also be recognized by looking at the plaintext file `META-INF/MANIFEST.MF`: in MR-JARs, the manifest has an entry `Multi-Release: true`.

## Usage recommendations

### Source Code Organization

- The code for the oldest supported Java version goes in the project’s default root directory: for example, `src/main/java`, not `src/main/java-X`.
- The code in that source folder is complete, meaning it can be compiled, tested, and deployed as is without additional files from version-specific source trees like `src/main/java-X`. (Note that if there's a feature that only works on a newer Java version, having a class that only throws errors stating “Operation not supported before Java X” counts as complete. It should not be left out, leading to an uninformative `NoClassDefFoundError`.)

To determine whether a particular layout works, the following steps should be undertaken:

- Compile and test the version-independent source tree on the oldest supported Java version.
- For each additional source tree:
    * Move the version-dependent code into the version-independent tree, replacing files where they have the same fully qualified name.
    * Compile and test the tree on the newer version.

As an alternative solution, creating separate projects for each Java version can be considered.

### Bytecode Organization

- The bytecode for the oldest supported Java version goes into the JAR’s root, meaning it’s not added after `--release`.
- The bytecode in the JAR’s root is complete, meaning it can be executed as is without additional files from `META-INF/versions`.

