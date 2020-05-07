package io.franzbecker.gradle.lombok.task
import io.franzbecker.gradle.lombok.LombokPlugin
import io.franzbecker.gradle.lombok.LombokPluginExtension
import org.gradle.api.Task
import org.gradle.api.tasks.JavaExec

/**
 * Invokes the Lombok UI so that the user can install it into the IDE.
 */
class InstallLombokTask extends JavaExec {

    static final String NAME = "installLombok"

    @Override
    Task configure(Closure closure) {
        setMain(project.extensions.findByType(LombokPluginExtension).main)
        return super.configure(closure)
    }

    @Override
    void exec() {
        // Configure JavaExec
        setIgnoreExitValue(true)
        setClasspath(project.configurations.getByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME))
        super.exec()
    }

}
