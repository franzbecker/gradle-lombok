package io.franzbecker.gradle.lombok

import io.franzbecker.gradle.lombok.task.InstallLombokTask
import io.franzbecker.gradle.lombok.task.VerifyLombokTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPlugin
import org.gradle.plugins.ide.eclipse.EclipsePlugin
import org.gradle.plugins.ide.idea.IdeaPlugin

/**
 * Plugin for project Lombok support.
 *
 * The main responsibility is to add Lombok as a "provided" dependency which is
 * only required during compilation and should not leave any trace on the resulting artifact.
 *
 * Furthermore, this plugin provides support for easy installation of Lombok into the IDE.
 * For this purpose, it verifies the JAR it adds as dependency (using its SHA-256 hash) and
 * the invokes it in order to call the default Lombok UI.
 *
 * @see <a href="https://projectlombok.org">https://projectlombok.org</a>
 */
class LombokPlugin implements Plugin<Project> {

    static final String NAME = "io.franzbecker.gradle-lombok"
    static final String LOMBOK_CONFIGURATION_NAME = "lombok"

    @Override
    void apply(Project project) {
        // Register extension
        project.extensions.create(LombokPluginExtension.NAME, LombokPluginExtension)

        project.plugins.withType(JavaPlugin) {
            def configuration = createLombokConfiguration(project)
            configureTasks(project)
            configureIdeaPlugin(project, configuration)
            configureEclipsePlugin(project, configuration)
        }
    }

    /**
     * Create a separate {@link Configuration} for the Lombok dependency.
     */
    private Configuration createLombokConfiguration(Project project) {
        def configuration = project.configurations.create(LOMBOK_CONFIGURATION_NAME)
            .setVisible(false)
            .setDescription("Additional compile classpath for Lombok.")

        project.afterEvaluate {
            project.dependencies.add(LOMBOK_CONFIGURATION_NAME, "org.projectlombok:lombok:${project.lombok.version}")
        }

        def compile = project.configurations.getByName(JavaPlugin.COMPILE_CONFIGURATION_NAME)
        compile.extendsFrom(configuration)
        return configuration
    }

    /**
     * Adds {@link VerifyLombokTask} and {@link InstallLombokTask} and lets installLombok
     * depend on verifyLombok.
     */
    private void configureTasks(Project project) {
        // Add VerifyLombokTask
        def verifyLombok = project.task(type: VerifyLombokTask, VerifyLombokTask.NAME)
        verifyLombok.outputs.upToDateWhen { false }

        // Add InstallLombokTask
        project.task(type: InstallLombokTask, InstallLombokTask.NAME).with {
            outputs.upToDateWhen { false }
            dependsOn verifyLombok
        }
    }

    /**
     * If the Idea plugin is present, Lombok is added to its classpath.
     */
    private void configureIdeaPlugin(Project project, Configuration lombok) {
        project.plugins.withType(IdeaPlugin) {
            project.idea.module {
                scopes.PROVIDED.plus += [lombok]
            }
        }
    }

    /**
     * If the Eclipse plugin is present, Lombok is added to its classpath.
     */
    private void configureEclipsePlugin(Project project, Configuration lombok) {
        project.plugins.withType(EclipsePlugin) {
            project.eclipse.classpath.plusConfigurations += [lombok]
        }
    }

}
