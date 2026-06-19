import xyz.jpenilla.resourcefactory.bukkit.Permission.Default
import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml.Load

plugins {
    id("java")
    id("checkstyle")
    alias(libs.plugins.shadow)
    alias(libs.plugins.gremlin.gradle)
    alias(libs.plugins.spotless)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.resource.factory)
}

group = "io.github.namiuni"
version = "2.0.0"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

checkstyle {
    toolVersion = libs.versions.checkstyle.get()
    configDirectory = rootProject.file(".checkstyle")
}

spotless {
    java {
        licenseHeaderFile(rootProject.file("LICENSE_HEADER"))
    }
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.configurate.yaml)
    runtimeDownload(libs.configurate.hocon)
    // Bundled kotonoha is compiled against geantyref 2.0.1, but it resolves geantyref
    // from the library loader (excluded from its bundle below to keep one copy). Pin
    // 2.0.1 here so the shared copy satisfies kotonoha as well as Configurate.
    runtimeDownload("io.leangen.geantyref:geantyref:2.0.1")

    // Gremlin's runtime-downloaded jars are loaded by Paper's isolated library class
    // loader, which CANNOT see Paper's bundled Adventure. Libraries that reference
    // Adventure are therefore bundled into the plugin jar instead, so they are loaded
    // by the plugin class loader (which can see both Paper's Adventure and the library
    // loader). Adventure/MiniMessage are excluded so they resolve to Paper's single
    // copy. geantyref carries no Adventure references and stays in the library loader;
    // excluding it here keeps a single shared copy there.
    implementation(libs.kotonoha.message) {
        exclude(group = "net.kyori")            // Adventure/MiniMessage -> Paper
        exclude(group = "io.leangen.geantyref") // single copy stays in library loader
    }
    implementation(libs.kotonoha.message.extra.miniplaceholders) {
        exclude(group = "net.kyori")            // Adventure/MiniMessage -> Paper
    }

    // The default Guice jar bundles a shaded ASM that cannot read Java 25 bytecode
    // (Unsupported class file major version 69). The "classes" variant is unshaded, so
    // pair it with a current ASM that supports Java 25. Downloaded at runtime; Gremlin
    // preserves the classifier in the generated dependency list.
    runtimeDownload(variantOf(libs.guice) { classifier("classes") })
    runtimeDownload(libs.asm)

    // Quick Shop
    compileOnly(libs.simplereloadlib)
    compileOnly(libs.quickshop.api)
    compileOnly(libs.quickshop.bukkit)

    // Integrations
    compileOnly(libs.mini.placeholders)

    // Misc
    implementation(libs.result4j)
    implementation(libs.bstats.bukkit)

    annotationProcessor(libs.kotonoha.resourcebundle.generator.processor)
}

configurations.compileOnly {
    extendsFrom(configurations.runtimeDownload.get())
}

val mainPackage = "${group}.qshdialog.minecraft.paper"
paperPluginYaml {
    name = "QuickShop-Hikari-Dialog"
    author = "Namiu (うにたろう)"
    website = "https://github.com/NamiUni"
    apiVersion = "1.21.11"

    main = "$mainPackage.QSHDialogPlugin"
    bootstrapper = "$mainPackage.QSHDialogBootstrap"
    loader = "$mainPackage.QSHDialogLoader"

    permissions {
        register("qshdialog.command.admin.reload") {
            description = "Reloads QuickShop-Hikari-Dialog's config and translations."
            default = Default.OP
        }
        register("qshdialog.command.shopdialog.create") {
            description = "Use the dialog to create a new shop."
            default = Default.TRUE
        }
        register("qshdialog.command.shopdialog.modify") {
            description = "Use the dialog to modify an existing shop."
            default = Default.TRUE
        }
        register("qshdialog.command.shopdialog.trade") {
            description = "Use the dialog to trade at a shop."
            default = Default.TRUE
        }
    }

    dependencies {
        bootstrap("MiniPlaceholders", Load.BEFORE, false)
        server("PlaceholderAPI", Load.BEFORE, false)
        server("QuickShop-Hikari", Load.BEFORE, true)
        server("MiniPlaceholders", Load.BEFORE, false)
    }
}

tasks {
    shadowJar {
        archiveBaseName = paperPluginYaml.name
        archiveClassifier = null as String?

        gremlin {
            listOf("xyz.jpenilla.gremlin")
                .forEach {
                    relocate(it, "libraries.$it")
                }
        }

        relocate("org.bstats", "libraries.org.bstats")
        relocate("com.github.sviperll", "libraries.com.github.sviperll")
    }

    runServer {
        minecraftVersion("26.1.2")
        systemProperty("log4j.configurationFile", "log4j2.xml")
        jvmArgs("--sun-misc-unsafe-memory-access=allow")
        downloadPlugins {
            modrinth("luckperms", "v5.5.17-bukkit")
            modrinth("miniplaceholders", "4zOT6txC")
            modrinth("quickshop-hikari", libs.versions.quickshop.get())
            url("https://github.com/dmulloy2/ProtocolLib/releases/download/dev-build/ProtocolLib.jar")
            url("https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar")
            url("https://ci.minebench.de/job/craftconomy3/lastSuccessfulBuild/artifact/build/libs/CraftConomy3.jar")
        }
    }

    compileJava {
        options.compilerArgs.add("-parameters")
    }
}
