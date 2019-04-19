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
