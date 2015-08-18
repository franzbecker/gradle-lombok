package net.franz_becker.gradle.lombok

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

/**
 * Test the {@link LombokPlugin}.
 */
class LombokPluginTest {

    Project project

    @Before
    void createProject() {
        project = ProjectBuilder.builder().build()
    }

    @Test
    void testNoTasksOnEmptyProjects() {
        // Given
        def beforeTaskMap = project.tasks.getAsMap().collect()

        // When
        applyLombokPlugin()

        // Then
        def afterTaskMap = project.tasks.getAsMap().collect()
        assert afterTaskMap == beforeTaskMap
    }

    @Test
    void testLombokConfiguration() {
        // Given
        project.apply plugin: 'java'

        // When
        applyLombokPlugin()

        // Then
        def config = project.configurations.findByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)
        assert config != null
        def lombokDependency = config.dependencies.find {it.name == "lombok"}
        assert lombokDependency != null
    }

    @Test
    public void testEclipseConfiguration() {
        // Given
        project.apply plugin: 'java'
        project.apply plugin: 'eclipse'

        // When
        applyLombokPlugin()

        // Then
        def config = project.configurations.getByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)
        assert project.tasks[EclipseInstallerTask.NAME] instanceof EclipseInstallerTask
        assert config in project.eclipse.classpath.plusConfigurations
    }

    private void applyLombokPlugin() {
        project.apply plugin: 'net.franz-becker.gradle-lombok'
    }

}
