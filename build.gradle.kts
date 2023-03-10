import com.diffplug.gradle.spotless.SpotlessExtension
import me.shedaniel.unifiedpublishing.UnifiedPublishingExtension
import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    java
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "1.0-SNAPSHOT" apply false
    id("io.github.juuxel.loom-quiltflower") version "1.7.1" apply false
    id("me.shedaniel.unified-publishing") version "0.1.+" apply false
    id("com.diffplug.spotless") version "6.4.1" apply false
}

val mcVersion = property("minecraft_version").toString()
val modId = property("mod_id").toString()

architectury {
    minecraft = mcVersion
}

tasks {
    val collectJars by registering(Copy::class) {
        val tasks = subprojects.filter { it.path != ":common" }.map { it.tasks.named("remapJar") }
        dependsOn(tasks)
        from(tasks)
        into(buildDir.resolve("libs"))
    }

    assemble {
        dependsOn(collectJars)
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "architectury-plugin")
    apply(plugin = "com.diffplug.spotless")

    base.archivesName.set("$modId-${project.name}")
    version = (System.getenv("FULLENG_VERSION") ?: "v0.0").substring(1)
    group = "${property("maven_group")}.$modId"

    repositories {
        mavenLocal()
        mavenCentral()

        maven {
            name = "ModMaven (K4U-NL)"
            url = uri("https://modmaven.dev/")
            content {
                includeGroup("appeng")
            }
        }

        maven {
            name = "CurseMaven"
            url = uri("https://cursemaven.com")
            content {
                includeGroup("curse.maven")
            }
        }

        maven {
            name = "Modrinth Maven"
            url = uri("https://api.modrinth.com/maven")
            content {
                includeGroup("maven.modrinth")
            }
        }

        maven {
            name = "Progwml6"
            url = uri("https://dvs1.progwml6.com/files/maven/")
            content {
                includeGroup("mezz.jei")
            }
        }

        maven {
            name = "TerraformersMC"
            url = uri("https://maven.terraformersmc.com/")
            content {
                includeGroup("com.terraformersmc")
            }
        }
    }

    tasks {
        jar {
            from("LICENSE") {
                rename { "${it}_${base.archivesName}"}
            }
        }

        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.release.set(17)
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    configure<SpotlessExtension> {
        java {
            target("src/**/java/**/*.java")
            endWithNewline()
            indentWithSpaces()
            removeUnusedImports()
            toggleOffOn()
            trimTrailingWhitespace()
            eclipse().configFile(rootProject.file("codeformat/codeformat.xml"))
            importOrderFile(rootProject.file("codeformat/mega.importorder"))

            // courtesy of diffplug/spotless#240
            // https://github.com/diffplug/spotless/issues/240#issuecomment-385206606
            custom("noWildcardImports") {
                if (it.contains("*;\n")) {
                    throw Error("No wildcard imports allowed")
                }
                it
            }
            bumpThisNumberIfACustomStepChanges(1)
        }

        json {
            target("src/*/resources/**/*.json")
            targetExclude("src/generated/resources/**")
            prettier().config(mapOf("parser" to "json"))
        }
    }
}

subprojects {
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "io.github.juuxel.loom-quiltflower")
    apply(plugin = "me.shedaniel.unified-publishing")

    configure<LoomGradleExtensionAPI> {
        silentMojangMappingsLicense()
        mixin {
            defaultRefmapName.set("${base.archivesName}-refmap.json")
        }
    }

    dependencies {
        "minecraft"("com.mojang:minecraft:$mcVersion")
        "mappings"(project.extensions.getByName<LoomGradleExtensionAPI>("loom").officialMojangMappings())
    }

    architectury {
        injectInjectables = false
    }

    if ((project.name == "fabric" || project.name == "forge") && project.version != "0.0") {
        configure<UnifiedPublishingExtension> {
            project {
                val modVersion = project.version.toString()

                gameVersions.set(listOf(mcVersion))
                gameLoaders.set(listOf(project.name))
                version.set("${project.name}-$modVersion")

                val loader = project.name.substring(0, 1).toUpperCase() + project.name.substring(1)
                val changes = System.getenv("CHANGELOG") ?: "No changelog provided?"

                releaseType.set("release")
                changelog.set(changes)
                displayName.set(String.format("%s (%s %s)", modVersion, loader, mcVersion))

                mainPublication(project.tasks.getByName("remapJar")) // Declares the published jar

                relations {
                    depends {
                        curseforge.set("applied-energistics-2")
                        modrinth.set("ae2")
                    }
                    optional {
                        curseforge.set("merequester")
                        modrinth.set("merequester")
                    }
                }

                val cfToken = System.getenv("CURSEFORGE_TOKEN")
                if (cfToken != null) {
                    curseforge {
                        token.set(cfToken)
                        id.set("776293")
                    }
                }

                val mrToken = System.getenv("MODRINTH_TOKEN")
                if (mrToken != null) {
                    modrinth {
                        token.set(mrToken)
                        id.set("oRg5rweB")
                    }
                }
            }
        }
    }
}
