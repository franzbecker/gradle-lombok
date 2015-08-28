# gradle-lombok
[![License](http://img.shields.io/badge/license-Apache_2.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Download](https://api.bintray.com/packages/franzbecker/maven/gradle-lombok-plugin/images/download.svg) ](https://bintray.com/franzbecker/maven/gradle-lombok-plugin/_latestVersion)
[![Build Status](https://travis-ci.org/franzbecker/gradle-lombok.svg?branch=master)](https://travis-ci.org/franzbecker/gradle-lombok)
[![codecov.io](http://codecov.io/github/franzbecker/gradle-lombok/coverage.svg?branch=master)](http://codecov.io/github/franzbecker/gradle-lombok?branch=master)


A Gradle plugin for project [Lombok](https://projectlombok.org) support.

# Usage
Currently the plugin is not in the official repository yet, so one has to add a Bintray Maven repository. A minimal `build.gradle` using this plugin looks like this:

	buildscript {
		repositories {
			maven {
				url "http://dl.bintray.com/franzbecker/maven"
			}
		}
		dependencies {
			classpath 'net.franz-becker:gradle-lombok:1.1'
		}
	}

	repositories {
    	jcenter() // or Maven central, required for Lombok dependency
	}

	apply plugin: 'java'
	apply plugin: 'net.franz-becker.gradle-lombok'

# Motivation

There are two issues this plugin solves: 

* Adding the dependency to the classpath in a non-intrusive way 
* Simplifying IDE support

## Adding Lombok dependency

In Maven you would usually do something like this:

	<dependencies>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.16.6</version>
			<scope>provided</scope>
		</dependency>
	</dependencies>
	
This will add the Lombok dependency to your project using the `provided` scope. The semantic of this scope is that you expect the JDK or a container to provide the dependency at runtime, which is not really what you want since the dependency is only required at compile time - a scope not supported by Maven.

In Gradle there is no `provided` scope by default. There are plugins addressing this issue such as the excellent [gradle-extra-configurations-plugin](https://github.com/nebula-plugins/gradle-extra-configurations-plugin) (which inspired this plugin) that provide this scope, or the `war` plugin with its `providedCompile` scope, but they implement the same semantic as Maven.

What we really want for Lombok is to have a dependency only during compile time. This is provided by this plugin by creating a new scope and applying the Lombok dependency to it. You can easily change the applied version by configuration:

	lombok {
		version = 1.16.6
		sha256 = "3ca225ce3917eac8bf4b7d2186845df4e70dcdede356dca8537b6d78a535c91e"
	}
	
Note that the hash code is only used for the second issue this plugin addresses: the simplification of IDE support.

## Simplifying IDE support

The project Lombok website states:
> NOTE: You'll still need to download lombok, or doubleclick on the lombok.jar file downloaded by maven / ivy / gradle, to install lombok into your eclipse installation.
 
When using Lombok in teams with no automated IDE provisioning this is quite a pain. Every developer has to retrieve the JAR, verify its integrity (many won't do that) and call `java -jar lombok.jar`.

This plugin adds a task called `installLombok` to your Gradle build that uses the dependency that has already been added to the compile-only scope, verifies its integrity using SHA-256 and finally invokes the main class of the JAR. This greatly simplifies the described process for each developer on the team.
