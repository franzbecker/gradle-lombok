package io.franzbecker.gradle.lombok.task

import io.franzbecker.gradle.lombok.AbstractIntegrationTest
import org.gradle.testkit.runner.TaskOutcome

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
        runBuild(VerifyLombokTask.NAME)

        then: "task succeeded without exception"
        noExceptionThrown()
    }

    def "Task succeeds with the default configuration"() {
        when: "invoking the task"
        runBuild(VerifyLombokTask.NAME)

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
        runBuild(VerifyLombokTask.NAME)

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
        def result = runTaskAndFail(VerifyLombokTask.NAME)

        then: "expect a failure"
        result.output.contains("Expected checksum: wrongHash")
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
        runBuild(VerifyLombokTask.NAME)

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
        runBuild(VerifyLombokTask.NAME)

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
        runBuild(VerifyLombokTask.NAME)
        def result = runBuild(VerifyLombokTask.NAME)

        then: "second run was up-to-date"
        result.tasks(TaskOutcome.UP_TO_DATE).first().path == ":$VerifyLombokTask.NAME"
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
        def result1 = runBuild(VerifyLombokTask.NAME)
        def result2 = runBuild(VerifyLombokTask.NAME)

        then: "both runs were not up-to-date"
        result1.tasks(TaskOutcome.UP_TO_DATE).empty
        result2.tasks(TaskOutcome.UP_TO_DATE).empty
    }

}
