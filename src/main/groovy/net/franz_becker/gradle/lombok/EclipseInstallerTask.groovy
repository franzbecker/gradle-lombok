package net.franz_becker.gradle.lombok

import net.franz_becker.gradle.lombok.util.HashUtil
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.resources.ResourceException
import org.gradle.api.tasks.TaskAction

/**
 * Created by becker on 18.08.15.
 */
class EclipseInstallerTask extends DefaultTask {

    static final NAME = "eclipseInstallLombok"

    @TaskAction
    def installLombok() {
        // Retrieve extension and configuration
        def extension = project.extensions.findByType(LombokPluginExtension)
        def configuration = project.configurations.getByName(LombokPlugin.LOMBOK_CONFIGURATION_NAME)

        // Lookup JAR
        def lombokJar = getLombokJar(extension.version, configuration)
        verifyIntegrity(extension.sha256, lombokJar)
        println "Got and verified Lombok JAR"
    }

    /**
     * Retrieves the JAR from the dependencies.
     *
     * @throws ResourceException if the JAR cannot be resolved
     */
    protected File getLombokJar(String lombokVersion, Configuration configuration) {
        // Retrieve file
        def lombokFileName = "lombok-${lombokVersion}.jar"
        def lombokJar = configuration.find { File file -> file.name == lombokFileName }
        if (!lombokJar) {
            throw new ResourceException("Could not resolve '${lombokFileName}'!")
        }
        return lombokJar
    }

    /**
     * Verifies the hash code of the passed file as configured.
     */
    protected void verifyIntegrity(String expectedSha256, File lombokJar) {
        def actualSha256 = HashUtil.calculateSha256(lombokJar)
        if (expectedSha256 != actualSha256) {
            def message = """\
                Verification of Lombok JAR failed!

                Local JAR file:    ${lombokJar}

                Expected checksum: ${expectedSha256}
                  Actual checksum: ${actualSha256}
            """.stripIndent()
            throw new ResourceException(message)
        }
    }

}
