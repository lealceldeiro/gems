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


