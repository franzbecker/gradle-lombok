package io.franzbecker.gradle.lombok

import nebula.test.IntegrationSpec

/**
 * Abstract base class for integration tests.
 */
abstract class AbstractIntegrationTest extends IntegrationSpec {

    def setup() {
        buildFile << """
            apply plugin: 'java'
            apply plugin: '${LombokPlugin.NAME}'

            repositories {
                jcenter()
            }
        """.stripIndent()
    }

}
