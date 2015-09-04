package net.franz_becker.gradle.lombok.task

/**
 * Unit tests for {@link DelombokTask}.
 */
class DelombokTaskSpec extends AbstractJavaExecTaskSpec {

    def "delomok calls JavaExec"() {
        given: "a newly created DelombokTask"
        def task = project.task(type: DelombokTask, "delombok")
        def execAction = mockJavaExecAction(task)

        when: "task executes"
        task.exec()

        then: "Delombok main is called"
        1 * execAction.setMain('lombok.launch.Main')
        1 * execAction.setClasspath(configuration)
        1 * execAction.execute()
    }

    def "delombok argument is set"() {
        when:
        def task = project.task(type: DelombokTask, "delombok")

        then:
        "delombok" in task.args
    }

}
