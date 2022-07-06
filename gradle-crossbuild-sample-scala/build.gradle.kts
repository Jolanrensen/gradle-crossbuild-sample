import com.igormaznitsa.jcp.gradle.JcpTask

plugins {
    scala
    id("com.igormaznitsa.jcp")
}

group = "nl.jolanrensen"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// TODO be able to somehow get these values from the project
val spark = project.findProperty("spark") as? String? ?: "3.3.0"
val scala = project.findProperty("scala") as? String? ?: "2.13.8"
val sparkMinor = spark.substringBeforeLast('.')
val scalaCompat = scala.substringBeforeLast('.')

val scalaMainSources = sourceSets.main.get().scala.sourceDirectories

// Performs the preprocessing
val preprocessMain by tasks.creating(JcpTask::class) {
    sources.set(scalaMainSources)
    clearTarget.set(true)
    fileExtensions.set(listOf("java", "scala"))
    vars.set(
        mapOf(
            "spark" to spark,
            "sparkMinor" to sparkMinor,
            "scala" to scala,
            "scalaCompat" to scalaCompat,
        )
    )
    outputs.upToDateWhen { target.get().exists() }
}

// Temporarily redirect the scala plugin to compile the preprocessed sources instead
tasks.compileScala {
    dependsOn(preprocessMain)
    outputs.upToDateWhen { preprocessMain.outcomingFiles.files.isEmpty() }
    doFirst {
        scala {
            sourceSets {
                main {
                    scala.setSrcDirs(listOf(preprocessMain.target.get()))
                }
            }
        }
    }

    doLast {
        scala {
            sourceSets {
                main {
                    scala.setSrcDirs(scalaMainSources)
                }
            }
        }
    }
}

dependencies {
    implementation("org.apache.spark:spark-sql_$scalaCompat:$spark")

    // TODO - adding dependencies as above would probably also be cleaner than
    // TODO - crossBuild303_213Implementation("...") and all its variants
    // TODO - especially when creating as many variants as I have...
}
