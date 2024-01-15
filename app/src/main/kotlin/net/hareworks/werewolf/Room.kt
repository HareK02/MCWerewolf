package net.hareworks.werewolf

import net.hareworks.werewolf.game.Instance
import org.bukkit.entity.Player


public class Room {
  var Game: Instance = Instance()
  var roomname: String = ""
	var inGame: Boolean = false
	var isPrivate: Boolean = false
  constructor(creator: Player, name: String) {
    this.players.add(creator)
    this.roomname = name
  }
  public var players: MutableList<Player> = mutableListOf()

  public fun start() {}
}
