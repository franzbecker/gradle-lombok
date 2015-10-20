# gradle-lombok
[![License](http://img.shields.io/badge/license-Apache_2.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Download](https://api.bintray.com/packages/franzbecker/maven/gradle-lombok-plugin/images/download.svg) ](https://bintray.com/franzbecker/maven/gradle-lombok-plugin/_latestVersion)
[![Build Status](https://travis-ci.org/franzbecker/gradle-lombok.svg?branch=master)](https://travis-ci.org/franzbecker/gradle-lombok)
[![codecov.io](http://codecov.io/github/franzbecker/gradle-lombok/coverage.svg?branch=master)](http://codecov.io/github/franzbecker/gradle-lombok?branch=master)


A Gradle plugin for project [Lombok](https://projectlombok.org) support.

# Usage
The plugin is included in the [central plugin repository](https://plugins.gradle.org/plugin/net.franz-becker.gradle-lombok/1.5). 

A minimal `build.gradle` looks like this:

	plugins {
		id 'net.franz-becker.gradle-lombok' version '1.5'
		id 'java'
	}
	
	repositories {
		jcenter() // or Maven central, required for Lombok dependency
	}


After applying the plugin, the Lombok annotations can be used directly in any Java code and the Lombok Eclipse installer can be called via `gradlew installLombok`. The Lombok version can be configured using the following syntax:

	lombok { // optional: values below are the defaults
		version = "1.16.4"
		sha256 = "3ca225ce3917eac8bf4b7d2186845df4e70dcdede356dca8537b6d78a535c91e"
	}

Some example Java code to get started can be found in the [wiki](https://github.com/franzbecker/gradle-lombok/wiki/Lombok-getting-started).


# Motivation

When using Lombok in your Gradle project, you should consider using this plugin because

* it adds the Lombok dependency to the classpath in a non-intrusive way 
* it simplifies the Eclipse IDE installation
* it offers support for delomboking

## Adding the Lombok dependency

In Maven you would typically do something like this:

	<dependencies>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.16.4</version>
			<scope>provided</scope>
		</dependency>
	</dependencies>
	
This will add the Lombok dependency to your project using the `provided` scope. The semantic of this scope is that you expect the JDK or a container to provide the dependency at runtime, which is not really what you want since the dependency is only required at compile time. Since such a scope is not supported by Maven, the `provided` scope is the best approximation (note that the `compile` scope in Maven implies a runtime dependency).

In Gradle there is no `provided` scope by default. There are plugins addressing this issue such as the excellent [gradle-extra-configurations-plugin](https://github.com/nebula-plugins/gradle-extra-configurations-plugin) (which inspired this plugin) that provide this scope, or the `war` plugin with its `providedCompile` scope, but they implement the same semantic as Maven.

What we really want for Lombok is to have a dependency only during compile time. This is provided by this plugin by creating a new scope and applying the Lombok dependency to it. You can easily change the applied version by configuration:

	lombok {
		version = 1.16.4
		sha256 = "3ca225ce3917eac8bf4b7d2186845df4e70dcdede356dca8537b6d78a535c91e"
	}
	
Note that the hash code is only used for the second issue this plugin addresses: the simplification of IDE support.

## Simplifying Eclipse IDE installation

The project Lombok website states:
> NOTE: You'll still need to download lombok, or doubleclick on the lombok.jar file downloaded by maven / ivy / gradle, to install lombok into your eclipse installation.
 
When using Lombok in teams with no automated Eclipse provisioning this is quite a pain. Every developer has to retrieve the JAR in the right version, verify its integrity (some won't) and call `java -jar lombok.jar`.

This plugin adds a task called `installLombok` to your Gradle build that uses the dependency that has already been added to the compile-only scope, verifies its integrity using SHA-256 and finally invokes the main class of the JAR. This greatly simplifies the installation process for each developer and makes sure that the same version is used across the team.

## Delombok support
The plugin offers basic support for delomboking. The `DelombokTask` is a simple `JavaExec` task that calls `delombok` on Lombok's main class and simplifies the setup of such a task in the build process:

	import net.franz_becker.gradle.lombok.task.DelombokTask
	
	task delombok(type: DelombokTask) {
		args("src/main/java", "-d", "src/delombok/java")
	}
	
	task delombokHelp(type: DelombokTask) {
		args "--help"
	}
	
The class path for the `JavaExec` task includes, by default, the dependencies of the `compile` and `lombok` configurations only.