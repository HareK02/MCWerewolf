import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

group = "net.hareworks"

version = "1.0"

bukkit {
  main = "net.hareworks.werewolf.MCWerewolf"
  name = "werewolf"
  description = "minecraft werewolf game plugin"
  version = getVersion().toString()
  apiVersion = "1.20"
  authors =
      listOf(
          "Hare-K02",
      )
  commands {
    // register("wbook") {
    //   description = "open and operate book"
    //   usage = "/wbook"
    //   permission = "werewolf.player"
    //   permissionMessage = "You don't have permission to run this command"
    // }
    // register("ww") {
    //   description = "Manage the game"
    //   usage = "/ww <subcommand>"
    //   permission = "werewolf.player"
    //   permissionMessage = "You don't have permission to run this command"
    // }
    // register("werewolf") {
    //   description = "Manage the game"
    //   usage = "/werewolf <subcommand>"
    //   permission = "werewolf.player"
    //   permissionMessage = "You don't have permission to run this command"
    // }
    // register("werewolf&") {
    //   description = "run command and open book"
    //   usage = "/werewolf& <subcommand>"
    //   permission = "werewolf.player"
    //   permissionMessage = "You don't have permission to run this command"
    // }
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
  compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
  implementation("net.kyori:adventure-api:4.14.0")
}

tasks {
  shadowJar {
    archiveBaseName.set(bukkit.name)
    archiveClassifier.set("")
  }
}

tasks {
    shadowJar {
        archiveBaseName.set(bukkit.name)
        archiveClassifier.set("")
    }
}