package io.franzbecker.gradle.lombok

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Unroll

@Unroll
class CompatibilityIntegrationTest extends AbstractIntegrationTest {

    String theGradleVersion

    @Override
    protected GradleRunner createGradleRunner() {
        def runner = super.createGradleRunner()
        return runner.withGradleVersion(theGradleVersion)
    }

    def "Gradle #gradleVersion - can compile and test @Data annotation"() {
        given: "a specific Gradle version"
        theGradleVersion = gradleVersion

        and: "a build configuration supporting JUnit"
        buildFile << """
            dependencies {
                testCompile 'junit:junit:4.12'
            }
        """.stripIndent()

        and: "source code to compile and test"
        createJavaSource()
        createTestSource()

        when: "calling gradle test"
        runBuild('test')

        then: "build is successful and both class file exist"
        noExceptionThrown()
        new File(projectDir, "$classesPath/main/com/example/HelloWorld.class").exists()
        new File(projectDir, "$classesPath/test/com/example/HelloWorldTest.class").exists()

        where:
        gradleVersion || classesPath
        '2.11'        || 'build/classes'
        '2.12'        || 'build/classes'
        '2.14.1'      || 'build/classes'
        '3.5'         || 'build/classes'
        '4.2.1'       || 'build/classes/java'
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
