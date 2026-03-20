pluginManagement {
    repositories {
        maven("https://maven.apexmodder.com/proxy")
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            if(requested.id.namespace == "apex-conventions") {
                useVersion("0.1.94")
            }
        }
    }
}

dependencyResolutionManagement {
    versionCatalogs.create("libs") {
        version("neoforge", "26.1.0.0-alpha.0+pre-2.20260316.055702")

        library("registree", "dev.apexstudios", "registree").version("26.1.20-beta-pr-17")
        library("apexcore", "dev.apexstudios", "apexcore").version("26.1.31-beta-pr-70")
        bundle("apexcore", listOf("registree", "apexcore"))
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "InfusedFoods"
