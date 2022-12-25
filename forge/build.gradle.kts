plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

architectury {
    platformSetupLoomIde()
    forge()
}

loom {
    runs {
        create("data") {
            data()
            programArgs("--existing", project(":common").file("src/main/resources").absolutePath)
            programArgs("--existing", file("src/main/resources").absolutePath)
        }
    }

    accessWidenerPath.set(project(":common").loom.accessWidenerPath)

    forge {
        val modId = property("mod_id").toString()

        dataGen {
            mod(modId)
        }

        convertAccessWideners.set(true)
        extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)

        mixinConfig("$modId-common.mixins.json")
        mixinConfig("$modId.mixins.json")
    }
}

val common: Configuration by configurations.creating
val shadowCommon: Configuration by configurations.creating

configurations {
    compileClasspath.get().extendsFrom(common)
    runtimeClasspath.get().extendsFrom(common)
    named("developmentForge").get().extendsFrom(common)
}

dependencies {
    val mcVersion = property("minecraft_version").toString()

    forge("net.minecraftforge:forge:$mcVersion-${property("forge_version")}")
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")

    common(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    shadowCommon(project(path = ":common", configuration = "transformProductionForge")) { isTransitive = false }

    modImplementation("appeng:appliedenergistics2-forge:${property("ae2_version")}")

    modRuntimeOnly("curse.maven:merequester-688367:${property("requester_fileid")}")
    modRuntimeOnly("mezz.jei:jei-$mcVersion-forge:${property("jei_version")}") { isTransitive = false }
    modRuntimeOnly("curse.maven:jade-324717:${property("jade_fileid")}")
}

sourceSets {
    main {
        resources {
            exclude("**/.cache")
        }
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }

    shadowJar {
        exclude("fabric.mod.json")
        exclude("architectury.common.json")

        configurations = listOf(shadowCommon)
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
        archiveClassifier.set(null as String?)
    }

    jar {
        archiveClassifier.set("dev")
    }

    sourcesJar {
        val commonSources = project(":common").tasks.sourcesJar
        dependsOn(commonSources)
        from(commonSources.get().archiveFile.map { zipTree(it) })
    }
}

val javaComponent = components["java"] as AdhocComponentWithVariants
javaComponent.withVariantsFromConfiguration(configurations["shadowRuntimeElements"]) {
    skip()
}
