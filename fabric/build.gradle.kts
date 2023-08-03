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
    modImplementation(libs.fabric.loader)
    modApi(libs.fabric.api)

    modImplementation(libs.ae2.fabric)
    modRuntimeOnly(libs.requester.fabric)
    modRuntimeOnly(libs.midnightLib)

    // modRuntimeOnly(libs.jade.fabric)
    modRuntimeOnly(libs.wthit.fabric)
    modRuntimeOnly(libs.badpackets.fabric)
}

tasks {
    processResources {
        filesMatching("fabric.mod.json") {
            val commonProps: Map<String, *> by extra
            expand(commonProps)
        }
    }

    remapJar {
        injectAccessWidener.set(true)
    }
}
