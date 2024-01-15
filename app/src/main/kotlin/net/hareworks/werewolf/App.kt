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
  override fun onEnable() {
    plugin = this
    InitConfig()
    this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord")
    this.getCommand("werewolf")?.setExecutor(Command())
  }
  private fun InitConfig() {
    var configFile = File(getDataFolder(), "game-settings/default.yml")
    if (!configFile.exists()) {
      configFile.getParentFile().mkdirs()
      saveResource("game-settings/default.yml", false)
    }
    defaultConfig = YamlConfiguration()
    try {
      defaultConfig.load(configFile)
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
