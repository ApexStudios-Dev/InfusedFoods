pluginManagement {
    repositories {
        maven("https://maven.apexstudios.dev/proxy")
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            if(requested.id.namespace == "apex-conventions") {
                useVersion("0.1.91")
            }
        }
    }
}

dependencyResolutionManagement {
    versionCatalogs.create("libs") {
        version("neoforge", "26.1.0.0-alpha.0+snapshot-10.20260225.011214")

        library("registree", "dev.apexstudios", "registree").version("26.1.18-beta-pr-17")
        library("apexcore", "dev.apexstudios", "apexcore").version("26.1.29-beta-pr-70")
        bundle("apexcore", listOf("registree", "apexcore"))
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "InfusedFoods"
