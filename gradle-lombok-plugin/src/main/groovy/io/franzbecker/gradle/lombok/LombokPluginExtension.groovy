package io.franzbecker.gradle.lombok

/**
 * Configuration for the plugin.
 */
class LombokPluginExtension {

    static final String NAME = "lombok"
    static final String DEFAULT_VERSION = "1.18.4"
    static final String DEFAULT_VERSION_HASH = "39f3922deb679b1852af519eb227157ef2dd0a21eec3542c8ce1b45f2df39742"

    /** The version of Lombok to use. */
    String version = DEFAULT_VERSION

    /** The SHA-256 hash of the JAR. */
    String sha256 = DEFAULT_VERSION_HASH

    /** The main class to call when invoking {@linkplain InstallLombokTask#NAME}. */
    String main = "lombok.launch.Main"

}
