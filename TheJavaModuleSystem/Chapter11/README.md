# Chapter 11: Refining dependencies and APIs

Implied readability:

- With a `requires transitive` directive, a module makes its client read the thus-required module even though the module doesn’t explicitly depend on it. This allows the module to use types from dependencies in its API without putting the burden to manually require those dependencies on the client modules. As a consequence, the module becomes instantly usable.
- A module should only rely on a transitive dependency being implicitly readable if it only uses it on the boundary to the respective direct dependency. As soon as the module starts using the transitive dependency to implement its own functionality, it should make it a direct dependency. This ensures that the module declaration reflects the true set of dependencies and makes the module more robust for refactorings that may remove the transitive dependency.
- Implied readability can be used when moving code between modules by having the modules that used to contain the code imply readability on the ones that do now. This lets clients access the code they depend on without requiring them to change their module descriptors, because they still end up reading the module that contains the code. Keeping compatibility like this is particularly interesting for libraries and frameworks.

Optional dependencies:

- With a `requires static` directive, a module marks a dependency that the module system will ensure is present at compile time but can be absent at run time. This allows coding against modules without forcing clients to always have those modules in their application.
- At launch time, modules required *only* by `requires static` directives aren’t added to the module graph even if they’re observable. Instead, they have to be added manually with `--add-modules`.
- Coding against optional dependencies should involve making sure no execution path can fail due to the dependency missing, because this would severely undermine the module’s usability.

Qualified exports:

- With an exports to directive, a module makes a package accessible only to the named modules. This is a third and more targeted option between encapsulating a package and making it accessible for everybody.
- Exporting to specific modules allows sharing code within a set of privileged modules without making it a public API. This reduces the API surface of a library or framework, thus improving maintainability.
- With the `--add-exports` command-line option, you can export packages at compile and run time that the module’s developers intended as internal APIs. On the one hand, this keeps code running that depends on those internals; on the other hand, it introduces its own maintainability problems.
