package io.franzbecker.gradle.lombok

/**
 * Configuration for the plugin.
 */
class LombokPluginExtension {

    static final String NAME = "lombok"
    static final String DEFAULT_VERSION = "1.18.12"
    static final String DEFAULT_VERSION_HASH = "49381508ecb02b3c173368436ef71b24c0d4418ad260e6cc98becbcf4b345406"

    /** The version of Lombok to use. */
    String version = DEFAULT_VERSION

    /** The SHA-256 hash of the JAR. */
    String sha256 = DEFAULT_VERSION_HASH

    /** The main class to call when invoking {@linkplain InstallLombokTask#NAME}. */
    String main = "lombok.launch.Main"

}
