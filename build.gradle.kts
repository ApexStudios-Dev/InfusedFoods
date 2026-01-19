plugins {
    id("apex-conventions.neoforge")
    id("apex-conventions.neoforge-datagen")
    id("apex-conventions.maven-publishing")
    id("apex-conventions.jspecify")
}

group = "dev.apexstudios"
neoForge.version = libs.versions.neoforge.get()

repositories {
    maven("https://prmaven.neoforged.net/NeoForge/pr2879") {
        content {
            includeModule("net.neoforged", "neoforge")
            includeModule("net.neoforged", "testframework")
        }
    }

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
    implementation(libs.bundles.apexcore)
    "dataImplementation"(libs.bundles.apexcore)
    accessTransformers(libs.apexcore)
}
