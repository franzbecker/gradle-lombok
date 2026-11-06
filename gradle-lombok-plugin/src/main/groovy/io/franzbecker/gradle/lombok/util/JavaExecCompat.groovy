package io.franzbecker.gradle.lombok.util

import org.gradle.api.tasks.JavaExec

/**
 * Bridges {@link JavaExec} API differences between Gradle versions.
 */
class JavaExecCompat {

    /**
     * Lazily sets the default main class of the given task, so that a value configured
     * by the user still takes precedence.
     *
     * Uses the {@code mainClass} property where available (Gradle 6.4+), since
     * {@code main} was removed in Gradle 8. Falls back to a convention mapping of
     * {@code main} for older Gradle versions.
     */
    static void setMainClassConvention(JavaExec task, Closure<String> mainClass) {
        if (task.hasProperty('mainClass')) {
            task.mainClass.convention(task.project.provider(mainClass))
        } else {
            task.conventionMapping.map('main', mainClass)
        }
    }

}
