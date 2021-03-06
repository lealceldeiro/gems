# Chapter 14: Customizing runtime images with `jlink`

The command-line tool `jlink` creates runtime images from selected platform modules (`jdeps` have to be used to determine which ones an application needs). To benefit from that, the application needs to run on Java 9+, but it doesn’t have to be modularized.

Once the application and its dependencies have been fully modularized (without the use of automatic modules), `jlink` can create application images with it, including the app’s modules.

All calls to `jlink` need to specify the following:

  - Where to find modules (including platform modules), with `--module-path`
  - The root modules for resolution, with `--add-modules`
  - The output directory for the resulting image, with `--output`

Some considerations about how `jlink` resolves modules:

  - Services aren’t bound by default.
  - Optional dependencies with `requires static` aren’t resolved.
  - Automatic modules aren’t allowed.
  - Required service providers or optional dependencies have to be added individually with `--add-modules` or all providers bind with `--bind-services`.
  - Developers should watch out for platform services that an application may implicitly depend on without realizing it. Some candidates are charsets (`jdk.charsets`), locales (`jdk.localedata`), the Zip file system (`jdk.zipfs`), and security providers (various modules).

The runtime image generated by `jlink`:

  - Is bound to the OS for which the platform modules chosen with `--module-path` were built
  - Has the same directory structure as the JDK and JRE
  - Fuses platform and application modules (collectively known as system modules) into `lib/modules`
  - Contains only the binaries (in `bin`) for which the required modules were included
  - To launch an application image, either option can be used: `bin/java --module ${initial-module}` (no module path required, because system modules are automatically resolved) or the launcher created with `--launcher ${name}=${module}/${main-class}`.

With application images, the module path can be used to add additional modules (particularly those providing services). Modules on the module path with the same name as system modules are ignored.

The security, performance, and stability implications of delivering application images must be carefully evaluated when developers aren’t able to readily replace those images with newer versions.

Various `jlink` options, which activate plugins, offer ways to reduce image size (for example, `--compress`, `--exclude-files`, `--exclude-resource`, `--include-locales`, and `--strip-debug`) or improve performance (mostly startup time; `--class-for-name`, `--generate-jli-classes`, and `--order-resources`).
