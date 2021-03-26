# Chapter 5: Running and debugging modular applications

## 5.1	Launching the JVM with modules

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
