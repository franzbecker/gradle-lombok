package io.franzbecker.gradle.lombok

import io.franzbecker.gradle.lombok.task.InstallLombokTask
import io.franzbecker.gradle.lombok.task.VerifyLombokTask
import nebula.test.PluginProjectSpec
/**
 * Unit tests for {@link LombokPlugin}.
 */
class LombokPluginSpec extends PluginProjectSpec {

    @Override
    String getPluginName() {
        return LombokPlugin.NAME
    }

    private def applyJavaAndLombok() {
        project.apply plugin: "java"
        project.apply plugin: pluginName
    }

    def "Does not create new tasks if Java plugin is not applied"() {
        given:
        def beforeTaskMap = project.tasks.getAsMap().collect()

        when:
        project.apply plugin: pluginName

        then:
        def afterTaskMap = project.tasks.getAsMap().collect()
        assert afterTaskMap == beforeTaskMap
    }

    def "Does not add Lombok configuration if Java plugin is not applied"() {
        when:
        project.apply plugin: pluginName

        then:
        assert !project.configurations.findByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)
    }

    def "Add Lombok configuration if Java plugin is applied"() {
        when:
        applyJavaAndLombok()

        then:
        assert project.configurations.findByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)
    }

    def "Lombok dependency is added"() {
        when:
        applyJavaAndLombok()
        project.evaluate()

        then:
        def lombokConfiguration = project.configurations.findByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)
        def dependency = lombokConfiguration.getDependencies().first()
        dependency.group == "org.projectlombok"
        dependency.name == "lombok"
    }

    def "Added dependencies are not transitive"() {
        when:
        applyJavaAndLombok()
        project.evaluate()

        then:
        def lombokConfiguration = project.configurations.findByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)
        lombokConfiguration.getDependencies().each {
            assert !it.isTransitive()
        }
    }

    def "Add tasks if Java plugin is applied and installLombok depends on verifyLombok"() {
        when:
        applyJavaAndLombok()

        then:
        VerifyLombokTask verifyLombok = project.tasks[VerifyLombokTask.NAME]
        InstallLombokTask installLombok = project.tasks[InstallLombokTask.NAME]
        verifyLombok in installLombok.getDependsOn()
    }

}
