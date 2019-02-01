package io.franzbecker.gradle.lombok.task

import io.franzbecker.gradle.lombok.LombokPlugin
import io.franzbecker.gradle.lombok.LombokPluginExtension
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.JavaExec

/**
 * Task type for delomboking. Not added to the project by default,
 * has to be instantiated and configured in the using gradle build.
 */
class DelombokTask extends JavaExec {

    @Input
    String compileConfigurationName = JavaPlugin.COMPILE_CONFIGURATION_NAME

    DelombokTask() {
        super()
        args "delombok"
    }

    @Override
    void exec() {
        // Retrieve extension and configuration
        def extension = project.extensions.findByType(LombokPluginExtension)
        def lombok = project.configurations.getByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)
        def compile = project.configurations.getByName(compileConfigurationName)

        // Configure JavaExec
        setMain(extension.main)
        classpath(lombok, compile)
        super.exec()
    }

}
