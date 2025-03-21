plugins {
    id("net.neoforged.moddev")
    id("com.diffplug.spotless")
}

val modId = "fulleng"

base.archivesName = modId
version = if (System.getenv("GITHUB_REF_TYPE") == "tag") System.getenv("GITHUB_REF_NAME") else "0.0.0"
group = "gripe.90"

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

dependencies {
    api(libs.ae2)
    implementation(libs.requester)

    implementation(libs.extendedae)
    implementation(libs.glodium)

    implementation(libs.appliede)
    runtimeOnly(libs.projecte)

    runtimeOnly(libs.jade)
}

sourceSets {
    main {
        resources.srcDir(file("src/generated/resources"))
    }

    create("data") {
        val main = main.get()
        compileClasspath += main.compileClasspath + main.output
        runtimeClasspath += main.runtimeClasspath + main.output
    }
}

neoForge {
    version = libs.versions.neoforge.get()
    validateAccessTransformers = true

    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.getByName("data"))
        }
    }

    runs {
        configureEach {
            gameDirectory = file("run")
        }

        create("client") {
            client()
        }

        create("server") {
            server()
            gameDirectory = file("run/server")
        }

        create("data") {
            data()
            programArguments.addAll(
                "--mod", modId,
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath,
                "--existing-mod", "ae2",
                "--existing-mod", "merequester",
                "--existing-mod", "extendedae",
                "--existing-mod", "appliede"
            )
            sourceSet = sourceSets.getByName("data")
        }
    }
}

tasks {
    jar {
        from(rootProject.file("LICENSE"))
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    processResources {
        exclude("**/.cache")

        val props = mapOf(
            "version" to version,
            "ae2Version" to libs.versions.ae2.get(),
            "requesterVersion" to libs.versions.requester.get(),
            "extendedAeVersion" to libs.versions.extendedae.get()
        )

        inputs.properties(props)
        filesMatching("META-INF/neoforge.mods.toml") {
            expand(props)
        }
    }
}

spotless {
    kotlinGradle {
        target("*.kts")
        diktat()
    }

    java {
        target("/src/**/java/**/*.java")
        endWithNewline()
        indentWithSpaces(4)
        removeUnusedImports()
        palantirJavaFormat()
        importOrderFile(file("mega.importorder"))
        toggleOffOn()

        // courtesy of diffplug/spotless#240
        // https://github.com/diffplug/spotless/issues/240#issuecomment-385206606
        custom("noWildcardImports") {
            if (it.contains("*;\n")) {
                throw Error("No wildcard imports allowed")
            }

            it
        }

        bumpThisNumberIfACustomStepChanges(1)
    }

    json {
        target("src/**/resources/**/*.json")
        targetExclude("src/generated/resources/**")
        biome()
        indentWithSpaces(2)
        endWithNewline()
    }
}
