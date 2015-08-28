package net.franz_becker.gradle.lombok
import net.franz_becker.gradle.lombok.util.HashUtil
import org.gradle.api.artifacts.Configuration
import org.gradle.api.resources.ResourceException
import org.gradle.api.tasks.JavaExec

import static net.franz_becker.gradle.lombok.LombokPlugin.LOMBOK_CONFIGURATION_NAME
/**
 * Installs the Lombok JAR as Eclipse Java agent.
 * <p>
 * It will look up the JAR from the dependencies, verify its integrity against the configured SHA-256 hash code
 * and finally execute {@code java -jar lombok-version.jar} to invoke the default Lombok installer.
 *
 */
class EclipseInstallerTask extends JavaExec {

    static final NAME = "eclipseInstallLombok"

    /**
     * Configure the task to be never up-to-date.
     */
    public EclipseInstallerTask() {
        getOutputs().upToDateWhen { false }
    }

    @Override
    void exec() {
        // Retrieve extension and configuration
        def extension = project.extensions.findByType(LombokPluginExtension)
        def configuration = project.configurations.getByName(LOMBOK_CONFIGURATION_NAME)

        // Lookup JAR and verify it
        def lombokJar = getLombokJar(extension.version, configuration)
        verifyIntegrity(extension.sha256, lombokJar)

        // Configure JavaExec
        setMain("lombok.launch.Main")
        setIgnoreExitValue(true)
        setClasspath(configuration)

        logger.quiet("  Verified the integrity of '${lombokJar.name}'. its installer.")
        super.exec()
    }

    /**
     * Retrieves the JAR from the dependencies.
     *
     * @throws ResourceException if the JAR cannot be resolved
     */
    protected File getLombokJar(String lombokVersion, Configuration configuration) {
        // Retrieve file
        def lombokFileName = "lombok-${lombokVersion}.jar"
        logger.debug("Searching for '${lombokFileName}' in dependencies of configuration '${LOMBOK_CONFIGURATION_NAME}'.")
        def lombokJar = configuration.find { File file -> file.name == lombokFileName }
        if (!lombokJar) {
            throw new ResourceException("Could not find '${lombokFileName}' in dependencies of configuration '${LOMBOK_CONFIGURATION_NAME}'.")
        }
        logger.debug("Found '${lombokJar}'.")
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
        logger.debug("Calculates matching hash '${actualSha256}' for '${lombokJar}'.")
    }

}
