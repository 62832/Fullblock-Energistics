val forgeVersion: String by extra
val ae2Version: String by extra

dependencies {
    val minecraftVersion: String by project

    forge("net.minecraftforge:forge:$minecraftVersion-$forgeVersion")

    modImplementation("appeng:appliedenergistics2-forge:$ae2Version")
    modRuntimeOnly("maven.modrinth:merequester:$minecraftVersion-${property("requesterVersion")}+forge")
}

tasks.processResources {
    val forgeProps = mapOf(
            "loaderVersion" to forgeVersion.substringBefore('.'),
            "ae2VersionEnd" to ae2Version.substringBefore('.').toInt() + 1
    )

    inputs.properties(forgeProps)

    filesMatching("META-INF/mods.toml") {
        val commonProps: Map<String, *> by extra
        expand(commonProps + forgeProps)
    }
}
