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
            plugin("spotless", "com.diffplug.spotless").version("6.20.0")

            val minecraftVersion = "1.20.1"
            version("minecraft", minecraftVersion)
            library("minecraft", "com.mojang", "minecraft").versionRef("minecraft")

            library("fabric-loader", "net.fabricmc", "fabric-loader").version("0.14.21")
            library("fabric-api", "net.fabricmc.fabric-api", "fabric-api").version("0.84.0+$minecraftVersion")

            library("forge", "net.minecraftforge", "forge").version("$minecraftVersion-47.1.3")

            version("ae2", "15.0.5-beta")
            library("ae2-fabric", "appeng", "appliedenergistics2-fabric").versionRef("ae2")
            library("ae2-forge", "appeng", "appliedenergistics2-forge").versionRef("ae2")

            val requesterVersion = "1.1.3"
            version("requester", requesterVersion)
            library("requester-fabric", "maven.modrinth", "merequester").version("$minecraftVersion-$requesterVersion+fabric")
            library("requester-forge", "maven.modrinth", "merequester").version("$minecraftVersion-$requesterVersion+forge")

            library("midnightLib", "maven.modrinth", "midnightlib").version("1.4.1-fabric")

            library("jade-fabric", "maven.modrinth", "jade").version("u3ts4cHu")
            library("jade-forge", "maven.modrinth", "jade").version("2zfdBx1K")

            val wthitVersion = "8.3.0"
            val badPacketsVersion = "0.4.1"
            library("wthit-fabric", "maven.modrinth", "wthit").version("fabric-$wthitVersion")
            library("wthit-forge", "maven.modrinth", "wthit").version("forge-$wthitVersion")
            library("badpackets-fabric", "maven.modrinth", "badpackets").version("fabric-$badPacketsVersion")
            library("badpackets-forge", "maven.modrinth", "badpackets").version("forge-$badPacketsVersion")

            library("theoneprobe", "maven.modrinth", "the-one-probe").version("$minecraftVersion-10.0.0")
        }
    }
}

include("common")

for (platform in providers.gradleProperty("enabledPlatforms").get().split(',')) {
    include(platform)
}

val modName: String by settings
rootProject.name = modName
