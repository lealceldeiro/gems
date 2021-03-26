# Chapter 5: Running and debugging modular applications

## 5.1	Launching the JVM with Modules

The `java` command has an option `--module ${module}` that specifies the initial module *`${module}`*. Module resolution starts from there, and it’s also the module from which a main class will be launched. Example:

```shell
java
    --module-path mods:libs
    --module monitor
```

The specific class is either defined by the initial module’s descriptor or specified with `--module ${module}/${class}` by appending the module name with a slash and the fully qualified class name. Specifying the main class on the command line overrides whatever the module descriptor defines. For this to work, the initial module must contain the specified class. Example:

```shell
java
    --module-path mods:libs
    --module monitor/monitor.Main
```
### 5.1.3 Passing Parameters to the Application

The JVM puts everything after the initial module into an array of strings (split on space) and passes it to the main method. `--module` should be the last option  process by the JVM all application options shoud be placed behind it.

## 5.2 Loading Resources from Modules

### 5.2.2 Resource Loading on Java 9 and Later

- The methods on `Class`
  * Within the same module all resources are found. This is true regardless of which packages the module encapsulates.
  * Across module boundaries:
    + Resources from a package are by default encapsulated.
    + Resources from the JAR’s root or from folders whose names can’t be mapped to packages (like META-INF because of the dash) are never encapsulated.
    + `.class` files are never encapsulated.
    + If resources are encapsulated, the `getResource` call returns `null`.
    + The `opens` directive gives reflective access to a package.
- The methods on `ClassLoader` (they have a different and generally less-useful behavior when it comes to modules)
- A new class, `java.lang.Module`, also has methods `getResource` and `getResourceAsStream`.

Opening packages to give access to resources invites other code to depend on the module’s internal structure. To avoid that, exposing a type in the public API that can be tasked with loading resources should be considered. Then resource can be rearranged internally as it fits the requirements without breaking other modules.

## 5.3	Debugging Modules and Modular Applications

### 5.3.1 Analyzing Individual Modules

Whereas `jmod describe` and `jar --describe-module` operate on artifacts, `java --describe` operates on modules.

### 5.3.2 Validating Sets of Modules

The `java` option `--validate-modules` scans the module path for errors. It reports *duplicate modules* and *split packages* but builds no module graph, so it can’t discover missing modules or dependency cycles. After executing the checks, `java` exits.

With the `--dry-run` option, the JVM executes the full module resolution, including building a module graph and asserting a reliable configuration, but then stops right before executing the main method.

### 5.3.4 Listing Observable Modules and Dependencies

The option `--list-modules` lists the universe of observable modules. The module system does nothing else and neither resolves modules nor launches the application.

The option `--limit-modules ${modules}` accepts a list of comma-separated module names. It limits the universe of observable modules to the specified ones and their transitive dependencies.

This is how the module system evaluates the option:

- Starting from the modules specified to `--limit-modules`, the JPMS determines all their transitive dependencies.
- If `--add-modules` or `--module` was used, the JPMS adds the specified modules (but not their dependencies).
- The JPMS uses the resulting set as the universe of observable modules for any further steps (like listing modules or launching the application).

### 5.3.6 Observing the Module System with Log Messages

The module system logs messages into two different mechanisms:

#### Diagnostic messages from the resolver

With the option `--show-module-resolution`, the module system prints messages during module resolution.

#### Unified JVM logging ([Online docs](https://docs.oracle.com/javase/9/tools/java.htm#JSWOR-GUID-BE93ABDC-999C-4CB5-A88B-1994AAAC74D5))

Java 9 introduced a unified logging architecture. It pipes the messages the JVM generates through a single mechanism and allows you to select which messages to show with the intricate command-line option `-Xlog`. Note that this includes neither JDK messages, such as the ones Swing logs, nor the custom application’s messages itself — this is purely about the JVM itself.

**What is unified logging?**

The JVM-internal, unified logging infrastructure is similar to other logging frameworks like Java Util Logging, Log4j, and Logback. It generates textual messages, attaches some metainformation like tags (describing the originating subsystem), a log level (describing the importance of the message), and time stamps, and prints them somewhere.

**Defining which messages should be shown**

The log level and tags can be used to define exactly what the logs should show by defining pairs of `<tag-set>=<level>`, which are called selectors. All tags can be selected with `all`, and the level is optional and defaults to `info`. Example:

```shell
java -Xlog:all=warning -version
```

**Defining where messages should go**

The output configuration needs to be put after the selectors (separated by a colon), and it has three possible values:

- `stdout`: The default output. On the console, that’s the terminal window, unless redirected. In IDEs, it’s often shown in its own tab or view.
- `stderr`: The default error output. On the console, that’s the terminal window, unless redirected. In IDEs it’s usually shown in the same tab/view as stdout, but printed in red.
- `file=<filename>`: Defines a file to pipe all messages into. Including file= is optional.

Example: `java -Xlog:all=debug:file=application.log -version`

**Defining what messages should say**

Each message consists of text and metainformation. Which of these additional pieces of information the JVM will print is configurable by selecting *decorators*. Example:

`java -Xlog:gc*=debug:stdout:time,uptimemillis,tid -version` will print something like:

`[2021-03-26T17:56:59.888+0200][42ms][771] G1 Service Thread (Periodic GC Task) (run)`

**Configuring the entire logging pipeline**

Formally, the `-Xlog` option has this syntax: `-Xlog:<selectors>:<output>:<decorators>:<output-options>`

**Using Unified Logging to Look into the JPMS**

Example:

```shell
java
    -Xlog:module*
    --module-path mods:libs
    --dry-run
    --module monitor
```

The previous will show something like:

```shell
...
[0.046s][info][module,load] java.base location: jrt:/java.base
[0.129s][info][module,load] jetty.http location: file://${base-dir}/libs/jetty-http-9.4.6.v20170531.jar
[0.129s][info][module,load] java.xml location: jrt:/java.xml
[0.129s][info][module,load] websocket.common location: file://${base-dir}/libs/websocket-common-9.4.6.v20170531.jar
...
# and many more entries
```

## 5.4 Java Virtual Machine options
