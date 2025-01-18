pluginManagement {
    if(file("../ApexGradle/build.gradle.kts").exists()) {
        includeBuild("../ApexGradle")
    }

    repositories {
        gradlePluginPortal()
        mavenLocal()

        maven("https://maven.apexstudios.dev/releases")
    }

    resolutionStrategy {
        eachPlugin {
            if(requested.id.namespace == "apex-conventions") {
                useVersion("+")
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

includeBuild("../ApexCore") {
    dependencySubstitution {
        substitute(module("dev.apexstudios:apexcore")).using(project(":"))
    }
}