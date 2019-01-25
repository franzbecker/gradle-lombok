package io.franzbecker.gradle.lombok

import groovy.util.slurpersupport.GPathResult

/**
 * Integration tests for {@link LombokPlugin}.
 */
class LombokPluginIntegrationTest extends AbstractIntegrationTest {

    /**
     * Creates a Java class with a method annotated with @SneakyThrows and tests if the
     * annotation is applied properly.
     */
    def "Can compile and test code with @SneakyThrows annotation."() {
        given: "a valid build configuration"
        buildFile << """
            dependencies {
                testCompile 'junit:junit:4.12'
            }
        """.stripIndent()

        and: "source code with @SneakyThrows to compile and test"
        createSneakyThrowsJavaSource()
        createSneakyThrowsTestCode()

        when: "calling gradle test"
        runBuild('test')

        then: "build is successful and both class file exist"
        noExceptionThrown()
        new File(projectDir, "build/classes/java/main/com/example/SneakyHelloWorld.class").exists()
        new File(projectDir, "build/classes/java/test/com/example/SneakyHelloWorldTest.class").exists()
    }

    def "Can compile test code with Lombok depenceny"() {
        given:
        buildFile << """
            dependencies {
                testCompile 'junit:junit:4.12'
            }
        """.stripIndent()

        and:
        createTestCodeWithLombokDependency()

        when:
        runBuild('test')

        then:
        noExceptionThrown()
        new File(projectDir, "build/classes/java/test/com/example/SneakyHelloWorldTest.class").exists()
    }

    def "Works with the java-library plugin"() {
        given:
        buildFile.text = buildFile.text.replace("id 'java'", "id 'java-library'")
        createSimpleTestCase()

        when:
        runBuild('test')

        then:
        noExceptionThrown()
        new File(projectDir, "build/classes/java/main/com/example/HelloWorld.class").exists()
        new File(projectDir, "build/classes/java/test/com/example/HelloWorldTest.class").exists()
    }

    def "Dependency does not occur in generated POM using the 'maven' plugin"() {
        given: "a valid build configuration"
        buildFile << """
            apply plugin: 'maven'

            dependencies {
                testCompile 'junit:junit:4.12'
            }
        """.stripIndent()

        when: "calling gradle install"
        runBuild('install')

        then: "Parse POM and retrieve <dependencies> object"
        def parsedXml = findAndParsePom("build/poms/pom-default.xml")
        def dependencies = parsedXml.dependencies
        assert dependencies.size() == 1
        assert dependencies.dependency.find { it.artifactId.text() == 'junit' }
        assert !dependencies.dependency.find { it.artifactId.text() == 'lombok' }
    }

    def "Dependency does not occur in generated POM using the 'maven-publish' plugin"() {
        given:
        buildFile << '''
            apply plugin: 'maven-publish'
            
            dependencies {
                compile 'javax.inject:javax.inject:1'
            }
            
            publishing {
                publications {
                    mavenJava(MavenPublication) {
                        from components.java
                    }
                }   
            }
            
            model {
                tasks.generatePomFileForMavenJavaPublication {
                    destination = file("$buildDir/generated-pom.xml")
                }
            }
        '''.stripIndent()

        when:
        runBuild('generatePomFileForMavenJavaPublication')

        then:
        def parsedXml = findAndParsePom("build/generated-pom.xml")
        def dependencies = parsedXml.dependencies
        assert dependencies.size() == 1
        assert dependencies.dependency.find { it.artifactId.text() == 'javax.inject' }
        assert !dependencies.dependency.find { it.artifactId.text() == 'lombok' }
    }

    private GPathResult findAndParsePom(String path) {
        def pom = new File(projectDir, path)
        assert pom.exists()
        return new XmlSlurper().parseText(pom.text)
    }

    /**
     * Verifies that the Lombok dependency is on the Eclipse classpath.
     */
    def "Lombok dependency is on Eclipse classpath"() {
        given:
        buildFile << """
            apply plugin: 'eclipse'
        """.stripIndent()

        when:
        runBuild('eclipse')

        then: "verify that the .classpath file exists"
        def dotClasspath = new File(projectDir, ".classpath")
        dotClasspath.exists()

        and: "verify the lombok JAR is included"
        def classpath = new XmlSlurper().parse(dotClasspath)
        def lombokEntry = classpath.classpathentry.find { node ->
            node.@kind == "lib" && node.@path.text().contains("lombok-${LombokPluginExtension.DEFAULT_VERSION}.jar")
        }
        assert lombokEntry
    }

    /**
     * Verifies that the Lombok dependency is on the IntelliJ classpath.
     */
    def "Lombok dependency is on IntelliJ classpath"() {
        given:
        buildFile << """
            apply plugin: 'idea'
        """.stripIndent()

        when:
        runBuild('idea')

        then: "verify that the .iml file exists"
        File imlFile = getImlFile()

        and: "verify the lombok JAR is included"
        def module = new XmlSlurper().parse(imlFile)
        def provided = module.component.orderEntry.find { it.@type == "module-library" }
        assert provided
        def lombokEntry = provided.library.CLASSES.root.find {
            it.@url.text().contains("lombok-${LombokPluginExtension.DEFAULT_VERSION}.jar")
        }
        assert lombokEntry
    }

    private File getImlFile() {
        FilenameFilter filter = { dir, name -> name.endsWith(".iml") }
        File[] imlFiles = projectDir.listFiles(filter)
        assert imlFiles.size() == 1
        return imlFiles[0]
    }

    private void createSneakyThrowsJavaSource() {
        def file = createFile("src/main/java/com/example/SneakyHelloWorld.java")
        file << """
            package com.example;

            import lombok.SneakyThrows;
            import java.io.UnsupportedEncodingException; // checked exception

            public class SneakyHelloWorld {

                @SneakyThrows(UnsupportedEncodingException.class)
                public byte[] throwingStuff() {
                    return "test".getBytes("unsupported");
                }

            }
        """.stripIndent()
    }

    private void createSneakyThrowsTestCode() {
        def file = createFile("src/test/java/com/example/SneakyHelloWorldTest.java")
        file << """
            package com.example;

            import org.junit.Assert;
            import org.junit.Test;
            import java.io.UnsupportedEncodingException;

            public class SneakyHelloWorldTest {

                @Test(expected = UnsupportedEncodingException.class)
                public void testThrowingStuff() {
                    SneakyHelloWorld instance = new SneakyHelloWorld();
                    instance.throwingStuff();
                }

            }

        """.stripIndent()
    }

    private void createTestCodeWithLombokDependency() {
        def file = createFile("src/test/java/com/example/SneakyHelloWorldTest.java")
        file << '''
            package com.example;

            import org.junit.Assert;
            import org.junit.Test;
            import lombok.SneakyThrows;
            import java.io.UnsupportedEncodingException;

            public class SneakyHelloWorldTest {

                @Test(expected = UnsupportedEncodingException.class)
                @SneakyThrows
                public void testThrowingStuff() {
                    "test".getBytes("unsupported");
                }

            }
        '''.stripIndent()
    }

}
