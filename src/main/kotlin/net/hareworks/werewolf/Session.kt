package net.hareworks.werewolf

import java.time.Duration
import net.hareworks.werewolf.game.Config
import net.hareworks.werewolf.game.Game
import net.hareworks.werewolf.gui.SessionMenu
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.entity.Player

public class Session(creator: Player, name: String) : Broadcaster {
  var roomname: String = name
  val config: Config = Config.default()

  val players: MutableList<Player> = mutableListOf(creator)
  override val targets: List<Player>
    get() = this.players.toList()

  val gui: SessionMenu = SessionMenu(this)
  val inGame: Boolean
    get() = this.game != null
  var isPrivate: Boolean = false
  val isFull
    get() = this.players.size >= capacity
  var capacity: Int = 8

  var game: Game? = null

  public fun join(player: Player): Boolean {
    if (this.isFull) return false
    this.players.add(player)
    for (p in this.players) {
      p.sendMessage(Lang.get("game.joined").assign(player.name))
    }
    return true
  }

  public fun leave(player: Player): Boolean {
    if (!this.players.contains(player)) return false
    this.players.remove(player)
    return true
  }

  public fun changeOwner(player: Player): Boolean {
    if (!this.players.contains(player)) return false
    this.players.remove(player)
    this.players.add(0, player)
    return true
  }

  public fun start() {
    if (this.inGame) {
      broadcast(Lang.get("game.already-started"))
      return
    }
    // if (this.players.size < 2) {
    //   broadcast(Lang.get("game.not-enough-players"))
    //   return
    // }
    broadcast(Component.text(Lang.get("game.will-start-soon")))
    MCWerewolf.instance.runTaskLater(10) {
      broadcastTitle(
          Title.title(
              Component.text("\uE000").font(Key.key("guitoolkit", "gui")),
              Component.empty(),
              Title.Times.times(
                  Duration.ofMillis(500),
                  Duration.ofMillis(1000),
                  Duration.ofMillis(500)
              )
          )
      )
    }
    MCWerewolf.instance.runTaskLater(50) {
      broadcastSound(Sound.sound(Key.key("block.bell.use"), Sound.Source.MASTER, 0.6f, 0.55f))
      this.game = Game(this)
      for (p in this.game!!.players) {
        p.showTitle(
            Title.title(
                Component.text(Lang.get("game.started")),
                Component.text(Lang.get("game.started-subtitle")),
                Title.Times.times(
                    Duration.ofMillis(500),
                    Duration.ofMillis(1000),
                    Duration.ofMillis(500)
                )
            )
        )
      }
    }
    MCWerewolf.instance.runTaskLater(100) { this.game?.start() }
  }

  fun forceEnd() {
    this.game?.forceEnd()
    this.game = null
  }

  public fun onPlayerDisconnect(player: Player) {
    if (this.inGame) {
      this.game?.onPlayerDisconnect(player)
    }
  }

  public fun onPlayerReconnect(player: Player) {
    val i = players.indexOf(players.find { it.player?.uniqueId == player.uniqueId })
    players[i] = player
    if (this.inGame) {
      players[i].sendMessage(Lang.get("game.reconnected-to-in-progress"))
      this.game?.onPlayerReconnect(player)
    } else players[i].sendMessage(Lang.get("game.reconnected"))
  }

  init {}
}
