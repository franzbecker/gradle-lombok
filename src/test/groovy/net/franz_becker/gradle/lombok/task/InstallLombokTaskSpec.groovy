package net.franz_becker.gradle.lombok.task

import net.franz_becker.gradle.lombok.LombokPlugin
import net.franz_becker.gradle.lombok.task.InstallLombokTask
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.JavaExec
import org.gradle.process.internal.JavaExecAction
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * Unit tests for {@link net.franz_becker.gradle.lombok.task.InstallLombokTask}.
 */
class InstallLombokTaskSpec extends Specification {

    Configuration configuration
    JavaExecAction execAction = Mock()
    InstallLombokTask task

    /**
     * Perform a minimal project setup that lets us retrieve the task.
     */
    void setup() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'java'
        project.apply plugin: LombokPlugin.NAME
        task = project.tasks.getByName(InstallLombokTask.NAME)
        configuration = project.configurations.getByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)

        // Mock private field javaExecHandleBuilder
        def field = JavaExec.getDeclaredField("javaExecHandleBuilder")
        field.accessible = true
        field.set(task, execAction)
    }

    def "installLombok calls JavaExec properly"() {
        when:
        task.exec()

        then:
        1 * execAction.setMain('lombok.launch.Main')
        1 * execAction.setIgnoreExitValue(true)
        1 * execAction.setClasspath(configuration)
        1 * execAction.execute()
    }

}
