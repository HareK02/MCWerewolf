package net.hareworks.werewolf

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.entity.Player

public class App : JavaPlugin() {
  companion object {
    lateinit var plugin: App
      private set
  }
  public var rooms: MutableList<Game> = mutableListOf()

  override fun onEnable() {
    plugin = this
    // saveDefaultConfig()
    this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord")
    this.getCommand("werewolf")?.setExecutor(Command())
  }

	fun getRoom(name: String): Game? {
		return this.rooms.find { room -> room.roomname == name }
	}

	fun hasRoom(player: Player): Boolean {
		return this.rooms.any { room -> room.players.contains(player) }
	}
}
