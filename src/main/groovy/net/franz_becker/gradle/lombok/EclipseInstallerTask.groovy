package net.franz_becker.gradle.lombok

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by becker on 18.08.15.
 */
class EclipseInstallerTask extends DefaultTask {

    static final NAME = "eclipseInstallLombok"

    @TaskAction
    def installLombok() {
        // TODO implement
        println "Installing Lombok as Java agent"
    }

}
