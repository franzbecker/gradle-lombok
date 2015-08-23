package net.franz_becker.gradle.lombok

import nebula.test.PluginProjectSpec
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar

/**
 * Test the {@link LombokPlugin}.
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
        project.apply plugin: pluginName
        project.apply plugin: "java"

        then:
        def lombokConfig = project.configurations.findByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)
        assert lombokConfig
        assert lombokConfig.dependencies.size() == 1
        assert lombokConfig.dependencies.first().name == "lombok"
        assert project.configurations.collect { it.dependencies }.flatten().size() == 1
    }

    // TODO if the order is pluginName, "eclipse", "java" this test fails
    def "Add Lombok to the Eclipse classpath and installer task"() {
        when:
        project.apply plugin: pluginName
        project.apply plugin: "java"
        project.apply plugin: "eclipse"

        then:
        def lombokConfig = project.configurations.findByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)
        assert lombokConfig
        assert lombokConfig in project.eclipse.classpath.plusConfigurations
        assert project.tasks[EclipseInstallerTask.NAME] instanceof EclipseInstallerTask
    }

}
