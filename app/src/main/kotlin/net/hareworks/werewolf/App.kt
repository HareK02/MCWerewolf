package net.hareworks.werewolf

import java.io.File
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

public class App : JavaPlugin() {
  companion object {
    lateinit var plugin: App
      private set
  }
  public var rooms: MutableList<Room> = mutableListOf()
  public var defaultConfig: YamlConfiguration = YamlConfiguration()
	public var langConfig: YamlConfiguration = YamlConfiguration()
  override fun onEnable() {
    plugin = this
    InitConfig()
    this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord")
    this.getCommand("wbook")?.setExecutor(Wbook())
    this.getCommand("werewolf")?.setExecutor(Werewolf())
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
    var langConfig = YamlConfiguration()
    try {
      langConfig.load(langFile)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun getRoom(name: String): Room? {
    return this.rooms.find { room -> room.roomname == name }
  }

  fun hasRoom(player: Player): Boolean {
    return this.rooms.any { room -> room.players.contains(player) }
  }
}
