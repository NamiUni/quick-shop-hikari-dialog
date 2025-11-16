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
version = "1.0-SNAPSHOT"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
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
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    compileOnly("org.spongepowered:configurate-yaml:4.2.0")
    implementation("net.kyori:adventure-serializer-configurate4:4.25.0")
    implementation("com.github.sviperll:result4j:1.2.0")

    // Quick Shop
    compileOnly("com.ghostchu:simplereloadlib:1.1.2")
    compileOnly("com.ghostchu:quickshop-api:6.2.0.11-SNAPSHOT-9")
    compileOnly("com.ghostchu:quickshop-bukkit:6.2.0.11-SNAPSHOT-9")
}

val mainPackage = "${group}.qshdialog"
paperPluginYaml {
    name = "QuickShop-Hikari-Dialog"
    author = "Namiu (うにたろう)"
    website = "https://github.com/NamiUni"
    apiVersion = "1.21.10"

    main = "$mainPackage.QSHDialogPlugin"
    bootstrapper = "$mainPackage.QSHDialogBootstrap"
//    loader = "$mainPackage."

    permissions {
        register("qshdialog.reload") {
            description = "Reloads QuickShop-Hikari-Dialog's config and translations."
            default = Default.OP
        }
    }

    dependencies {
        server("QuickShop-Hikari", Load.BEFORE, true)
    }
}

tasks {
    runServer {
        minecraftVersion("1.21.10")
        downloadPlugins {
            modrinth("luckperms", "v5.5.17-bukkit")
            modrinth("packetevents", "2.10.1")
            url("https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar")
            url("https://ci.minebench.de/job/FakeEconomy/lastSuccessfulBuild/artifact/target/FakeEconomy.jar")
            url("https://ci.codemc.io/job/Ghost-chu/job/QuickShop-Hikari-SNAPSHOT/lastBuild/com.ghostchu\$quickshop-bukkit/artifact/com.ghostchu/quickshop-bukkit/6.2.0.11-SNAPSHOT-9/quickshop-bukkit-6.2.0.11-SNAPSHOT-9-shaded.jar")
        }
    }
}
