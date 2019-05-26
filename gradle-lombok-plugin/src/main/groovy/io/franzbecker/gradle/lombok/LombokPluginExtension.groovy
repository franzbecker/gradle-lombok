package io.franzbecker.gradle.lombok

/**
 * Configuration for the plugin.
 */
class LombokPluginExtension {

    static final String NAME = "lombok"
    static final String DEFAULT_VERSION = "1.18.8"
    static final String DEFAULT_VERSION_HASH = "0396952823579b316a0fe85cbd871bbb3508143c2bcbd985dd7800e806cb24fc"

    /** The version of Lombok to use. */
    String version = DEFAULT_VERSION

    /** The SHA-256 hash of the JAR. */
    String sha256 = DEFAULT_VERSION_HASH

    /** The main class to call when invoking {@linkplain InstallLombokTask#NAME}. */
    String main = "lombok.launch.Main"

}
