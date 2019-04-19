package io.franzbecker.gradle.lombok.task

import org.gradle.api.plugins.JavaPlugin

/**
 * Unit tests for {@link DelombokTask}.
 */
class DelombokTaskSpec extends AbstractJavaExecTaskSpec {

    def "delombok calls JavaExec"() {
        given: "a newly created DelombokTask"
        def task = project.task(type: DelombokTask, "delombok")
        def execAction = mockJavaExecAction(task)
        def expectedClasspath = project.configurations.getByName(JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME)

        when: "task executes"
        task.exec()

        then: "Delombok main is called"
        1 * execAction.setMain('lombok.launch.Main')
        1 * execAction.classpath(expectedClasspath)
        1 * execAction.execute()
    }

    def "delombok argument is set"() {
        when:
        def task = project.task(type: DelombokTask, "delombok")

        then:
        "delombok" in task.args
    }

}
