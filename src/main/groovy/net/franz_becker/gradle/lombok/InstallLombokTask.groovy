package net.franz_becker.gradle.lombok

import org.gradle.api.tasks.JavaExec

import static net.franz_becker.gradle.lombok.LombokPlugin.LOMBOK_CONFIGURATION_NAME

/**
 * Invokes the Lombok UI so that the user can install it into the IDE.
 */
class InstallLombokTask extends JavaExec {

    static final String NAME = "installLombok"

    @Override
    void exec() {
        // Retrieve extension and configuration
        def extension = project.extensions.findByType(LombokPluginExtension)
        def configuration = project.configurations.getByName(LOMBOK_CONFIGURATION_NAME)

        // Configure JavaExec
        setMain(extension.main)
        setIgnoreExitValue(true)
        setClasspath(configuration)

        super.exec()
    }

}
