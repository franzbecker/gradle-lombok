package io.franzbecker.gradle.lombok.task

import io.franzbecker.gradle.lombok.LombokPlugin
import io.franzbecker.gradle.lombok.LombokPluginExtension
import io.franzbecker.gradle.lombok.util.HashUtil
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.resources.ResourceException
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

/**
 * Task that verifies the integrity of the Lombok dependency.
 */
class VerifyLombokTask extends DefaultTask {

    static final String NAME = "verifyLombok"

    @Internal
    HashUtil hashUtil = new HashUtil()

    @TaskAction
    void verifyLombok() {
        // Retrieve extension and configuration
        def extension = project.extensions.findByType(LombokPluginExtension)
        def configuration = project.configurations.getByName(LombokPlugin.VERIFY_LOMBOK_CLASSPATH_CONFIGURATION_NAME)
        verifyLombok(extension, configuration)
    }

    protected verifyLombok(LombokPluginExtension extension, Configuration configuration) {
        if (extension.sha256) {
            // Lookup JAR and verify it
            def lombokJar = getLombokJar(extension.version, configuration)
            verifyIntegrity(extension.sha256, lombokJar)
        } else {
            logger.info("Skipping verification of Lombok since SHA-256 was not configured.")
        }
    }

    /**
     * Retrieves the JAR from the dependencies.
     *
     * @throws org.gradle.api.resources.ResourceException if the JAR cannot be resolved
     */
    protected File getLombokJar(String lombokVersion, Configuration configuration) {
        // Retrieve file
        def lombokFileName = "lombok-${lombokVersion}.jar"
        logger.debug("Searching for '${lombokFileName}' in dependencies of configuration '${configuration.name}'.")
        def lombokJar = configuration.files.find { File file -> file.name == lombokFileName }
        if (!lombokJar) {
            throw new ResourceException("Could not find '${lombokFileName}' in dependencies of configuration '${configuration.name}'.")
        }
        logger.debug("Found '${lombokJar}'.")
        return lombokJar
    }

    /**
     * Verifies the hash code of the passed file as configured.
     */
    protected void verifyIntegrity(String expectedSha256, File lombokJar) {
        def actualSha256 = hashUtil.calculateSha256(lombokJar)
        if (expectedSha256 != actualSha256) {
            def message = """\
                Verification of Lombok JAR failed!

                Local JAR file:    ${lombokJar}

                Expected checksum: ${expectedSha256}
                  Actual checksum: ${actualSha256}
            """.stripIndent()
            throw new ResourceException(message)
        }
        logger.debug("Calculates matching hash '${actualSha256}' for '${lombokJar}'.")
    }

}
