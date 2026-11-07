package io.franzbecker.gradle.lombok.task

import io.franzbecker.gradle.lombok.LombokPluginExtension
import org.gradle.api.plugins.JavaPlugin

/**
 * Unit tests for {@link DelombokTask}.
 */
class DelombokTaskSpec extends AbstractJavaExecTaskSpec {

    def "delombok is configured to call Lombok main"() {
        given:
        def mainJar = project.file('main.jar')
        project.dependencies.add(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME, project.files(mainJar))

        when: "a newly created DelombokTask"
        DelombokTask task = project.task(type: DelombokTask, "delombok")

        then:
        task.mainClass.get() == 'lombok.launch.Main'
        task.classpath.files == [mainJar] as Set
    }

    def "delombok picks up main configured after task creation"() {
        given:
        DelombokTask task = project.task(type: DelombokTask, "delombok")

        when:
        project.extensions.getByType(LombokPluginExtension).main = 'custom.Main'

        then:
        task.mainClass.get() == 'custom.Main'
    }

    def "delombok uses configured compileConfigurationName"() {
        given:
        def mainJar = project.file('main.jar')
        def testJar = project.file('test.jar')
        project.dependencies.add(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME, project.files(mainJar))
        project.dependencies.add(JavaPlugin.TEST_COMPILE_ONLY_CONFIGURATION_NAME, project.files(testJar))
        DelombokTask task = project.task(type: DelombokTask, "delombok")

        when:
        task.compileConfigurationName = JavaPlugin.TEST_COMPILE_CLASSPATH_CONFIGURATION_NAME

        then:
        task.classpath.files == [testJar] as Set
    }

    def "delombok argument is set"() {
        when:
        def task = project.task(type: DelombokTask, "delombok")

        then:
        "delombok" in task.args
    }

}
