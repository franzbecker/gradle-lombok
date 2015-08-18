package net.franz_becker.gradle.lombok

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

/**
 * Test the {@link LombokPlugin}.
 */
class LombokPluginTest {

    @Test
    public void testTaskAttached() {
        // Given
        def project = ProjectBuilder.builder().build()

        // When
        project.pluginManager.apply('net.franz-becker.gradle-lombok')

        // Then
        assert project.tasks[EclipseInstallerTask.NAME] instanceof EclipseInstallerTask
    }

}
