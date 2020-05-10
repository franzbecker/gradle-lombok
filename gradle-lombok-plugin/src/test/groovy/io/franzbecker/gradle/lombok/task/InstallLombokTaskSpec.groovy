package io.franzbecker.gradle.lombok.task

import io.franzbecker.gradle.lombok.LombokPlugin

/**
 * Unit tests for {@link InstallLombokTask}.
 */
class InstallLombokTaskSpec extends AbstractJavaExecTaskSpec {

    def "installLombok calls JavaExec properly"() {
        given:
        InstallLombokTask task = project.tasks.getByName(InstallLombokTask.NAME)
        def execAction = mockJavaExecAction(task)
        def expectedClasspath = project.configurations.getByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)

        when:
        task.configure {}
        task.exec()

        then:
        1 * execAction.setMain('lombok.launch.Main')
        1 * execAction.setIgnoreExitValue(true)
        1 * execAction.setClasspath(expectedClasspath)
        1 * execAction.execute()
    }

}
