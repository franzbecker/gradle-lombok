package net.franz_becker.gradle.lombok

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin for project Lombok support.
 *
 * @see <a href="https://projectlombok.org">https://projectlombok.org</a>
 */
class LombokPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        // TODO add lombok to compile class path
        // TODO verify lombok JAR
        project.task(type: EclipseInstallerTask, EclipseInstallerTask.NAME)
    }

}
