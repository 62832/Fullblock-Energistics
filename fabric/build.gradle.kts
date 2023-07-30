loom {
    runs {
        create("data") {
            client()
            name("Minecraft Data")

            source(sourceSets.create("data") {
                val main = sourceSets.main.get()
                compileClasspath += main.compileClasspath + main.output
                runtimeClasspath += main.runtimeClasspath + main.output
            })

            property("fabric-api.datagen")
            property("fabric-api.datagen.modid", property("modId").toString())
            property("fabric-api.datagen.output-dir", project(":common").file("src/generated/resources").absolutePath)
            property("fabric-api.datagen.strict-validation")
        }
    }
}

dependencies {
    val minecraftVersion: String by project

    modImplementation("net.fabricmc:fabric-loader:${property("fabricLoaderVersion")}")
    modApi("net.fabricmc.fabric-api:fabric-api:${property("fabricApiVersion")}+$minecraftVersion")

    modImplementation("appeng:appliedenergistics2-fabric:${property("ae2Version")}")
    modRuntimeOnly("maven.modrinth:merequester:$minecraftVersion-${property("requesterVersion")}+fabric")
    modRuntimeOnly("maven.modrinth:midnightlib:${property("midnightLibVersion")}-fabric")
}

tasks.processResources {
    filesMatching("fabric.mod.json") {
        val commonProps: Map<String, *> by extra
        expand(commonProps)
    }
}
