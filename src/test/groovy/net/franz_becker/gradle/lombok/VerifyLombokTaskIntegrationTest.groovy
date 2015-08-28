package net.franz_becker.gradle.lombok

import nebula.test.IntegrationSpec
import org.gradle.api.GradleException

import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage

/**
 * Integration tests for {@link VerifyLombokTask}.
 */
class VerifyLombokTaskIntegrationTest extends IntegrationSpec {

    def setup() {
        buildFile << """
            apply plugin: 'java'
            apply plugin: 'eclipse'
            apply plugin: '${LombokPlugin.NAME}'

            repositories {
                jcenter()
            }
        """.stripIndent()
    }

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

}
