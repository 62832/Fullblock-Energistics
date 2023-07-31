loom {
    forge {
        convertAccessWideners.set(true)
        extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
    }
}

dependencies {
    forge(libs.forge)

    modImplementation(libs.ae2.forge)
    modRuntimeOnly(libs.requester.forge)
}

tasks.processResources {
    val forgeProps = mapOf(
            "loaderVersion" to libs.forge.get().version!!.substringAfter('-').substringBefore('.'),
            "ae2VersionEnd" to libs.versions.ae2.get().substringBefore('.').toInt() + 1
    )

    inputs.properties(forgeProps)

    filesMatching("META-INF/mods.toml") {
        val commonProps: Map<String, *> by extra
        expand(commonProps + forgeProps)
    }
}
