package net.franz_becker.gradle.lombok.task
import net.franz_becker.gradle.lombok.LombokPlugin
import net.franz_becker.gradle.lombok.util.HashUtilSpec
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * Unit tests for {@link net.franz_becker.gradle.lombok.task.VerifyLombokTask}.
 */
class VerifyLombokTaskSpec extends Specification {

    VerifyLombokTask task
    Configuration configuration = Mock()

    /**
     * Perform a minimal project setup that lets us retrieve the task.
     */
    void setup() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'java'
        project.apply plugin: LombokPlugin.NAME
        task = project.tasks.getByName(VerifyLombokTask.NAME)
        configuration.iterator() >> Mock(Iterator)
    }

    def "Fails if Lombok JAR is not found"() {
        given:
        def version = "1.16.4"

        when:
        task.getLombokJar(version, configuration)

        then:
        GradleException e = thrown()
        e.message == "Could not find 'lombok-${version}.jar' in dependencies of configuration 'lombok'."
    }

    def "Does not fail if file integrity is fulfilled"() {
        given:
        def file = new File(getClass().getClassLoader().getResource("dummy.txt").path)
        assert file.exists()

        when:
        task.verifyIntegrity(HashUtilSpec.DUMMY_TXT_HASH, file)

        then:
        noExceptionThrown()
    }

    def "Fails if file integrity is not fulfilled"() {
        given:
        def file = new File(getClass().getClassLoader().getResource("dummy.txt").path)
        assert file.exists()

        when:
        task.verifyIntegrity("wrongHash", file)

        then:
        GradleException e = thrown()
        e.message.contains("Expected checksum: wrongHash")
        e.message.contains("Actual checksum: ${HashUtilSpec.DUMMY_TXT_HASH}")
    }

}
