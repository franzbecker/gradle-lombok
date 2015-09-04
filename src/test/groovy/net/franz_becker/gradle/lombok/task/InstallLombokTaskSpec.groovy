package net.franz_becker.gradle.lombok.task
/**
 * Unit tests for {@link InstallLombokTask}.
 */
class InstallLombokTaskSpec extends AbstractJavaExecTaskSpec {

    def "installLombok calls JavaExec properly"() {
        given:
        InstallLombokTask task = project.tasks.getByName(InstallLombokTask.NAME)
        def execAction = mockJavaExecAction(task)

        when:
        task.exec()

        then:
        1 * execAction.setMain('lombok.launch.Main')
        1 * execAction.setIgnoreExitValue(true)
        1 * execAction.setClasspath(configuration)
        1 * execAction.execute()
    }

}
