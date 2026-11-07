package io.franzbecker.gradle.lombok.task

import io.franzbecker.gradle.lombok.LombokPlugin
import io.franzbecker.gradle.lombok.LombokPluginExtension
import org.gradle.api.tasks.JavaExec

/**
 * Invokes the Lombok UI so that the user can install it into the IDE.
 */
abstract class InstallLombokTask extends JavaExec {

    static final String NAME = "installLombok"

    InstallLombokTask() {
        def extension = project.extensions.findByType(LombokPluginExtension)
        mainClass.convention(project.provider { extension.main })
        ignoreExitValue = true
        // compileOnly is not resolvable in recent Gradle versions, use compileClasspath instead
        classpath = project.configurations.getByName(LombokPlugin.VERIFY_LOMBOK_CLASSPATH_CONFIGURATION_NAME)
    }

}
