package net.hareworks.werewolf

import java.io.File
import net.hareworks.werewolf.book.GameMenu
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

public class MCWerewolf : JavaPlugin() {
  companion object {
    lateinit var instance: MCWerewolf
      private set
  }
  public var rooms: MutableList<Room> = mutableListOf()
  public var defaultConfig: YamlConfiguration = YamlConfiguration()
  public var langConfig: YamlConfiguration = YamlConfiguration()
  override fun onEnable() {
    instance = this
    InitConfig()
    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord")
    getCommand("wbook")?.setExecutor(Wbook())
    getCommand("werewolf")?.setExecutor(Werewolf())
    getCommand("werewolf&")?.setExecutor(Werewolf())
    getServer().getPluginManager().registerEvents(EventListener(), this)

    this.logger.info(defaultConfig.getConfigurationSection("roles")!!.getKeys(false).toString())
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
}

fun logger(): java.util.logging.Logger {
  return MCWerewolf.instance.logger
}

fun debuglog(msg: String) {
  logger().info("[DEBUG] $msg")
}
