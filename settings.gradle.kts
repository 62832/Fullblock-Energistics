pluginManagement {
    repositories {
        maven { url = uri("https://maven.neoforged.net/releases") }
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            plugin("moddev", "net.neoforged.moddev").version("0.1.85")
            plugin("spotless", "com.diffplug.spotless").version("6.23.3")

            version("minecraft", "1.21")
            version("neoforge", "21.0.13-beta")

            version("ae2", "19.0.2-alpha")
            library("ae2", "appeng", "appliedenergistics2-neoforge").versionRef("ae2")

            version("requester", "1.20.4-1.1.6")
            library("requester", "maven.modrinth", "merequester").version("cj9FGAyI")

            library("jade", "maven.modrinth", "jade").version("gF1TRsRm")
        }
    }
}

rootProject.name = "FullblockEnergistics"
