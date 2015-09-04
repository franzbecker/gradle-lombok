package net.franz_becker.gradle.lombok.task
import net.franz_becker.gradle.lombok.LombokPlugin
import net.franz_becker.gradle.lombok.LombokPluginExtension
import org.gradle.api.tasks.JavaExec

/**
 * Task type for delomboking. Not added to the project by default,
 * has to be instantiated and configured in the using gradle build.
 */
class DelombokTask extends JavaExec {

    String mainClass = "lombok.delombok.Delombok"

    public DelombokTask() {
        super()
        args "delombok"
    }

    @Override
    void exec() {
        // Retrieve extension and configuration
        def extension = project.extensions.findByType(LombokPluginExtension)
        def configuration = project.configurations.getByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)

        // Configure JavaExec
        setMain(extension.main)
        setClasspath(configuration)
        super.exec()
    }

}
