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

sourceSets.asMap.filter { it.key.startsWith("crossBuild") }.forEach {

    val sourceSet = it.value
    val spark = sourceSet.ext.get("spark") as String
    val sparkMinor = spark.substringBeforeLast('.')
    val scala = sourceSet.ext.get("scalaCompilerVersion") as String
    val scalaCompat = scala.substringBeforeLast('.')

    // Performs the preprocessing
    val task = tasks.create(it.key + "JcpTask", JcpTask::class) {
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

    // Here we make tasks dependency so preprocessing will kick in at the right moment
    (project.tasks.findByName(sourceSet.getCompileTaskName("scala")) as ScalaCompile).apply {
        dependsOn(task)
    }

    // Here we add in a programmatic way dependencies per cross build.
    // SourceSet ExtraProperties are leveraged again for that
    project.dependencies.add(
        sourceSet.implementationConfigurationName,
        mapOf("group" to "org.apache.spark", "name" to "spark-sql_${scalaCompat}", "version" to spark)
    )
}