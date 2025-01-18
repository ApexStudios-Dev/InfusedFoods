import dev.apexstudios.gradle.single.ApexSingleExtension

plugins {
    id("apex-conventions.neoforge")
    id("apex-conventions.immaculate")
}

group = "dev.apexstudios"

apex.neoVersion("21.4.50-beta", "2025.01.05")
apex.extendCompilerErrors()

val single = ApexSingleExtension.getOrCreate(project)
single.withDataGen()

dependencies {
    implementation("dev.apexstudios:apexcore:21.4.0")
    accessTransformers("dev.apexstudios:apexcore:21.4.0")
    interfaceInjectionData("dev.apexstudios:apexcore:21.4.0")
    "dataImplementation"("dev.apexstudios:apexcore:21.4.0")
}