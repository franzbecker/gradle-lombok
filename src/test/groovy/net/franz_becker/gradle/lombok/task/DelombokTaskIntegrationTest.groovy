package net.franz_becker.gradle.lombok.task

import net.franz_becker.gradle.lombok.AbstractIntegrationTest

/**
 * Integration tests for {@link DelombokTask}.
 */
class DelombokTaskIntegrationTest extends AbstractIntegrationTest {

    def "Delombok Task is called"() {
        given:
        buildFile << """
            import net.franz_becker.gradle.lombok.task.DelombokTask

            task delombok(type: DelombokTask) {
                args "--help"
            }

        """.stripIndent()

        when:
        runTasksSuccessfully("delombok")

        then:
        noExceptionThrown()
    }

}
