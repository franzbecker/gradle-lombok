package io.franzbecker.gradle.lombok

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * Abstract base class for integration tests.
 */
abstract class AbstractIntegrationTest extends Specification {

    @Rule
    TemporaryFolder testProjectDir
    File projectDir
    File buildFile
    File propertiesFile

    def setup() {
        projectDir = testProjectDir.root
        buildFile = createFile('build.gradle')
        writeDefaultBuildFileContents()
        propertiesFile = createFile('gradle.properties')
        configureJacoco()
    }

    protected void writeDefaultBuildFileContents() {
        buildFile <<  """\
            plugins {
                id 'java'
                id '${LombokPlugin.NAME}'
            }
            repositories {
                jcenter()
            }
        """.stripIndent()
    }

    /** see https://github.com/koral--/jacoco-gradle-testkit-plugin */
    private void configureJacoco() {
        propertiesFile << this.class.classLoader.getResourceAsStream('testkit-gradle.properties')
    }

    protected GradleRunner createGradleRunner() {
        def runner = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDir)
        return runner
    }

    BuildResult runBuild(String... arguments) {
        def runner = createGradleRunner()
        return runner.withArguments(arguments).build()
    }

    BuildResult runBuildAndFail(String... arguments) {
        def runner = createGradleRunner()
        return runner.withArguments(arguments).buildAndFail()
    }

    BuildResult runTaskAndFail(String task) {
        def result = runBuildAndFail(task)
        def buildTask = result.getTasks().first()
        assert buildTask.path == ":$task"
        assert buildTask.outcome == TaskOutcome.FAILED
        return result
    }

    File createFile(String path) {
        File file = new File(projectDir, path)
        file.getParentFile().mkdirs()
        file.createNewFile()
        return file
    }

    protected void createSimpleTestCase() {
        createSimpleTestCase("testCompile")
    }

    protected void createSimpleTestCase(String testConfigurationName) {
        // build configuration supporting JUnit
        buildFile << """
            dependencies {
                ${testConfigurationName} 'junit:junit:4.12'
            }
        """.stripIndent()

        // source code to compile and test
        createJavaSource()
        createTestSource()
    }

    private void createJavaSource() {
        def file = createFile("src/main/java/com/example/HelloWorld.java")
        file << """\
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
        file << """\
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
