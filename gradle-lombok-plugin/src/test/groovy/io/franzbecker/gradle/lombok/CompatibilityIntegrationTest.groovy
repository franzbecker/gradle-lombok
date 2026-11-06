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
        createSimpleTestCase(testConfigurationName)

        when: "calling gradle test"
        runBuild('test')

        then: "build is successful and both class file exist"
        noExceptionThrown()
        new File(projectDir, "$classesPath/main/com/example/HelloWorld.class").exists()
        new File(projectDir, "$classesPath/test/com/example/HelloWorldTest.class").exists()

        where:
        gradleVersion || classesPath          || testConfigurationName
        '2.12'        || 'build/classes'      || 'testCompile'
        '2.14.1'      || 'build/classes'      || 'testCompile'
        '3.5'         || 'build/classes'      || 'testCompile'
        '4.2.1'       || 'build/classes/java' || 'testCompile'
        '4.7'         || 'build/classes/java' || 'testCompile'
        '5.4'         || 'build/classes/java' || 'testCompile'
        '6.4'         || 'build/classes/java' || 'testCompile'
        '6.4'         || 'build/classes/java' || 'testImplementation'
        '7.0'         || 'build/classes/java' || 'testImplementation'
    }

    def "Gradle #gradleVersion - can run verifyLombok"() {
        given:
        theGradleVersion = gradleVersion
        createSimpleTestCase(testConfigurationName)

        when: "calling gradle verifyLombok"
        runBuild('verifyLombok')

        then: "no exception is thrown"
        noExceptionThrown()

        where:
        gradleVersion || classesPath          || testConfigurationName
        '2.12'        || 'build/classes'      || 'testCompile'
        '2.14.1'      || 'build/classes'      || 'testCompile'
        '3.5'         || 'build/classes'      || 'testCompile'
        '4.2.1'       || 'build/classes/java' || 'testCompile'
        '4.7'         || 'build/classes/java' || 'testCompile'
        '5.4'         || 'build/classes/java' || 'testCompile'
        '6.4'         || 'build/classes/java' || 'testCompile'
        '7.0'         || 'build/classes/java' || 'testImplementation'
    }

    def "Gradle #gradleVersion - can compile and test @Data annotation in alternate source set"() {
        given:
        theGradleVersion = gradleVersion
        String classDirPostfix = Integer.parseInt(gradleVersion[0]) < 4 ? "Dir" : "Dirs"
        buildFile << """
sourceSets {
    functionalTest {}
}

dependencies {
    ${testConfigurationName} 'junit:junit:4.12'
}

tasks.create('functionalTest', Test) {
    testClasses${classDirPostfix} = sourceSets.functionalTest.output.classes${classDirPostfix}
    classpath = sourceSets.functionalTest.runtimeClasspath
}
""".stripIndent()

        createJavaSource("src/functionalTest/java")
        createTestSource("src/functionalTest/java")

        when: "calling gradle test"
        runBuild('functionalTest')

        then: "build is successful and both class file exist"
        noExceptionThrown()
        new File(projectDir, "$classesPath/functionalTest/com/example/HelloWorld.class").exists()
        new File(projectDir, "$classesPath/functionalTest/com/example/HelloWorldTest.class").exists()

        where:
        gradleVersion || classesPath          || testConfigurationName
        '2.12'        || 'build/classes'      || 'functionalTestCompile'
        '2.14.1'      || 'build/classes'      || 'functionalTestCompile'
        '3.5'         || 'build/classes'      || 'functionalTestCompile'
        '4.2.1'       || 'build/classes/java' || 'functionalTestCompile'
        '4.7'         || 'build/classes/java' || 'functionalTestCompile'
        '5.4'         || 'build/classes/java' || 'functionalTestCompile'
        '6.4'         || 'build/classes/java' || 'functionalTestCompile'
        '6.4'         || 'build/classes/java' || 'functionalTestImplementation'
        '7.0'         || 'build/classes/java' || 'functionalTestImplementation'
    }

}
