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
                useVersion("[0.1,)")
            }
        }
    }
}

dependencyResolutionManagement {
    versionCatalogs.create("libs") {
        library("apexcore", "dev.apexstudios", "apexcore").version {
            strictly("[21.4.0,21.5.0)")
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

if(file("../ApexCore/build.gradle.kts").exists()) {
    includeBuild("../ApexCore") {
        dependencySubstitution {
            substitute(module("dev.apexstudios:apexcore")).using(project(":"))
        }
    }
}

rootProject.name = "InfusedFoods"
