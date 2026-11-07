# Contributing

Please feel free to contribute using pull requests.
All contributions must be licensed under the [Apache License 2.0](LICENSE) or compatible.

## Testing IDEs locally

All features should be covered by unit and integration tests.
Still, since we don't have automated IDE tests it might be necessary to test your contribution
manually for IDE compatibility.

Perform the following Gradle command to publish the plugin to your local Maven repository:

```
./gradlew publishToMavenLocal
```

A simple sample build.gradle using that will look like:

```
buildscript {
    repositories {
        mavenLocal()
    }
    dependencies {
        classpath 'io.franzbecker:gradle-lombok:+'
    }
}

apply plugin: 'java'
apply plugin: 'io.franzbecker.gradle-lombok'
```

## Releasing

Releases are published to the [Gradle Plugin Portal](https://plugins.gradle.org/plugin/io.franzbecker.gradle-lombok)
by the [Release workflow](.github/workflows/release.yml) whenever a tag `v<version>` is pushed.

One-time setup: add the API key and secret from your Plugin Portal account as repository secrets
`GRADLE_PUBLISH_KEY` and `GRADLE_PUBLISH_SECRET` (Settings → Secrets and variables → Actions).

To release, run on an up-to-date `master` without local changes:

```
cd gradle-lombok-plugin
./gradlew release -Prelease.useAutomaticVersion=true -Prelease.releaseVersion=<version>
```

This sets `version` in `gradle.properties`, updates the version in the README, commits both as
`[release] ...`, creates the tag `v<version>` and pushes commit and tag. The workflow then checks
that the tag matches the version in `gradle.properties`, runs the tests and publishes the plugin.
