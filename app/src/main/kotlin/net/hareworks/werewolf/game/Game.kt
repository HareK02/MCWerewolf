package net.hareworks.werewolf.game

import net.hareworks.werewolf.App
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

public class Instance {
  public var players: MutableList<Player> = mutableListOf()
  public var config: YamlConfiguration = YamlConfiguration()
  public var day: Int = 0

  init {
    config.loadFromString(App.plugin.defaultConfig.saveToString())
  }

  public fun start() {
    for (player in players) {
      player.sendMessage("Game started.")
    }
  }
}
