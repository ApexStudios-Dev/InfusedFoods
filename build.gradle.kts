import org.slf4j.event.Level

plugins {
    `java-library`
    `maven-publish`

    id("net.neoforged.moddev") version "2.0.141"
    id("apex-conventions.jspecify")
}

group = "dev.apexstudios"
base.archivesName = "infusedfoods"
version = providers.environmentVariable("VERSION").getOrElse("0.0NONE")

sourceSets {
    main {
        resources {
            exclude(".cache")
            srcDir("src/data/generated")
        }
    }

    create("data") {
        resources.setSrcDirs(files())

        compileClasspath += sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].output
        runtimeClasspath += sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].output
    }
}

neoForge {
    version = libs.versions.neoforge.get()
    addModdingDependenciesTo(sourceSets["data"])

    accessTransformers {
        from(file("src/${SourceSet.MAIN_SOURCE_SET_NAME}/resources/META-INF/accesstransformer.cfg"))
        publish(file("src/${SourceSet.MAIN_SOURCE_SET_NAME}/resources/META-INF/accesstransformer.cfg"))
    }

    mods.create("data") {
        sourceSet(sourceSets[SourceSet.MAIN_SOURCE_SET_NAME])
        sourceSet(sourceSets["data"])
    }

    runs {
        listOf(true, false).forEach { isClient ->
            val id = if(isClient) "client" else "server"

            create(id) {
                if(isClient) {
                    client()
                } else {
                    server()
                }

                logLevel.set(Level.DEBUG)
                gameDirectory.set(layout.projectDirectory.dir("run/$id"))
                systemProperty("terminal.ansi", "true") // fix terminal not having colors

                jvmArguments.addAll(
                    "-XX:+AllowEnhancedClassRedefinition",
                    "-XX:+IgnoreUnrecognizedVMOptions",
                    "-XX:+AllowRedefinitionToAddDeleteMethods",
                    "-XX:+ClassUnloading"
                )
            }
        }

        create("data") {
            clientData()

            sourceSet.set(sourceSets["data"])
            loadedMods.set(listOf(mods["data"]))

            programArguments.addAll(
                "--mod", "infusedfoods",
                "--all",
                "--output", file("src/data/generated").absolutePath,
                "--existing", file("src/${SourceSet.MAIN_SOURCE_SET_NAME}/resources").absolutePath
            )
        }
    }
}

repositories {
    maven("https://maven.apexmodder.com/releases")
}

dependencies {
    implementation(libs.bundles.apexcore)
    "dataImplementation"(libs.bundles.apexcore)
    accessTransformers(libs.apexcore)
}

java {
    toolchain.vendor.set(JvmVendorSpec.JETBRAINS)
    withSourcesJar()
}

publishing {
    publications.create("release", MavenPublication::class.java) {
        afterEvaluate {
            groupId = "dev.apexstudios"
            artifactId = "infusedfoods"
            version = project.version as String
        }

        from(components["java"])
    }

    repositories {
        if(System.getenv("MAVEN_USERNAME") != null && System.getenv("MAVEN_PASSWORD") != null) {
            maven("https://maven.apexmodder.com/releases") {
                name = "ApexStudios-Releases"

                credentials {
                    username = System.getenv("MAVEN_USERNAME")
                    password = System.getenv("MAVEN_PASSWORD")
                }

                authentication.create<BasicAuthentication>("basic")
            }
        }
    }
}