plugins {
    id("apex-conventions.neoforge")
    id("apex-conventions.neoforge-datagen")
    id("apex-conventions.maven-publishing")
    id("apex-conventions.jspecify")
}

group = "dev.apexstudios"
neoForge.version = "26.1.0.0-alpha.5+snapshot-2"

repositories {
    maven("https://maven.apexstudios.dev/prs/Registree/pr17") {
        content {
            includeModule("dev.apexstudios", "registree")
        }
    }

    maven("https://maven.apexstudios.dev/prs/ApexCore-Private/pr70") {
        content {
            includeModule("dev.apexstudios", "apexcore")
        }
    }
}

dependencies {
    val registree = "26.1.8-beta-pr-17"
    implementation("dev.apexstudios:registree:$registree")
    "dataImplementation"("dev.apexstudios:registree:$registree")

    val apexcore = "26.1.10-beta-pr-70"
    implementation("dev.apexstudios:apexcore:$apexcore")
    "dataImplementation"("dev.apexstudios:apexcore:$apexcore")
    accessTransformers("dev.apexstudios:apexcore:$apexcore")
}
