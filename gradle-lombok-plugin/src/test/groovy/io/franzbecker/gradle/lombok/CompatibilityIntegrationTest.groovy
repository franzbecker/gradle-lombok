package io.franzbecker.gradle.lombok

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Unroll

@Unroll
class CompatibilityIntegrationTest extends AbstractIntegrationTest {

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
        new File(projectDir, "$classesPath/main/com/example/HelloWorld.class").exists()
        new File(projectDir, "$classesPath/test/com/example/HelloWorldTest.class").exists()

        where:
        gradleVersion || classesPath
        '2.12'        || 'build/classes'
        '2.14.1'      || 'build/classes'
        '3.5'         || 'build/classes'
        '4.2.1'       || 'build/classes/java'
        '4.7'         || 'build/classes/java'
        '5.4'         || 'build/classes/java'
    }

}
