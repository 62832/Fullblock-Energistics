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
            property("fabric-api.datagen.modid", rootProject.property("modId").toString())
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
    modRuntimeOnly("curse.maven:merequester-688367:${property("requesterFileId")}")
    modRuntimeOnly("maven.modrinth:midnightlib:0.6.0")
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }

    remapJar {
        injectAccessWidener.set(true)
    }
}
