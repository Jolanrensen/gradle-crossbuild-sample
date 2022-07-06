import com.igormaznitsa.jcp.gradle.JcpTask

plugins {
    kotlin("jvm") version "1.7.0"
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

val kotlinMainSources = kotlin.sourceSets.main.get().kotlin.sourceDirectories

// Performs the preprocessing
val preprocessMain by tasks.creating(JcpTask::class) {
    sources.set(kotlinMainSources)
    clearTarget.set(true)
    fileExtensions.set(listOf("java", "kt"))
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

// Temporarily redirect the kotlin plugin to compile the preprocessed sources instead
tasks.compileKotlin {
    dependsOn(preprocessMain)
    outputs.upToDateWhen {
        preprocessMain.outcomingFiles.files.isEmpty()
    }

    doFirst {
        kotlin {
            sourceSets {
                main {
                    kotlin.setSrcDirs(listOf(preprocessMain.target.get()))
                }
            }
        }
    }

    doLast {
        kotlin {
            sourceSets {
                main {
                    kotlin.setSrcDirs(kotlinMainSources)
                }
            }
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":gradle-crossbuild-sample-scala"))
    implementation("org.apache.spark:spark-sql_$scalaCompat:$spark")

    // TODO - adding dependencies as above would probably also be cleaner than
    // TODO - crossBuild303_213Implementation("...") and all its variants
    // TODO - especially when creating as many variants as I have...
}