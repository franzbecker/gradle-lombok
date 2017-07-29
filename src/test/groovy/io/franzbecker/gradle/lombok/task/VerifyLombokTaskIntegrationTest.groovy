package io.franzbecker.gradle.lombok.task
import io.franzbecker.gradle.lombok.AbstractIntegrationTest
import org.gradle.api.GradleException

import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage

/**
 * Integration tests for {@link VerifyLombokTask}.
 */
class VerifyLombokTaskIntegrationTest extends AbstractIntegrationTest {

    def "Task succeeds if hash is valid"() {
        given: "a properly configured hash"
        buildFile << """
            lombok {
                version = "1.16.4"
                sha256 = "3ca225ce3917eac8bf4b7d2186845df4e70dcdede356dca8537b6d78a535c91e"
            }
        """.stripIndent()

        when: "invoking the task"
        runTasksSuccessfully(VerifyLombokTask.NAME)

        then: "task succeeded without exception"
        noExceptionThrown()
    }

    def "Task succeeds with the default configuration"() {
        when: "invoking the task"
        runTasksSuccessfully(VerifyLombokTask.NAME)

        then: "task succeeded without exception"
        noExceptionThrown()
    }

    def "Task succeeds with non-default Lombok version"() {
        given: "a properly configured hash"
        buildFile << """
            lombok {
                version = "1.16.6"
                sha256 = "e0a471be03e1e6b02bf019480cec7a3ac9801702bf7bf62f15d077ad4df8dd5d"
            }
        """.stripIndent()

        when: "invoking the task"
        runTasksSuccessfully(VerifyLombokTask.NAME)

        then: "task succeeded without exception"
        noExceptionThrown()
    }

    def "Task fails if hash is invalid"() {
        given: "a badly configured hash"
        buildFile << """
            lombok {
                version = "1.16.4"
                sha256 = "wrongHash"
            }
        """.stripIndent()

        when: "invoking the task"
        runTasks(VerifyLombokTask.NAME).rethrowFailure()

        then: "expect a failure"
        GradleException e = thrown()
        def message = getRootCauseMessage(e)
        message.contains("wrongHash")
    }

    def "Task succeeds if hash is null"() {
        given: "an unset hash"
        buildFile << """
            lombok {
                version = "1.16.4"
                sha256 = null
            }
        """.stripIndent()

        when: "invoking the task"
        runTasks(VerifyLombokTask.NAME).rethrowFailure()

        then: "task succeeded without exception"
        noExceptionThrown()
    }

    def "Task succeeds if hash is empty"() {
        given: "an unset hash"
        buildFile << """
            lombok {
                version = "1.16.4"
                sha256 = ""
            }
        """.stripIndent()

        when: "invoking the task"
        runTasks(VerifyLombokTask.NAME).rethrowFailure()

        then: "task succeeded without exception"
        noExceptionThrown()
    }

    def "Task is up-to-date when hash is empty"() {
        given: "an unset hash"
        buildFile << """
            lombok {
                version = "1.16.4"
                sha256 = ""
            }
        """.stripIndent()

        when: "invoking the task two times"
        // there is no build history on the first run, so it is never up-to-date
        runTasks(VerifyLombokTask.NAME).rethrowFailure()
        def result = runTasks(VerifyLombokTask.NAME).rethrowFailure()

        then: "second run was up-to-date"
        result.wasUpToDate(VerifyLombokTask.NAME)
    }

    def "Task is never up-to-date when hash is set"() {
        given: "a properly configured hash"
        buildFile << """
            lombok {
                version = "1.16.4"
                sha256 = "3ca225ce3917eac8bf4b7d2186845df4e70dcdede356dca8537b6d78a535c91e"
            }
        """.stripIndent()

        when: "invoking the task multiple times"
        def result1 = runTasksSuccessfully(VerifyLombokTask.NAME)
        def result2 = runTasksSuccessfully(VerifyLombokTask.NAME)

        then: "both runs were not up-to-date"
        !result1.wasUpToDate(VerifyLombokTask.NAME)
        !result2.wasUpToDate(VerifyLombokTask.NAME)
    }

}
