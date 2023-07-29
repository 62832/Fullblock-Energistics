import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask

plugins {
    java
    id("dev.architectury.loom") version "1.3-SNAPSHOT" apply false
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("me.shedaniel.unified-publishing") version "0.1.+"
    id("com.diffplug.spotless") version "6.20.0"
}

val modId: String by project
val minecraftVersion: String by project
val javaVersion: String by project

val platforms by extra {
    property("enabledPlatforms").toString().split(',')
}

fun capitalise(str: String): String {
    return str.replaceFirstChar {
        if (it.isLowerCase()) {
            it.titlecase()
        } else {
            it.toString()
        }
    }
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

    withType<Jar> {
        enabled = false
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "architectury-plugin")
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "com.diffplug.spotless")

    base.archivesName.set("$modId-${project.name}")
    version = (System.getenv("FULLENG_VERSION") ?: "v0.0").substring(1)
    group = "${property("mavenGroup")}.$modId"

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    architectury {
        minecraft = minecraftVersion
        injectInjectables = false
    }

    configure<LoomGradleExtensionAPI> {
        silentMojangMappingsLicense()

        val accessWidenerFile = project(":common").file("src/main/resources/$modId.accesswidener")

        if (accessWidenerFile.exists()) {
            accessWidenerPath.set(accessWidenerFile)
        }
    }

    repositories {
        maven {
            name = "ModMaven (K4U-NL)"
            url = uri("https://modmaven.dev/")
            content {
                includeGroup("appeng")
                includeGroup("mezz.jei")
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
            name = "TerraformersMC"
            url = uri("https://maven.terraformersmc.com/")
            content {
                includeGroup("com.terraformersmc")
            }
        }
    }

    dependencies {
        "minecraft"("com.mojang:minecraft:$minecraftVersion")
        "mappings"(project.extensions.getByName<LoomGradleExtensionAPI>("loom").officialMojangMappings())
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

    spotless {
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

for (platform in platforms) {
    project(":$platform") {
        apply(plugin = "com.github.johnrengelman.shadow")
        apply(plugin = "me.shedaniel.unified-publishing")

        architectury {
            platformSetupLoomIde()
            loader(platform)
        }

        val common: Configuration by configurations.creating
        val shadowCommon: Configuration by configurations.creating

        configurations {
            compileClasspath.get().extendsFrom(common)
            runtimeClasspath.get().extendsFrom(common)
            named("development${capitalise(platform)}").get().extendsFrom(common)
        }

        dependencies {
            common(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
            shadowCommon(project(path = ":common", configuration = "transformProduction${capitalise(platform)}")) { isTransitive = false }
        }

        tasks {
            shadowJar {
                exclude("architectury.common.json")

                configurations = listOf(shadowCommon)
                archiveClassifier.set("dev-shadow")
            }

            withType<RemapJarTask> {
                inputFile.set(shadowJar.get().archiveFile)
                dependsOn(shadowJar)
                archiveClassifier.set(null as String?)
            }

            jar {
                archiveClassifier.set("dev")
            }
        }

        val javaComponent = components["java"] as AdhocComponentWithVariants
        javaComponent.withVariantsFromConfiguration(configurations["shadowRuntimeElements"]) {
            skip()
        }

        if (project.version != "0.0") {
            unifiedPublishing {
                project {
                    val modVersion = project.version.toString()

                    gameVersions.set(listOf(minecraftVersion))
                    gameLoaders.set(listOf(platform))
                    version.set("$platform-$modVersion")

                    val changes = System.getenv("CHANGELOG") ?: "No changelog provided?"

                    releaseType.set("release")
                    changelog.set(changes)
                    displayName.set(String.format("%s (%s %s)",
                            modVersion.substring(0, modVersion.lastIndexOf("-")),
                            capitalise(platform),
                            minecraftVersion))

                    mainPublication(project.tasks.getByName("remapJar"))

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
}
