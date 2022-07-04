buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.igormaznitsa:jcp:7.0.5")
    }
}

plugins {
    id("com.github.prokod.gradle-crossbuild") version "0.13.1"
    idea
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.scala-lang:scala-library:2.13.8")
}

subprojects {
    apply(plugin = "com.github.prokod.gradle-crossbuild-scala")

    crossBuild {
        scalaVersionsCatalog = mapOf(
            "2.13" to "2.13.8",
            "2.12" to "2.12.15",
        )

        val sparkVersionsForBoth = listOf("3.3.0", "3.2.1", "3.2.0")
        val sparkVersionsFor2_12 = listOf("3.1.3", "3.1.2", "3.1.1", "3.1.0", "3.0.3", "3.0.2", "3.0.1", "3.0.0")

        builds {
            for (spark in sparkVersionsForBoth)
                create(spark) {
                    scalaVersions = mutableSetOf("2.12", "2.13")
                }

            for (spark in sparkVersionsFor2_12)
                create(spark) {
                    scalaVersions = mutableSetOf("2.12")
                }
        }
    }
}

idea.module {
    sourceDirs = mutableSetOf(File("./gradle-crossbuild-sample-kotlin/src/main/kotlin"))
}
