pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()

        maven("https://maven.apexstudios.dev/releases")
    }

    resolutionStrategy {
        eachPlugin {
            if(requested.id.namespace == "apex-conventions") {
                useVersion("0.1-SNAPSHOT")
            }
        }
    }
}

dependencyResolutionManagement {
    versionCatalogs.create("libs") {
        library("apexcore", "dev.apexstudios", "apexcore").version("21.5.6-beta-pr-13")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "InfusedFoods"
