# gradle-lombok
[![License](http://img.shields.io/badge/license-Apache_2.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://app.travis-ci.com/franzbecker/gradle-lombok.svg?branch=master)](https://app.travis-ci.com/franzbecker/gradle-lombok)
[![codecov.io](http://codecov.io/github/franzbecker/gradle-lombok/coverage.svg?branch=master)](http://codecov.io/github/franzbecker/gradle-lombok?branch=master)


A Gradle plugin for project [Lombok](https://projectlombok.org) support.

__Maintenance mode.__ Only critical bugs will be fixed and there won't be any more feature from my side. 
Please feel free to continue contributing and open PRs, I will still review them in a timely manner.

# Usage
The plugin is included in the [central plugin repository](https://plugins.gradle.org/plugin/io.franzbecker.gradle-lombok/5.0.0). 

A minimal `build.gradle` looks like this:

    plugins {
        id 'io.franzbecker.gradle-lombok' version '5.0.0'
        id 'java'
    }
    
    repositories {
        jcenter() // or Maven central, required for Lombok dependency
    }

After applying the plugin, the Lombok annotations can be used directly in any Java code and the Lombok Eclipse installer can be called via `gradlew installLombok`. The Lombok version can be configured using the following syntax:

    lombok { // optional: values below are the defaults
        version = "1.18.12"
        sha256 = "49381508ecb02b3c173368436ef71b24c0d4418ad260e6cc98becbcf4b345406"
    }

If the verifcation of the integrity shall be skipped, the `sha256` value needs to be set to null or an empty string:

    lombok {
        sha256 = "" // skip verifyLombok task
    }

Some examples can be found in the [examples](https://github.com/franzbecker/gradle-lombok/tree/master/examples) folder.


# Motivation

When using Lombok in your Gradle project, you should consider using this plugin because

* it adds the Lombok dependency to the classpath
* it simplifies the Eclipse IDE installation
* it offers support for delomboking

## Adding the Lombok dependency

This plugin adds the Lombok dependency to `compileOnly` and `testCompileOnly`. Starting with Gradle 4.6 also `annotationProcessor` and `testAnnotationProcessor`.
Note that for using Gradle prior to version 2.12 please use this plugin in version "1.11" which is the last version that supports Gradle < 2.12.

## Simplifying Eclipse IDE installation

The project Lombok website states:
> NOTE: You'll still need to download lombok, or doubleclick on the lombok.jar file downloaded by maven / ivy / gradle, to install lombok into your eclipse installation.
 
When using Lombok in teams with no automated Eclipse provisioning this is quite a pain. Every developer has to retrieve the JAR in the right version, verify its integrity (some won't) and call `java -jar lombok.jar`.

This plugin adds a task called `installLombok` to your Gradle build that uses the dependency that has already been added to the compile-only scope, verifies its integrity using SHA-256 and finally invokes the main class of the JAR. This greatly simplifies the installation process for each developer and makes sure that the same version is used across the team.

## Delombok support

The plugin offers basic support for delomboking. The `DelombokTask` is a simple `JavaExec` task that calls `delombok` on Lombok's main class and simplifies the setup of such a task in the build process. It is not a ready to use task.

Examples can be found at:

[examples/delombok-gradle-groovy/build.gradle](examples/delombok-gradle-groovy/build.gradle) and<br/>
[examples/delombok-gradle-kotlin/build.gradle.kts](examples/delombok-gradle-kotlin/build.gradle.kts)
