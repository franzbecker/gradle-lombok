package net.franz_becker.gradle.lombok

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPlugin
import org.gradle.plugins.ide.eclipse.EclipsePlugin
import org.gradle.plugins.ide.idea.IdeaPlugin

/**
 * Plugin for project Lombok support.
 *
 * @see <a href="https://projectlombok.org">https://projectlombok.org</a>
 */
class LombokPlugin implements Plugin<Project> {

    static final NAME = "net.franz-becker.gradle-lombok"
    static final LOMBOK_CONFIGURATION_NAME = "lombok"

    @Override
    void apply(Project project) {
        project.extensions.create("lombok", LombokPluginExtension)
        // TODO verify lombok JAR
        project.plugins.withType(JavaPlugin) {
            def configuration = createLombokConfiguration(project)
            addLombokDependency(project)
            configureIdeaPlugin(project, configuration)
            configureEclipsePlugin(project, configuration)
        }
    }

    // TODO can we maybe depend and reuse com.netflix.nebula:gradle-extra-configurations-plugin ?
    private Configuration createLombokConfiguration(Project project) {
        def lombok = project.configurations.create(LOMBOK_CONFIGURATION_NAME)
                .setVisible(false)
                .setDescription("Additional compile classpath for Lombok.")
        def compile = project.configurations.getByName(JavaPlugin.COMPILE_CONFIGURATION_NAME)
        compile.extendsFrom(lombok)
        return lombok
    }

    private void configureIdeaPlugin(Project project, Configuration lombok) {
        project.plugins.withType(IdeaPlugin) {
            project.idea.module {
                scopes.PROVIDED.plus += lombok
            }
        }
    }

    private void configureEclipsePlugin(Project project, Configuration lombok) {
        project.plugins.withType(EclipsePlugin) {
            project.task(type: EclipseInstallerTask, EclipseInstallerTask.NAME)
            project.eclipse.classpath.plusConfigurations += [lombok]
        }
    }

    private void addLombokDependency(Project project) {
        project.dependencies.add(LOMBOK_CONFIGURATION_NAME, "org.projectlombok:lombok:${project.lombok.version}")
    }

}
