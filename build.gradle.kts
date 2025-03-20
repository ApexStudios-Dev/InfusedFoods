import dev.apexstudios.gradle.single.ApexSingleExtension

plugins {
    id("apex-conventions.neoforge")
    id("apex-conventions.immaculate")
    id("apex-conventions.maven-publishing")
}

group = "dev.apexstudios"

apex.neoVersion("21.5.0-alpha.1.21.5-pre3.20250320.012342", "1.21.4", "2025.02.16")
apex.extendCompilerErrors()

val single = ApexSingleExtension.getOrCreate(project)
single.withDataGen()

neoForge {
    validateAccessTransformers.set(false)
}

repositories {
    maven("https://maven.apexstudios.dev/private")
}

dependencies {
    implementation(libs.apexcore)
    accessTransformers(libs.apexcore)
    "dataImplementation"(libs.apexcore)
}
