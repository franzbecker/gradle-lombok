package io.franzbecker.gradle.lombok

import io.franzbecker.gradle.lombok.task.InstallLombokTask
import io.franzbecker.gradle.lombok.task.VerifyLombokTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.util.GradleVersion

import static org.gradle.api.plugins.JavaPlugin.*

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
    static final String LOMBOK_CONFIGURATION_NAME = COMPILE_ONLY_CONFIGURATION_NAME

    @Override
    void apply(Project project) {
        project.extensions.create(LombokPluginExtension.NAME, LombokPluginExtension)

        project.plugins.withType(JavaPlugin) {
            project.afterEvaluate {
                addLombokDependency(project)
            }
            configureTasks(project)
        }
    }

    private void addLombokDependency(Project project) {
        addLombokDependency(project, COMPILE_ONLY_CONFIGURATION_NAME)
        addLombokDependency(project, TEST_COMPILE_ONLY_CONFIGURATION_NAME)

        boolean atLeastGradle4_6 = GradleVersion.version(project.gradle.gradleVersion) >= GradleVersion.version('4.6')
        if (atLeastGradle4_6) {
            addLombokDependency(project, ANNOTATION_PROCESSOR_CONFIGURATION_NAME)
            addLombokDependency(project, TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME)
        }
    }

    private void addLombokDependency(Project project, String configurationName) {
        project.dependencies.add(
                configurationName,
                "org.projectlombok:lombok:${project.lombok.version}",
                { transitive = false }
        )
    }

    /**
     * Adds {@link VerifyLombokTask} and {@link InstallLombokTask} and lets installLombok
     * depend on verifyLombok.
     */
    private void configureTasks(Project project) {
        def extension = project.extensions.findByType(LombokPluginExtension)

        // Add VerifyLombokTask
        def verifyLombok = project.task(type: VerifyLombokTask, VerifyLombokTask.NAME)
        verifyLombok.outputs.upToDateWhen { !extension.sha256 }

        // Add InstallLombokTask
        project.task(type: InstallLombokTask, InstallLombokTask.NAME).with {
            outputs.upToDateWhen { false }
            dependsOn verifyLombok
        }
    }

}
