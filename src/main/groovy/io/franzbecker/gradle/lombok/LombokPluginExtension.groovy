package io.franzbecker.gradle.lombok

/**
 * Configuration for the plugin.
 */
class LombokPluginExtension {

    static final String NAME = "lombok"

    /** The version of Lombok to use. */
    def String version = "1.16.18"

    /** The SHA-256 hash of the JAR. */
    def String sha256 = "9d957f572386b9e257093a45b148f9b411cff80d9efd55eaf6fca27002d2e4d9"

    /** The main class to call when invoking {@linkplain InstallLombokTask#NAME}. */
    def String main = "lombok.launch.Main"

}
