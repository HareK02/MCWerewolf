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