package net.hareworks.werewolf.game

import net.hareworks.werewolf.Broadcaster
import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.MCWerewolf
import net.hareworks.werewolf.Room
import net.hareworks.werewolf.assign
import net.hareworks.werewolf.debuglog
import net.hareworks.werewolf.game.role.Role
import net.hareworks.werewolf.game.role.RoleObject
import net.hareworks.werewolf.game.scenario.Scenario
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.bukkit.scheduler.BukkitRunnable

enum class Winner {
  Citizens,
  Werewolves,
  None,
}

public class Game(room: Room) : Broadcaster {
  val config: Config = room.config
  var scenario: Scenario = Scenario.load(config.scenario, this)
  override val players: MutableList<Player> = mutableListOf()
  val spectators: MutableList<Player> = mutableListOf()
  public enum class Time {
    Day,
    Night,
  }
  var time: Int = 0
  var timeState: Time = Time.Day
  val day: Int
    get() = time / (config.dayLength + config.nightLength) + 1

  private var isPlayerDeath: Boolean = false
  private val timerbar =
      BossBar.bossBar(Component.text("Day"), 0.0f, BossBar.Color.YELLOW, BossBar.Overlay.NOTCHED_10)
  private var ticker: BukkitRunnable =
      object : BukkitRunnable() {
        override fun run() {
          time++
          when (time % (config.dayLength + config.nightLength)) {
            0 -> onNightEnd()
            config.dayLength -> onDayEnd()
          }
          if (timeState == Time.Day) {
            timerbar.name(
                Component.text(
                    "Day $day, daytime: " +
                        ((config.dayLength - time % config.dayLength) / 20) +
                        "s"
                )
            )
            timerbar.progress(
                (config.dayLength - time % config.dayLength).toFloat() / config.dayLength
            )
          } else {
            timerbar.name(
                Component.text(
                    " Day $day, night time: " +
                        ((config.nightLength - time % config.nightLength) / 20) +
                        "s"
                )
            )
            timerbar.progress(
                (config.nightLength - time % config.nightLength).toFloat() / config.nightLength
            )
          }

          players.forEach {
            it.setPlayerTime(
                (if (timeState == Time.Day)
                        (time.toFloat() % (config.dayLength + config.nightLength) /
                            config.dayLength * 13000)
                    else
                        ((time.toFloat() % (config.dayLength + config.nightLength) /
                            config.nightLength) * 10000) + 10000)
                    .toLong(),
                false
            )
          }

          if (isPlayerDeath) {
            val citizens = players.filter { it.role.meta.type == RoleObject.Type.Citizen }.count()
            val werewolves =
                players.filter { it.role.meta.type == RoleObject.Type.Werewolf }.count()

            if (citizens == 0 || werewolves == 0) {
              
            }

            isPlayerDeath = false
          }
        }
      }

  init {
    val roles: HashMap<String, Int> =
        hashMapOf<String, Int>().apply { config.roles.forEach { this[it.name] = it.amount } }
    debuglog("Game: Starting game with ${room.players.size} players and $roles")
    while (roles.any { it.value > 0 } || players.size < room.players.size) {
      val roleIndex = (0 until roles.size).random()
      val role = roles.keys.toList()[roleIndex]
      val instance = Role.roles[role]?.instantiate() ?: continue
      val players = room.players.filter { p -> players.none { it.playerEntity == p } }
      val playerIndex = (0 until players.size).random()
      debuglog("Game: Assigning ${players[playerIndex].name} to $role")
      this.players.add(Player(players[playerIndex], instance))
      roles[role] = roles[role]!! - 1
      if (roles[role] == 0) roles.remove(role)
    }
  }

  public fun start() {
    this.onGameStart()
  }

  public fun forceend() {
    this.onGameEnd()
  }

  public fun gameEnd(winner: Winner) {
    this.onGameEnd()
    when (winner) {
      Winner.Citizens -> broadcast(Lang.get("game.winner-citizens"))
      Winner.Werewolves -> broadcast(Lang.get("game.winner-werewolves"))
      Winner.None -> broadcast(Lang.get("game.winner-none"))
    }
    broadcastSound(
        net.kyori.adventure.sound.Sound.sound(
            net.kyori.adventure.key.Key.key("ui.toast.challenge_complete"),
            net.kyori.adventure.sound.Sound.Source.MASTER,
            0.6f,
            0.9f
        )
    )
  }

  public fun getGamePlayer(player: org.bukkit.entity.Player): Player? {
    return players.find { it.playerEntity == player }
  }

  public fun onPlayerDisconnect(player: org.bukkit.entity.Player) {
    val pl = players.find { it.playerEntity == player } ?: return
    pl.disconnected = true
    if (config.allowRejoin) {
      broadcast(Lang.get("game.disconnected").assign(pl.playerEntity.name))
      if (config.createLeavingDummy) {
        // TODO
      }
    } else {}
  }

  public fun onPlayerReconnect(player: org.bukkit.entity.Player) {
    val i = players.indexOf(players.find { it.playerEntity.uniqueId == player.uniqueId })
    players[i].playerEntity = player
    players[i].disconnected = false
    players[i].showBossBar(timerbar)
    if (config.allowRejoin) {
      players[i].alive = true
      if (config.createLeavingDummy) {
        // TODO
      }
    }
  }

  private fun onGameStart() {
    this.timeState = Time.Day
    players.forEach {
      it.alive = true
      it.disconnected = false
    }

    players.forEach { it.showBossBar(timerbar) }
    ticker.runTaskTimer(MCWerewolf.instance, 0, 1)
    scenario.onGameStart()
    broadcast(Lang.get("game.started"))
  }

  private fun onGameEnd() {
    scenario.onGameEnd()
    ticker.cancel()
    players.forEach {
      it.hideBossBar(timerbar)
      it.playerEntity.resetPlayerTime()
    }
    broadcast(Lang.get("game.ended"))
  }

  private fun onDayEnd() {
    timeState = Time.Night
    timerbar.color(BossBar.Color.BLUE)
    scenario.onDayEnd()
    broadcast(Lang.get("game.on-day-end"))
  }

  private fun onNightEnd() {
    timeState = Time.Day
    timerbar.color(BossBar.Color.YELLOW)
    scenario.onNightEnd()
    broadcast(Lang.get("game.on-night-end"))
  }

  private fun onPlayerDeath() {
    scenario.onPlayerDeath()
    isPlayerDeath = true
  }
}
