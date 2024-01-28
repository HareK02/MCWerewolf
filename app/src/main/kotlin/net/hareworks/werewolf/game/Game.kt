package net.hareworks.werewolf.game

import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.MCWerewolf
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

  public fun broadcast(message: String) {
    for (player in players) {
      player.sendMessage(message)
    }
  }

  public fun openBook(player: org.bukkit.entity.Player) {
    players.find { it.player == player }?.openBook()
  }

  public fun onPlayerDisconnect(player: org.bukkit.entity.Player) {
    val pl = players.find { it.player == player } ?: return
    pl.disconnected = true
    if (config.allowRejoin) {
      broadcast(Lang.get("game.player-disconnected").assign(pl.player.name))
      if (config.createLeavingDummy) {
        // TODO
      }
    } else {}
  }

  public fun onPlayerRejoin(player: org.bukkit.entity.Player) {
    val pl = players.find { it.player == player } ?: return
    pl.disconnected = false
    if (config.allowRejoin) {
      pl.alive = true
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
