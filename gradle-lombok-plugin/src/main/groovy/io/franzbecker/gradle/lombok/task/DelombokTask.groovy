package io.franzbecker.gradle.lombok.task

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
    String compileConfigurationName = JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME

    DelombokTask() {
        super()
        args "delombok"

        def extension = project.extensions.findByType(LombokPluginExtension)
        def configurations = project.configurations
        mainClass.convention(project.provider { extension.main })
        // resolved lazily so that compileConfigurationName can still be changed
        classpath({ configurations.getByName(compileConfigurationName) })
    }

}
