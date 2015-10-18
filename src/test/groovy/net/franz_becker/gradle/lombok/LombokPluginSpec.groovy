package net.franz_becker.gradle.lombok

import nebula.test.PluginProjectSpec
import net.franz_becker.gradle.lombok.task.InstallLombokTask
import net.franz_becker.gradle.lombok.task.VerifyLombokTask

/**
 * Unit tests for {@link LombokPlugin}.
 */
class LombokPluginSpec extends PluginProjectSpec {

    @Override
    String getPluginName() {
        return "net.franz-becker.gradle-lombok"
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
        project.apply plugin: "java"
        project.apply plugin: pluginName

        then:
        assert project.configurations.findByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)
    }

    def "Add tasks if Java plugin is applied and installLombok depends on verifyLombok"() {
        when:
        project.apply plugin: "java"
        project.apply plugin: pluginName

        then:
        VerifyLombokTask verifyLombok = project.tasks[VerifyLombokTask.NAME]
        InstallLombokTask installLombok = project.tasks[InstallLombokTask.NAME]
        verifyLombok in installLombok.getDependsOn()
    }

}
