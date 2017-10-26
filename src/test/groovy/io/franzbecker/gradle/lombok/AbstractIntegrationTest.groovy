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

}
