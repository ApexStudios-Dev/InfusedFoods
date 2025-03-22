import dev.apexstudios.gradle.single.ApexSingleExtension

plugins {
    id("apex-conventions.neoforge")
    id("apex-conventions.immaculate")
    id("apex-conventions.maven-publishing")
}

group = "dev.apexstudios"

apex.neoVersion("21.5.0-alpha.1.21.5-rc1.20250321.225134", "1.21.4", "2025.02.16")
apex.extendCompilerErrors()

val single = ApexSingleExtension.getOrCreate(project)
single.withDataGen()

neoForge {
    validateAccessTransformers.set(false)
}

repositories {
    maven("https://maven.apexstudios.dev/private")
    apex.neoPrMaven(this, 2039)
}

dependencies {
    implementation(libs.apexcore)
    accessTransformers(libs.apexcore)
    "dataImplementation"(libs.apexcore)
}
