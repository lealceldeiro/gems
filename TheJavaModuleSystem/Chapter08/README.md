# Chapter 8: Incremental modularization of existing projects

An incremental modularization will often use the class path and the module path.
  - Any JAR on the class path, plain or modular, ends up in the unnamed module
  - Any JAR on the module path ends up as a named module — either as an automatic module (for a plain JAR) or an explicit module (for a modular JAR).

This allows the user of a JAR (instead of its creator) to determine whether it becomes a named module.

The unnamed module is a compatibility feature that makes the module system work with the class path:

  - It captures class-path content, has no name, reads every other module, and exports and opens all packages.
  - Because it has no name, explicit modules can’t refer to it in their module declarations. One consequence is that they can’t read the unnamed module and can hence never use types that are defined on the class path.
  - If the unnamed module is the initial one, a specific set of rules is used to ensure that the right set of modules is resolved. By and large these are the non-JEE modules and their dependencies. This lets code from the class path read all Java SE APIs without further configuration, thus maximizing compatibility.

Automatic modules are a migration feature that allows modules to depend on plain JARs:

  - An automatic module is created for each JAR on the module path. Its name is defined by the `Automatic-Module-Name` header in the JAR’s manifest (if present) or derived from its filename otherwise. It reads every other module, including the unnamed one, and exports and opens all packages.
  - It’s a regular named module and as such can be referenced in module declarations, for example to require it. This allows projects that are being modularized to depend on others that haven’t been yet.
  - An automatic module’s dependency can be placed on the class path or the module path. Which path to use depends on circumstances, but placing modular dependencies on the module path and plain ones on the class path is a sensible default.
  - As soon as the first automatic module is resolved, so are all others. Furthermore, any module that reads one automatic module reads all of them due to implied readability. This should be taken into account when testing out dependencies on automatic modules.
