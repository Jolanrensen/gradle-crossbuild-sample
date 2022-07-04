import com.igormaznitsa.jcp.gradle.JcpTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm") version "1.7.0"
    id("com.igormaznitsa.jcp")
    idea
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

// Performs the preprocessing
val preprocessMain by tasks.creating(JcpTask::class) {
    sources.set(listOf(File("./src/main/kotlin")))
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
    outputs.upToDateWhen { false }
}

tasks.compileKotlin { dependsOn(preprocessMain) }

// Redirect the kotlin plugin to compile the preprocessed sources instead
kotlin {
    sourceSets {
        main {
            kotlin.setSrcDirs(listOf(preprocessMain.target.get()))
        }
    }
}

dependencies {
    implementation("org.apache.spark:spark-sql_$scalaCompat:$spark")

    // TODO - adding dependencies as above would probably also be cleaner than
    // TODO - crossBuild303_213Implementation("...") and all its variants
    // TODO - especially when creating as many variants as I have...
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

idea.module {
    sourceDirs = mutableSetOf(File("./src/main/kotlin"))
}