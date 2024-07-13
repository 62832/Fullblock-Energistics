pluginManagement {
    plugins {
        id("net.neoforged.moddev") version "0.1.112"
        id("net.neoforged.moddev.repositories") version "0.1.112"
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
                name = "Modrinth Maven"
                url = uri("https://api.modrinth.com/maven")
                content {
                    includeGroup("maven.modrinth")
                }
            }
        }

        versionCatalogs {
            create("libs") {
                version("neoforge", "21.0.87-beta")

                version("ae2", "19.0.11-alpha")
                library("ae2", "appeng", "appliedenergistics2").versionRef("ae2")

                version("requester", "1.20.4-1.1.6")
                library("requester", "maven.modrinth", "merequester").version("cj9FGAyI")

                library("jade", "maven.modrinth", "jade").version("gF1TRsRm")
            }
        }
    }
}
