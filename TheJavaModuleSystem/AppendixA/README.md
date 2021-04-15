# Apendix A: Class-path

The class path is a concept related to the compiler and the virtual machine. They use it for the same purpose: to search the listed JARs for types that they require but that aren’t in the JDK.

Regarding Java 9 (and later versions), it’s important to stress that the class path isn’t going away! It operates exactly as it did in earlier Java versions, and if applications that compiled on such a version didn’t do anything problematic, they will continue to compile on Java 9 and beyond with the same commands.

