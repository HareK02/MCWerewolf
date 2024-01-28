package net.hareworks.werewolf.game

import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.Room
import net.hareworks.werewolf.assign
import net.hareworks.werewolf.game.scenario.Scenario

public enum class GameStatus {
  Lobby,
  Day,
  Night,
  End
}

public class Game {
  val players: MutableList<Player> = mutableListOf()
  val spectators: MutableList<Player> = mutableListOf()
  val config: Config
  var status: GameStatus = GameStatus.Lobby

  var scenario: Scenario
  val time: Int = 0

  constructor(room: Room) {
    this.config = room.config
    this.scenario = Scenario.load(this.config.scenario, this)
  }

  public fun reset() {
    this.scenario = Scenario.load(this.config.scenario, this)
  }

  public fun start() {
    for (player in players) {
      player.alive = true
      player.disconnected = false
    }
    status = GameStatus.Day
    this.scenario.onGameStart()
  }

  public fun end() {

    this.scenario.onGameEnd()
  }

  public fun getGamePlayer(player: org.bukkit.entity.Player): Player? {
    return players.find { it.playerEntity == player }
  }

  public fun broadcast(message: String) {
    for (player in players) {
      player.sendMessage(message)
    }
  }

  public fun onPlayerDisconnect(player: org.bukkit.entity.Player) {
    val pl = players.find { it.playerEntity == player } ?: return
    pl.disconnected = true
    if (config.allowRejoin) {
      broadcast(Lang.get("game.player-disconnected").assign(pl.playerEntity.name))
      if (config.createLeavingDummy) {
        // TODO
      }
    } else {}
  }

  public fun onPlayerReconnect(player: org.bukkit.entity.Player) {
    val i = players.indexOf(players.find { it.playerEntity.uniqueId == player.uniqueId })
    players[i].playerEntity = player
    players[i].disconnected = false
    if (config.allowRejoin) {
      players[i].alive = true
      if (config.createLeavingDummy) {
        // TODO
      }
    }
  }

  private fun onGameStart() {
    scenario.onGameStart()
    broadcast("Game started.")
  }

  private fun onGameEnd() {
    scenario.onGameEnd()
    broadcast("Game ended.")
  }

  private fun onDayEnd() {
    scenario.onDayEnd()
    broadcast("Day ended.")
  }

  private fun onNightEnd() {
    scenario.onNightEnd()
    broadcast("Night ended.")
  }

  private fun onPlayerDeath() {
    scenario.onPlayerDeath()
    broadcast("Player died.")
  }
}
