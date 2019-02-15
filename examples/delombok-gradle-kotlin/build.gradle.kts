import io.franzbecker.gradle.lombok.task.DelombokTask

tasks {

    val delombok by registering(DelombokTask::class)
    delombok {
        dependsOn(compileJava)
        val outputDir by extra { file("$buildDir/delombok") }
        outputs.dir(outputDir)
        sourceSets.getByName("main").java.srcDirs.forEach {
            inputs.dir(it)
            args(it, "-d", outputDir)
        }
        doFirst {
            outputDir.delete()
        }
    }

    javadoc {
        dependsOn(delombok)
        val outputDir: File by delombok.get().extra
        source = fileTree(outputDir)
        isFailOnError = false
    }

}
