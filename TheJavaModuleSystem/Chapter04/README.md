# Chapter 4: Building modules from source to JAR

## 4.1.2 Established Directory Structure

This structure has a top-level directory for each module. The modules can then organize their own files as best fits their needs.

![project-structure](https://user-images.githubusercontent.com/15990580/112185671-20ee1400-8c09-11eb-95d2-1f4f774a739e.jpg)

A modular JAR is nothing but a plain JAR with a module descriptor `module-info.class`, which is compiled from a module declaration `module-info.java`. For that reason, the compiler uses the presence or absence of `module-info.java` in the list of sources to compile that to discern whether it works on a module.

If code that includes a module declaration is compiled:

- It must require its dependencies to be able to access the types these dependencies export
- The required dependencies have to be present
