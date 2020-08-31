# Chapter 15: Packages and Packaging

## 15.6 Running a Program from a JAR

### Problem

You want to distribute a single large file containing all the classes of your application and run the main program from within the JAR.

### Solution

Create a JAR file with a `Main-Class:` line in the manifest; run the program with the `java -jar` option.

Using Maven, the POM file would look like:

```xml
<project ...>
    ...
    <packaging>jar</packaging>
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>${maven-jar-plugin-version}</version>
                <configuration>
                    <archive>
                        <manifest>
                            <addclasspath>true</addclasspath>
                            <mainClass>${main.class}</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
          
            <plugin>
              <groupId>org.apache.maven.plugins</groupId>
              <artifactId>maven-assembly-plugin</artifactId>
              <version>${maven-assembly-plugin-version}</version>
              <configuration>
                  <descriptorRefs>
                      <descriptorRef>jar-with-dependencies</descriptorRef>
                  </descriptorRefs>
                  <archive>
                      <manifest>
                          <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
                          <mainClass>${main.class}</mainClass>
                          <!-- <manifestFile>manifest.txt</manifestFile> -->
                      </manifest>
                      <manifestEntries>
                          <Vendor-URL>http://YOURDOMAIN.com/SOME_PATH/</Vendor-URL>
                      </manifestEntries>
                  </archive>
              </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

From this POM file, using `mvn package assembly:single` will produce a runnable JAR with all dependencies.
