package io.franzbecker.gradle.lombok

/**
 * Configuration for the plugin.
 */
class LombokPluginExtension {

    static final String NAME = "lombok"

    /** The version of Lombok to use. */
    String version = "1.16.20"

    /** The SHA-256 hash of the JAR. */
    String sha256 = "c5178b18caaa1a15e17b99ba5e4023d2de2ebc18b58cde0f5a04ca4b31c10e6d"

    /** The main class to call when invoking {@linkplain InstallLombokTask#NAME}. */
    String main = "lombok.launch.Main"

}
