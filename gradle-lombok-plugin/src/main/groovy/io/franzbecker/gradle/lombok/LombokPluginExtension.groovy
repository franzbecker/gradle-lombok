package io.franzbecker.gradle.lombok

/**
 * Configuration for the plugin.
 */
class LombokPluginExtension {

    static final String NAME = "lombok"
    static final String DEFAULT_VERSION = "1.18.10"
    static final String DEFAULT_VERSION_HASH = "2836e954823bfcbad45e78c18896e3d01058e6f643749810c608b7005ee7b2fa"

    /** The version of Lombok to use. */
    String version = DEFAULT_VERSION

    /** The SHA-256 hash of the JAR. */
    String sha256 = DEFAULT_VERSION_HASH

    /** The main class to call when invoking {@linkplain InstallLombokTask#NAME}. */
    String main = "lombok.launch.Main"

}
