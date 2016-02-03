package io.franzbecker.gradle.lombok

/**
 * Configuration for the plugin.
 */
class LombokPluginExtension {

    static final String NAME = "lombok"

    /** The version of Lombok to use. */
    def String version = "1.16.4"

    /** The SHA-256 hash of the JAR. */
    def String sha256 = "3ca225ce3917eac8bf4b7d2186845df4e70dcdede356dca8537b6d78a535c91e"

    /** The main class to call when invoking {@linkplain InstallLombokTask#NAME}. */
    def String main = "lombok.launch.Main"

}
