architectury {
    val platforms: List<String> by rootProject.extra
    println("Platforms: $platforms")
    common(platforms)
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${project(":fabric").dependencyProject.property("fabricLoaderVersion")}")
    modCompileOnly("net.fabricmc.fabric-api:fabric-api:${project(":fabric").dependencyProject.property("fabricApiVersion")}+${property("minecraftVersion")}")

    modCompileOnly("appeng:appliedenergistics2-fabric:${property("ae2Version")}")
    modCompileOnly("maven.modrinth:merequester:${property("minecraftVersion")}-${property("requesterVersion")}+fabric")
}

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")
            exclude("**/.cache")
        }
    }
}
