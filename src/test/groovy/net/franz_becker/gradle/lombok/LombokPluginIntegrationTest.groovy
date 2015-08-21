package net.franz_becker.gradle.lombok

import nebula.test.IntegrationSpec

class LombokPluginIntegrationTest extends IntegrationSpec {

    def setup() {
        buildFile << """
            apply plugin: 'java'
            apply plugin: 'net.franz-becker.gradle-lombok'

            repositories {
                jcenter()
            }
        """
    }

    def "Can compile HelloWorld"() {
        when:
        buildFile << """
        """
        createJavaSource()
        runTasksSuccessfully('compileJava')

        then:
        new File(projectDir, "build/classes/main/com/example/HelloWorld.class").exists()
    }

    private void createJavaSource() {
        File javaFile = createFile("src/main/java/com/example/HelloWorld.java")
        javaFile << """
            package com.example;

            import lombok.Data;

            @Data
            public class HelloWorld {

                private String id;

            }
        """
    }

}
