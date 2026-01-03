import dev.apexstudios.gradle.single.ApexSingleExtension

plugins {
    id("apex-conventions.neoforge") version "0.1.88"
    id("apex-conventions.maven-publishing") version "0.1.88"
    id("apex-conventions.jspecify") version "0.1.88"
}

group = "dev.apexstudios"

apex.neoVersion("26.1.0.0-alpha.1+snapshot-1")
apex.extendCompilerErrors()

val single = ApexSingleExtension.getOrCreate(project)
single.withDataGen()

repositories {
    maven("https://maven.apexstudios.dev/prs/Registree/pr17") {
        content {
            includeModule("dev.apexstudios", "registree")
        }
    }

    maven("https://maven.apexstudios.dev/prs/Placement-Visualizer/pr19") {
        content {
            includeModule("dev.apexstudios", "placementvisualizer")
        }
    }

    maven("https://maven.apexstudios.dev/prs/ApexCore-Private/pr70") {
        content {
            includeModule("dev.apexstudios", "apexcore")
        }
    }
}

dependencies {
    implementation(libs.registree)
    "dataImplementation"(libs.registree)

    implementation(libs.apexcore)
    accessTransformers(libs.apexcore)
    "dataImplementation"(libs.apexcore)
}
