package io.franzbecker.gradle.lombok.task
import io.franzbecker.gradle.lombok.LombokPlugin
import io.franzbecker.gradle.lombok.LombokPluginExtension
import org.gradle.api.tasks.JavaExec

/**
 * Invokes the Lombok UI so that the user can install it into the IDE.
 */
class InstallLombokTask extends JavaExec {

    static final String NAME = "installLombok"

    @Override
    void exec() {
        // Retrieve extension and configuration
        def extension = project.extensions.findByType(LombokPluginExtension)
        def configuration = project.configurations.getByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)

        // Configure JavaExec
        setMain(extension.main)
        setIgnoreExitValue(true)
        setClasspath(configuration)

        super.exec()
    }

}
