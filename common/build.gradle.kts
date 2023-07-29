architectury {
    val platforms: List<String> by rootProject.extra
    println("Platforms: $platforms")
    common(platforms)
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${property("fabricLoaderVersion")}")
    modCompileOnly("net.fabricmc.fabric-api:fabric-api:${project(":fabric").dependencyProject.property("fabricApiVersion")}+${property("minecraftVersion")}")

    modCompileOnly("appeng:appliedenergistics2-fabric:${property("ae2Version")}")
    modCompileOnly("curse.maven:merequester-688367:${project(":fabric").dependencyProject.property("requesterFileId")}")
}

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")
            exclude("**/.cache")
        }
    }
}
