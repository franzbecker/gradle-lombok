package net.franz_becker.gradle.lombok

import nebula.test.IntegrationSpec
import org.gradle.api.GradleException

import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage

class EclipseInstallerTaskIntegrationTest extends IntegrationSpec {

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

    def "Task runs on valid SHA-256 hash"() {
        when:
        buildFile << """
            lombok {
                version = "1.16.4"
                sha256 = "3ca225ce3917eac8bf4b7d2186845df4e70dcdede356dca8537b6d78a535c91e"
            }
        """.stripIndent()
        runTasksSuccessfully(EclipseInstallerTask.NAME)

        then:
        noExceptionThrown()
    }

    def "Task fails on invalid SHA-256 hash"() {
        when:
        buildFile << """
            lombok {
                version = "1.16.4"
                sha256 = "wrongHash"
            }
        """.stripIndent()
        runTasks(EclipseInstallerTask.NAME).rethrowFailure()

        then:
        GradleException e = thrown()
        def message = getRootCauseMessage(e)
        message.contains("wrongHash")
    }

}
