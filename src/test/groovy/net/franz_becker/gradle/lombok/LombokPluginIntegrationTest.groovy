package net.franz_becker.gradle.lombok

import nebula.test.IntegrationSpec

class LombokPluginIntegrationTest extends IntegrationSpec {

    def setup() {
        buildFile << """
            apply plugin: 'java'
            apply plugin: '${LombokPlugin.NAME}'

            repositories {
                jcenter()
            }
        """.stripIndent()
    }

    /**
     * Creates a Java class annotated with @Data and tests if the
     * getter and setter are created properly by Lombok.
     */
    def "Can compile and test @Data annotation."() {
        when:
        buildFile << """
            dependencies {
                testCompile 'junit:junit:4.12'
            }
        """.stripIndent()
        createJavaSource()
        createTestSource()
        runTasksSuccessfully('test')

        then:
        new File(projectDir, "build/classes/main/com/example/HelloWorld.class").exists()
        new File(projectDir, "build/classes/test/com/example/HelloWorldTest.class").exists()
    }

    /**
     * Verifies that the Lombok dependency does not occur in the generated POM.
     */
    def "Dependency does not occur in generated POM"() {
        when:
        buildFile << """
            apply plugin: 'maven'

            dependencies {
                testCompile 'junit:junit:4.12'
            }
        """.stripIndent()
        runTasksSuccessfully('install')

        then:
        // Check that the POM exists
        def pom = new File(projectDir, "build/poms/pom-default.xml")
        assert pom.exists()

        // Parse XML and retrieve <dependencies> object
        def pomXml = new XmlSlurper().parseText(pom.text)
        def dependencies = pomXml.dependencies
        assert dependencies.size() == 1

        // Retrieve junit and (potential) lombok dependency
        def junit = dependencies.dependency.find { it.artifactId.text() == 'junit' }
        def lombok = dependencies.dependency.find { it.artifactId.text() == 'lombok' }
        assert junit
        assert !lombok
    }

    private void createJavaSource() {
        def file = createFile("src/main/java/com/example/HelloWorld.java")
        file << """
            package com.example;

            import lombok.Data;

            @Data
            public class HelloWorld {

                private String id;

            }
        """.stripIndent()
    }

    private void createTestSource() {
        def file = createFile("src/test/java/com/example/HelloWorldTest.java")
        file << """
            package com.example;

            import java.util.UUID;
            import org.junit.Test;
            import org.junit.Assert;

            public class HelloWorldTest {

                @Test
                public void testHelloWorld() {
                    // Given
                    HelloWorld obj = new HelloWorld();
                    String id = UUID.randomUUID().toString();

                    // When
                    obj.setId(id);

                    // Then
                    Assert.assertEquals(id, obj.getId());
                }

            }
        """.stripIndent()
    }

}
