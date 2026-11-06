package io.franzbecker.gradle.lombok

import org.gradle.testkit.runner.GradleRunner

/**
 * Runs the plugin against the supported range of Gradle versions,
 * from {@link LombokPlugin#MINIMUM_GRADLE_VERSION} up to the latest release.
 */
class CompatibilityIntegrationTest extends AbstractIntegrationTest {

    static final List<String> GRADLE_VERSIONS = ['7.3.3', '7.6.6', '8.14.6', '9.8.2']

    String theGradleVersion

    @Override
    protected GradleRunner createGradleRunner() {
        def runner = super.createGradleRunner()
        return runner.withGradleVersion(theGradleVersion)
    }

    def "Gradle #gradleVersion - can compile and test @Data annotation"() {
        given:
        theGradleVersion = gradleVersion
        createSimpleTestCase()

        when: "calling gradle test"
        runBuild('test')

        then: "build is successful and both class file exist"
        noExceptionThrown()
        new File(projectDir, "build/classes/java/main/com/example/HelloWorld.class").exists()
        new File(projectDir, "build/classes/java/test/com/example/HelloWorldTest.class").exists()

        where:
        gradleVersion << GRADLE_VERSIONS
    }

    def "Gradle #gradleVersion - can run verifyLombok"() {
        given:
        theGradleVersion = gradleVersion
        createSimpleTestCase()

        when: "calling gradle verifyLombok"
        runBuild('verifyLombok')

        then: "no exception is thrown"
        noExceptionThrown()

        where:
        gradleVersion << GRADLE_VERSIONS
    }

}
