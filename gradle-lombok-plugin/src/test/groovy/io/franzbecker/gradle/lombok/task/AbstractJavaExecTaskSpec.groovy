package io.franzbecker.gradle.lombok.task

import io.franzbecker.gradle.lombok.LombokPlugin
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * Base class for unit testing tasks of type JavaExec.
 */
abstract class AbstractJavaExecTaskSpec extends Specification {

    Project project

    void setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'java'
        project.apply plugin: LombokPlugin.NAME
    }

}
