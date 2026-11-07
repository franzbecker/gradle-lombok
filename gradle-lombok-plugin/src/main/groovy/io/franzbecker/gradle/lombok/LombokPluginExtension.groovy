package io.franzbecker.gradle.lombok

/**
 * Configuration for the plugin.
 */
class LombokPluginExtension {

    static final String NAME = "lombok"
    static final String DEFAULT_VERSION = "1.18.48"
    static final String DEFAULT_VERSION_HASH = "85477a4655ebb2c074a9099cfb749be454449fee564d4282610df1b85f7c508b"

    /** The version of Lombok to use. */
    String version = DEFAULT_VERSION

    /** The SHA-256 hash of the JAR. */
    String sha256 = DEFAULT_VERSION_HASH

    /** The main class to call when invoking {@linkplain InstallLombokTask#NAME}. */
    String main = "lombok.launch.Main"

}
