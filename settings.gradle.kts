pluginManagement {
    repositories {
        maven { url = uri("https://maven.fabricmc.net/") }
        maven { url = uri("https://maven.architectury.dev/") }
        maven { url = uri("https://maven.minecraftforge.net/") }
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            plugin("archLoom", "dev.architectury.loom").version("1.3-SNAPSHOT")
            plugin("architectury", "architectury-plugin").version("3.4-SNAPSHOT")
            plugin("shadow", "com.github.johnrengelman.shadow").version("8.1.1")
            plugin("unifiedPublishing", "me.shedaniel.unified-publishing").version("0.1.+")
            plugin("spotless", "com.diffplug.spotless").version("6.20.0")

            val minecraftVersion = "1.19.2"
            version("minecraft", minecraftVersion)
            library("minecraft", "com.mojang", "minecraft").versionRef("minecraft")

            library("fabric-loader", "net.fabricmc", "fabric-loader").version("0.14.16")
            library("fabric-api", "net.fabricmc.fabric-api", "fabric-api").version("0.76.0+$minecraftVersion")

            library("forge", "net.minecraftforge", "forge").version("$minecraftVersion-43.2.0")

            version("ae2", "12.9.5")
            library("ae2-fabric", "appeng", "appliedenergistics2-fabric").versionRef("ae2")
            library("ae2-forge", "appeng", "appliedenergistics2-forge").versionRef("ae2")

            val requesterVersion = "1.1.3"
            version("requester", requesterVersion)
            library("requester-fabric", "maven.modrinth", "merequester").version("$minecraftVersion-$requesterVersion+fabric")
            library("requester-forge", "maven.modrinth", "merequester").version("$minecraftVersion-$requesterVersion+forge")

            library("midnightLib", "maven.modrinth", "midnightlib").version("1.0.0-fabric")
        }
    }
}

include("common")
include("fabric")
include("forge")

rootProject.name = "Fullblock-Energistics"
