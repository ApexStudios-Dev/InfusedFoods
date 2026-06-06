plugins {
    id("apex-conventions.neoforge")
    id("apex-conventions.neoforge-datagen")
    id("apex-conventions.maven-publishing")
    id("apex-conventions.jspecify")
}

group = "dev.apexstudios"
neoForge.version = libs.versions.neoforge.get()

repositories {
    maven("https://prmaven.neoforged.net/NeoForge/pr3198") {
        content {
            includeModule("net.neoforged", "neoforge")
            includeModule("net.neoforged", "testframework")
        }
    }

    maven("https://maven.apexmodder.com/prs/Registree/pr29") {
        content {
            includeModule("dev.apexstudios", "registree")
        }
    }

    maven("https://maven.apexmodder.com/prs/ApexCore/pr88") {
        content {
            includeModule("dev.apexstudios", "apexcore")
        }
    }
}

dependencies {
    implementation(libs.bundles.apexcore)
    "dataImplementation"(libs.bundles.apexcore)
    accessTransformers(libs.apexcore)
}