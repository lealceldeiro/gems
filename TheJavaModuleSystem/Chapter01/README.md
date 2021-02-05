# Chapter 1: First piece of the puzzle

Each module (called a *part* too), has clear responsibilities and a well-defined contract it implements. It’s self-contained, it’s opaque to its clients, and it can be replaced by a different module as long as that one implements the same contract. Its few dependencies are APIs, not implementations.

This is what modularity is all about: achieving maintainability and flexibility as emergent properties of well-designed modules.

## 1.3	Complications before Java 9

**Unexpressed dependencies between jars**

**Shadowing classes with the same name**

Shadowing class: Because a class will be loaded from the first JAR on the class path that contains it, it makes all other classes of the same name unavailable—it’s said to shadow them.

**Conflicts between different versions of the same project**

Version conflicts arise when two required libraries depend on different, incompatible versions of a third library.

**Complex class loading**

**Weak encapsulation across jars**

**Security checks have to be handcrafted**

**Poor startup performance**

**Rigid java runtime**

Before Java 8, there was no way to install a subset of the JRE. All Java installations had support for, for example, XML, SQL, and Swing, which many use cases don’t require.
