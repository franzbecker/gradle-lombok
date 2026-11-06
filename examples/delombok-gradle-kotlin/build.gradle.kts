import io.franzbecker.gradle.lombok.task.DelombokTask

tasks {

    val delombok by registering(DelombokTask::class) {
        dependsOn(compileJava)
        val outputDir = layout.buildDirectory.dir("delombok").get().asFile
        outputs.dir(outputDir)
        sourceSets["main"].java.srcDirs.forEach {
            inputs.dir(it)
            args(it, "-d", outputDir)
        }
        doFirst {
            outputDir.deleteRecursively()
        }
    }

    javadoc {
        // use the delomboked sources (this also adds the task dependency)
        setSource(delombok)
        isFailOnError = false
    }

}
