architectury {
    common(property("enabled_platforms").toString().split(','))
}

loom {
    accessWidenerPath.set(file("src/main/resources/${property("mod_id")}.accesswidener"))
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")

    modCompileOnly("appeng:appliedenergistics2-fabric:${property("ae2_version")}")
    modCompileOnly("curse.maven:merequester-688367:${project(":fabric").dependencyProject.property("requester_fileid")}")
}
