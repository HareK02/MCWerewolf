package net.hareworks.werewolf

import java.time.Duration
import net.hareworks.werewolf.game.Config
import net.hareworks.werewolf.game.Game
import net.hareworks.werewolf.gui.book.RoomConfigMenu
import net.hareworks.werewolf.gui.book.RoomMenu
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.entity.Player

public class Room(creator: Player, name: String) : Broadcaster {
  var roomname: String = name
  val config: Config = Config.default()

  override val players: MutableList<Player> = mutableListOf(creator)
  val book: RoomMenu = RoomMenu(this)
  val configBook: RoomConfigMenu = RoomConfigMenu(this.config)
  val inGame: Boolean
    get() = this.Game != null
  var isPrivate: Boolean = false
  val isFull
    get() = this.players.size >= capacity
  var capacity: Int = 8

  var Game: Game? = null

  public fun join(player: Player): Boolean {
    if (this.isFull) return false
    this.players.add(player)
    for (p in this.players) {
      p.sendMessage(Lang.get("game.joined").assign(player.name))
    }
    this.book.build()
    return true
  }

  public fun leave(player: Player): Boolean {
    if (!this.players.contains(player)) return false
    this.players.remove(player)
    logger().info("Room: $roomname ${this.players.toString()}")
    return true
  }

  public fun changeOwner(player: Player): Boolean {
    if (!this.players.contains(player)) return false
    this.players.remove(player)
    this.players.add(0, player)
    return true
  }

  public fun start() {
    if (this.config.getRoleCount() > this.players.size) {
      broadcast(Lang.get("game.not-enough-players"))
      return
    }
    broadcast(Component.text(Lang.get("game.will-start-soon")))
    MCWerewolf.instance.runTaskLater(90) {
      broadcastSound(Sound.sound(Key.key("block.bell.use"), Sound.Source.MASTER, 0.6f, 0.55f))
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
    MCWerewolf.instance.runTaskLater(100) {
      this.Game = Game(this)
      this.Game?.start()
    }
  }

  fun forceend() {
    this.Game?.forceend()
    this.Game = null
  }

  public fun onPlayerDisconnect(player: Player) {
    if (this.inGame) {
      this.Game?.onPlayerDisconnect(player)
    }
  }

  public fun onPlayerReconnect(player: Player) {
    val i = players.indexOf(players.find { it.player?.uniqueId == player.uniqueId })
    players[i] = player
    if (this.inGame) {
      players[i].sendMessage(Lang.get("game.reconnected-to-in-progress"))
      this.Game?.onPlayerReconnect(player)
    } else players[i].sendMessage(Lang.get("game.reconnected"))
  }

  init {
    this.book.build()
    this.configBook.build()
  }
}
