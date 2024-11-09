pluginManagement {
    plugins {
        id("net.neoforged.moddev") version "1.0.14"
        id("net.neoforged.moddev.repositories") version "1.0.14"
        id("com.diffplug.spotless") version "6.25.0"
    }
}

plugins {
    id("net.neoforged.moddev.repositories")
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

run {
    @Suppress("UnstableApiUsage")
    dependencyResolutionManagement {
        repositoriesMode = RepositoriesMode.PREFER_SETTINGS
        rulesMode = RulesMode.PREFER_SETTINGS

        repositories {
            mavenCentral()

            maven {
                name = "ModMaven (K4U-NL)"
                url = uri("https://modmaven.dev/")
                content {
                    includeGroup("appeng")
                }
            }

            maven {
                name = "Curse Maven"
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
        }

        versionCatalogs {
            create("libs") {
                version("neoforge", "21.1.36")

                version("ae2", "19.0.21-beta")
                library("ae2", "appeng", "appliedenergistics2").versionRef("ae2")

                version("requester", "1.21.1-1.1.8")
                library("requester", "maven.modrinth", "merequester").version("a7QNsSHf")

                version("extendedae", "1.21-2.1.4-neoforge")
                library("extendedae", "curse.maven", "ex-pattern-provider-892005").version("5887634")
                library("glodium", "curse.maven", "glodium-957920").version("5821676")

                library("jade", "maven.modrinth", "jade").version("gF1TRsRm")
            }
        }
    }
}
