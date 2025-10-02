import dev.apexstudios.gradle.single.ApexSingleExtension

plugins {
    id("apex-conventions.neoforge") version "0.1.74"
    id("apex-conventions.maven-publishing") version "0.1.74"
}

group = "dev.apexstudios"

apex.neoVersion("21.9.0-beta", "1.21.8", "2025.09.14")
apex.extendCompilerErrors()

val single = ApexSingleExtension.getOrCreate(project)
single.withDataGen()

dependencies {
    implementation(libs.apexcore)
    accessTransformers(libs.apexcore)
    "dataImplementation"(libs.apexcore)
}
