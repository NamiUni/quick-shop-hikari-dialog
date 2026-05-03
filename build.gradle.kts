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
version = "1.0.6"

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
    runtimeDownload(libs.guice)
    runtimeDownload(libs.configurate.hocon)
    runtimeDownload(libs.adventure.serializer.configurate)
    runtimeDownload(libs.kotonoha.message)
    runtimeDownload(libs.kotonoha.message.extra.miniplaceholders)

    // Quick Shop
    compileOnly(libs.simplereloadlib)
    compileOnly(libs.quickshop.api)
    compileOnly(libs.quickshop.bukkit)

    // Integrations
    compileOnly(libs.mini.placeholders)

    // Misc
    implementation(libs.result4j)
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
    }

    runServer {
        minecraftVersion("26.1.2")
        systemProperty("log4j.configurationFile", "log4j2.xml")
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
