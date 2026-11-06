package io.franzbecker.gradle.lombok.task

import io.franzbecker.gradle.lombok.LombokPlugin
import io.franzbecker.gradle.lombok.LombokPluginExtension
import io.franzbecker.gradle.lombok.util.HashUtil
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Provider
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

    // Captured at configuration time: accessing Task.project during execution is deprecated,
    // and Configuration objects are not supported by the configuration cache.
    private final String classpathName = LombokPlugin.VERIFY_LOMBOK_CLASSPATH_CONFIGURATION_NAME
    private final Provider<String> lombokVersion
    private final Provider<String> lombokSha256
    private final FileCollection lombokClasspath

    VerifyLombokTask() {
        def extension = project.extensions.findByType(LombokPluginExtension)
        lombokVersion = project.provider { extension.version }
        lombokSha256 = project.provider { extension.sha256 }
        lombokClasspath = project.files(project.configurations.getByName(classpathName))
    }

    @TaskAction
    void verifyLombok() {
        verifyLombok(lombokVersion.getOrNull(), lombokSha256.getOrNull(), lombokClasspath)
    }

    protected void verifyLombok(String version, String sha256, FileCollection classpath) {
        if (sha256) {
            // Lookup JAR and verify it
            def lombokJar = getLombokJar(version, classpath)
            verifyIntegrity(sha256, lombokJar)
        } else {
            logger.info("Skipping verification of Lombok since SHA-256 was not configured.")
        }
    }

    /**
     * Retrieves the JAR from the dependencies.
     *
     * @throws org.gradle.api.resources.ResourceException if the JAR cannot be resolved
     */
    protected File getLombokJar(String lombokVersion, FileCollection classpath) {
        // Retrieve file
        def lombokFileName = "lombok-${lombokVersion}.jar"
        logger.debug("Searching for '${lombokFileName}' in dependencies of configuration '${classpathName}'.")
        def lombokJar = classpath.files.find { File file -> file.name == lombokFileName }
        if (!lombokJar) {
            throw new ResourceException("Could not find '${lombokFileName}' in dependencies of configuration '${classpathName}'.")
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
