package net.hareworks.werewolf

import java.io.File
import kotlin.collections.mutableMapOf
import net.hareworks.kommandlib.KommandLib
import net.hareworks.werewolf.gui.book.GameMenu
import net.hareworks.werewolf.gui.inventory.InventoryGUIListener
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.bossbar.BossBar
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
  public var rooms: MutableList<Room> = mutableListOf()
  public var defaultConfig: YamlConfiguration = YamlConfiguration()
  public var langConfig: YamlConfiguration = YamlConfiguration()

  override fun onEnable() {
    instance = this

    kommand = command()

    debuglog(getServer().getPluginCommand("ww").toString())
    InitConfig()
    getServer().getPluginManager().registerEvents(EventListener(), this)
    getServer().getPluginManager().registerEvents(InventoryGUIListener(), this)

    this.logger.info(defaultConfig.getConfigurationSection("roles")!!.getKeys(false).toString())
  }

  override fun onDisable() {
    kommand.unregister()
    rooms.forEach { it.forceend() }
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
    rooms.add(Room(player, name))
    GameMenu.build()
    return true
  }

  fun deleteRoom(room: Room) {
    rooms.remove(room)
    GameMenu.build()
  }

  fun getRoom(name: String): Room? {
    return this.rooms.find { room -> room.roomname == name }
  }

  fun getRoom(player: Player): Room? {
    return this.rooms.find { room -> room.players.contains(player) }
        ?: this.rooms.find { it.players.find { it.player?.uniqueId == player.uniqueId } != null }
  }

  fun hasRoom(player: Player): Boolean {
    return this.rooms.any { room -> room.players.contains(player) }
  }

  fun runTaskLater(ticks: Long, task: () -> Unit) {
    getServer().getScheduler().runTaskLater(this, task, ticks)
  }
}

abstract interface Broadcaster {
  val players: List<Audience>

  public fun broadcast(message: String) {
    for (p in this.players) {
      p.sendMessage(Component.text(message))
    }
  }
  public fun broadcast(message: Component) {
    for (p in this.players) {
      p.sendMessage(message)
    }
  }
  public fun broadcastTitle(title: Title) {
    for (p in this.players) {
      p.showTitle(title)
    }
  }
  public fun broadcastSound(sound: Sound) {
    for (p in this.players) {
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
