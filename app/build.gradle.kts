import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

group = "net.hareworks"
version = "1.0"

bukkit {
    main = "net.hareworks.werewolf.App"
    name = "werewolf"
    description = "just server command to send player to bungee server"
    version = getVersion().toString()
		apiVersion = "1.20"
    authors =
            listOf(
                "Hare-K02",
            )
    commands {
				register("wbook") {
						description = "get book"
						usage = "/wbook"
						permission = "werewolf.player"
						permissionMessage = "You don't have permission to run this command"
				}
        register("werewolf") {
            description = "Manage the game"
            usage = "/werewolf <subcommand>"
            permission = "werewolf.gamemaster"
            permissionMessage = "You don't have permission to run this command"
        }
    }
    permissions {
        register("werewolf.gamemaster") {
            description = "allow player to use gamemaster command"
            default = BukkitPluginDescription.Permission.Default.TRUE // TRUE, FALSE, OP or NOT_OP
        }
				register("werewolf.player") {
						description = "allow player to use player command"
						default = BukkitPluginDescription.Permission.Default.TRUE // TRUE, FALSE, OP or NOT_OP
				}
		}
}

plugins {
    kotlin("jvm") version "1.9.22"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}
repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}
dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("net.kyori:adventure-api:4.14.0")
}
tasks {
    shadowJar {
        archiveBaseName.set(bukkit.name)
        archiveClassifier.set("")
    }
}