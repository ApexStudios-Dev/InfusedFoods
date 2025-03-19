import dev.apexstudios.gradle.single.ApexSingleExtension

plugins {
    id("apex-conventions.neoforge")
    id("apex-conventions.immaculate")
    id("apex-conventions.maven-publishing")
}

group = "dev.apexstudios"

apex.neoVersion("21.5.0-alpha.25w09a.20250319.011826", "1.21.4", "2025.02.16")
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
