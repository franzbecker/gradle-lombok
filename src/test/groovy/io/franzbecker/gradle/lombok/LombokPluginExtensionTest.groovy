package io.franzbecker.gradle.lombok

import org.gradle.testkit.runner.InvalidPluginMetadataException

/**
 * Integration tests for {@link LombokPlugin}.
 */
class LombokPluginExtensionTest extends AbstractIntegrationTest {
    private static final LOMBOK_VERSION = "1.16.20"
    
    /**
     * Creates a build file with bad lombok coordinates and then check to see if
     * the lombok dependency can be applied successfully, which should fail given
     * the coordinates are incorrect.
     */
    def "Fails to find the lombok dependency if coordinates are invalid"() {
        given: "a valid build configuration"
        buildFile << """
            lombok {
                version = "${LOMBOK_VERSION}"
                coordindates = "org.projectlombok:lombok-custom"
            }
        """.stripIndent()
        
        when: "calling gradle test"
        runBuild('test')

        then: "build fails to find the lombok dependency with bad coordinates"
        thrown(InvalidPluginMetadataException.class)
    }
}
