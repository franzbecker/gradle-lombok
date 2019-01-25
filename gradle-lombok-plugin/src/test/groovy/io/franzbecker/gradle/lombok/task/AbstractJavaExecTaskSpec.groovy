package io.franzbecker.gradle.lombok.task

import io.franzbecker.gradle.lombok.LombokPlugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.JavaExec
import org.gradle.process.internal.JavaExecAction
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * Base class for unit testing tasks of type JavaExec.
 */
abstract class AbstractJavaExecTaskSpec extends Specification {

    Project project
    Configuration configuration

    void setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'java'
        project.apply plugin: LombokPlugin.NAME
        configuration = project.configurations.getByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)
    }

    /**
     * Sets the private field "javaExecHandleBuilder" of the passed task
     * to a mock and returns that mock.
     *
     * @param task the task
     * @return the mock
     */
    JavaExecAction mockJavaExecAction(JavaExec task) {
        JavaExecAction execAction = Mock()
        def field = JavaExec.getDeclaredField("javaExecHandleBuilder")
        field.accessible = true
        field.set(task, execAction)
        return execAction
    }

}
