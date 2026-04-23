import xyz.jpenilla.resourcefactory.bukkit.Permission.Default
import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml.Load

plugins {
    id("java")
    id("checkstyle")
    id("com.diffplug.spotless") version "8.0.0"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.1"
    id("com.gradleup.shadow") version "9.2.2"
}

group = "io.github.namiuni"
version = "1.0.3"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

checkstyle {
    configDirectory = rootProject.file(".checkstyle")
}

spotless {
    java {
        licenseHeaderFile(rootProject.file("LICENSE_HEADER"))
    }
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    implementation("org.spongepowered:configurate-hocon:4.2.0")
    compileOnly("org.spongepowered:configurate-yaml:4.2.0")
    implementation("net.kyori:adventure-serializer-configurate4:4.26.1")

    // Quick Shop
    compileOnly("com.ghostchu:simplereloadlib:1.1.2")
    compileOnly("com.ghostchu:quickshop-api:6.2.0.11")
    compileOnly("com.ghostchu:quickshop-bukkit:6.2.0.11")

    // Integrations
    compileOnly("io.github.miniplaceholders:miniplaceholders-api:3.1.0")

    // Misc
    implementation("com.github.sviperll:result4j:1.2.0")
}

val mainPackage = "${group}.qshdialog.minecraft.paper"
paperPluginYaml {
    name = "QuickShop-Hikari-Dialog"
    author = "Namiu (うにたろう)"
    website = "https://github.com/NamiUni"
    apiVersion = "1.21.10"

    main = "$mainPackage.QSHDialogPlugin"
    bootstrapper = "$mainPackage.QSHDialogBootstrap"
//    loader = "$mainPackage."

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
        minecraftVersion("1.21.11")
        downloadPlugins {
            modrinth("luckperms", "v5.5.17-bukkit")
            modrinth("miniplaceholders", "4zOT6txC")
            modrinth("quickshop-hikari", "6.2.0.11")
            url("https://github.com/dmulloy2/ProtocolLib/releases/download/dev-build/ProtocolLib.jar")
            url("https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar")
            url("https://ci.minebench.de/job/craftconomy3/lastSuccessfulBuild/artifact/build/libs/CraftConomy3.jar")
        }
    }
}
