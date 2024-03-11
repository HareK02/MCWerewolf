package net.hareworks.werewolf

import java.io.File
import net.hareworks.kommandlib.KommandLib
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

public class MCWerewolf : JavaPlugin() {
  companion object {
    lateinit var instance: MCWerewolf
      private set
  }
  lateinit var kommand: KommandLib
  public var sessionList: MutableList<Session> = mutableListOf()
  public var defaultConfig: YamlConfiguration = YamlConfiguration()
  public var langConfig: YamlConfiguration = YamlConfiguration()

  override fun onEnable() {
    instance = this

    kommand = command()

    debuglog(getServer().getPluginCommand("ww").toString())
    InitConfig()
    getServer().getPluginManager().registerEvents(EventListener(), this)

    this.logger.info(defaultConfig.getConfigurationSection("roles")!!.getKeys(false).toString())
  }

  override fun onDisable() {
    kommand.unregister()
    sessionList.forEach { it.forceEnd() }
  }

  private fun InitConfig() {
    var defaultGameSettingsFile = File(getDataFolder(), "game-settings/default.yml")
    if (!defaultGameSettingsFile.exists()) {
      defaultGameSettingsFile.getParentFile().mkdirs()
      saveResource("game-settings/default.yml", false)
    }
    defaultConfig = YamlConfiguration()
    try {
      defaultConfig.load(defaultGameSettingsFile)
    } catch (e: Exception) {
      e.printStackTrace()
    }

    var langFile = File(getDataFolder(), "lang.yml")
    if (!langFile.exists()) {
      langFile.getParentFile().mkdirs()
      saveResource("lang.yml", false)
    }
    langConfig = YamlConfiguration()
    try {
      langConfig.load(langFile)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun newRoom(player: Player, name: String): Boolean {
    if (getRoom(name) != null) return false
    sessionList.add(Session(player, name))
    return true
  }

  fun deleteRoom(room: Session) {
    sessionList.remove(room)
  }

  fun getRoom(name: String): Session? {
    return this.sessionList.find { room -> room.roomname == name }
  }

  fun getRoom(player: Player): Session? {
    return this.sessionList.find { room -> room.players.contains(player) }
        ?: this.sessionList.find { it.players.find { it.player?.uniqueId == player.uniqueId } != null }
  }

  fun hasRoom(player: Player): Boolean {
    return this.sessionList.any { room -> room.players.contains(player) }
  }

  fun runTaskLater(ticks: Long, task: () -> Unit) {
    getServer().getScheduler().runTaskLater(this, task, ticks)
  }
}

abstract interface Broadcaster {
  val targets: List<Audience>

  public fun broadcast(message: String) {
    for (p in this.targets) {
      p.sendMessage(Component.text(message))
    }
  }
  public fun broadcast(message: Component) {
    for (p in this.targets) {
      p.sendMessage(message)
    }
  }
  public fun broadcastTitle(title: Title) {
    for (p in this.targets) {
      p.showTitle(title)
    }
  }
  public fun broadcastSound(sound: Sound) {
    for (p in this.targets) {
      p.playSound(sound, Sound.Emitter.self())
    }
  }
}

fun logger(): java.util.logging.Logger {
  return MCWerewolf.instance.logger
}

fun debuglog(msg: String) {
  logger().info("[DEBUG] $msg")
}
