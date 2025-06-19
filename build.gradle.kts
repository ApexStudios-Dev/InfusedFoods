import dev.apexstudios.gradle.single.ApexSingleExtension

plugins {
    id("apex-conventions.neoforge")
    id("apex-conventions.immaculate")
    id("apex-conventions.maven-publishing")
}

group = "dev.apexstudios"

apex.neoVersion("21.5.75", "2025.06.01")
apex.extendCompilerErrors()

val single = ApexSingleExtension.getOrCreate(project)
single.withDataGen()

dependencies {
    implementation(libs.apexcore)
    accessTransformers(libs.apexcore)
    "dataImplementation"(libs.apexcore)
}
