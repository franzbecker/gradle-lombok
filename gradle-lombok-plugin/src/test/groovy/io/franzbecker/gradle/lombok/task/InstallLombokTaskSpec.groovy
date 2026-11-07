package io.franzbecker.gradle.lombok.task

import io.franzbecker.gradle.lombok.LombokPlugin

/**
 * Unit tests for {@link InstallLombokTask}.
 */
class InstallLombokTaskSpec extends AbstractJavaExecTaskSpec {

    def "installLombok is configured to call Lombok main"() {
        given:
        def lombokJar = project.file('lombok.jar')
        project.dependencies.add(LombokPlugin.LOMBOK_CONFIGURATION_NAME, project.files(lombokJar))

        when:
        InstallLombokTask task = project.tasks.getByName(InstallLombokTask.NAME)

        then:
        task.mainClass.get() == 'lombok.launch.Main'
        task.ignoreExitValue
        task.classpath.files == [lombokJar] as Set
    }

}
