package net.franz_becker.gradle.lombok

import nebula.test.PluginProjectSpec

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

    def "Does not add Lombok dependency if Java plugin is not applied"() {
        when:
        project.apply plugin: pluginName

        then:
        assert !project.configurations.findByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)
        assert project.configurations.collect { it.dependencies }.flatten().size() == 0
    }

    def "Add Lombok configuration and dependency if Java plugin is applied"() {
        when:
        project.apply plugin: "java"
        project.apply plugin: pluginName

        then:
        def lombokConfig = project.configurations.findByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)
        assert lombokConfig
        assert lombokConfig.dependencies.size() == 1
        assert lombokConfig.dependencies.first().name == "lombok"
        assert project.configurations.collect { it.dependencies }.flatten().size() == 1
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
