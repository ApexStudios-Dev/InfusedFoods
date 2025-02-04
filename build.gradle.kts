import dev.apexstudios.gradle.single.ApexSingleExtension
import me.modmuss50.mpp.ReleaseType

plugins {
    id("apex-conventions.neoforge")
    id("apex-conventions.immaculate")
    id("apex-conventions.mod-publishing")
}

group = "dev.apexstudios"

apex.neoVersion("21.4.50-beta", "2025.01.05")
apex.extendCompilerErrors()

val single = ApexSingleExtension.getOrCreate(project)
single.withDataGen()

dependencies {
    implementation(libs.apexcore)
    accessTransformers(libs.apexcore)
    interfaceInjectionData(libs.apexcore)
    "dataImplementation"(libs.apexcore)
}

publishMods {
    type = ReleaseType.ALPHA

    modrinth {
        projectId = "QinxcGwM"
    }

    curseforge {
        projectId = "629292"
        projectSlug = "infused-foods"
    }
}
