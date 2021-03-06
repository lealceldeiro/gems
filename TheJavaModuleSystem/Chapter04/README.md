# Chapter 4: Building modules from source to JAR

## 4.1.2 Established Directory Structure

This structure has a top-level directory for each module. The modules can then organize their own files as best fits their needs.

![project-structure](https://user-images.githubusercontent.com/15990580/112185671-20ee1400-8c09-11eb-95d2-1f4f774a739e.jpg)

A modular JAR is nothing but a plain JAR with a module descriptor `module-info.class`, which is compiled from a module declaration `module-info.java`. For that reason, the compiler uses the presence or absence of `module-info.java` in the list of sources to compile that to discern whether it works on a module.

If code that includes a module declaration is compiled:

- It must require its dependencies to be able to access the types these dependencies export
- The required dependencies have to be present

## 4.3.3	The Asterisk as a Token for the Module Name

The module source path can contain an asterisk (\*). Although it’s commonly interpreted as a wildcard, which in paths usually means “anything in the directory up to the asterisk,” this isn’t the case here. Instead, the asterisk functions as a token that indicates where on the path the module names appear. The rest of the path after the asterisk must point to the directory containing the modules’ packages.

## 4.3.4	Multiple Module Source Path Entries

The module source path lets alternative paths with `{dir1,dir2}` to be defined. Various paths (if they only differ in the name of single path elements) can be unified. Example:

```shell
--module-source-path "src/*/{share,unix}/classes"
```

### 4.3.5	Setting the Initial Module

Compiling a single module and its dependencies just by naming it no longer requires the source files to compile to be explicitly listed.

The option `--module` can be used to compile a single module and its transitive dependencies without explicitly listing the source files. Example:

```shell
javac
    --module-path mods:libs
    --module-source-path "./*/src/main/java"
    -d classes
    --module monitor # Because the initial module 'monitor' depends on all other modules, all of them are built
```

## 4.4	Compiler options

The `-source` and `-target` options are used to compile the code to run on an older version of Java. But if the option `-bootclasspath` is not specified the application can crash at runtime because a method call failed (for example). Without that option, the compiler creates bytecode that a JVM with the target version understands (good), but it links against the current version’s core library API (bad). That can create calls to types or methods that didn’t exist in the older JDK version and thus cause runtime errors.

From Java 9 on, the compiler prevents that common operating error with the `--release` option that sets all three options to the correct value.

JAR isn’t the only format used to deliver Java bytecode. JEE also works with WAR and EAR files. Until the specification is updated to embrace modules, though, it isn’t possible to create modular WARs or EARs.

### 4.5.3 Defining an Entry Point

When `jar` is used to package class files into an archive, a main class can be defined with `--main-class ${class}`, where `${class}` is the fully qualified name (meaning the package name appended with a dot and the class name) of the class with the *main* method. It will be recorded in the module descriptor and used by default as the main class when the module is the initial module for launching an application. `jar --main-class` also sets the manifest’s `Main-Class` entry.

## Summary

- The `javac` command to compile all of a module’s sources, including the declaration, is the same as before Java 9, except that it uses the module path instead of the class path.
- The module source path (`--module-source-path`) informs the compiler of how the project is structured. This lifts the compiler operation from processing types to processing modules, allowing you to compile a selected module and all its dependencies with a simple option (`--module` or `-m`) instead of listing source files.
- Modular JARs are just JARs with a module descriptor module-info.class. The jar tool processes them just as well as other class files, so packaging all of them into a JAR requires no new options.
- Optionally, jar allows the specification of a module’s entry point (with --main-class), which is the class with the main method. This makes launching the module simpler.
