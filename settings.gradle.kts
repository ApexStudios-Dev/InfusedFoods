pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.apexmodder.com/releases")
    }

    if(file("../../ApexGradle").exists()) {
        includeBuild("../../ApexGradle")
    } else {
        resolutionStrategy {
            eachPlugin {
                if(requested.id.namespace == "apex-conventions") {
                    useVersion("0.1.100")
                }
            }
        }
    }
}

dependencyResolutionManagement {
    versionCatalogs.create("libs") {
        version("neoforge", "26.2.0.1-beta")

        library("registree", "dev.apexstudios", "registree").version("26.2.2-beta-pr-33")
        library("apexcore", "dev.apexstudios", "apexcore").version("26.2.4-beta-pr-92")
        bundle("apexcore", listOf("registree", "apexcore"))
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "InfusedFoods"
