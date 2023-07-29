loom {
    val modId: String by project

    forge {
        convertAccessWideners.set(true)
        extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
    }
}

dependencies {
    forge("net.minecraftforge:forge:${property("minecraftVersion")}-43.1.65")

    modImplementation("appeng:appliedenergistics2-forge:${property("ae2Version")}")
    modRuntimeOnly("curse.maven:merequester-688367:4094402")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("META-INF/mods.toml") {
        expand("version" to project.version)
    }
}
