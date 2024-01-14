package net.hareworks.werewolf

import org.bukkit.entity.Player

public class Game {
  init {
    App.plugin.rooms.add(this)
  }
  var roomname: String = ""
  constructor(creator: Player, name: String) {
    this.players.add(creator)
    this.roomname = name
  }
  enum class State {
    Day,
    Night
  }
  public var players: MutableList<Player> = mutableListOf()
  public var day: Int = 0
  public var state: State = State.Day

  public fun start() {
    this.state = State.Night
    this.day = 1
    this.players = App.plugin.getServer().getOnlinePlayers().toMutableList()
    this.players.shuffle()
    this.players.forEach { player -> player.sendMessage("You are a werewolf!") }
  }
}
