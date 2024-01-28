package net.hareworks.werewolf

import net.hareworks.werewolf.book.RoomMenu
import net.hareworks.werewolf.game.Config
import net.hareworks.werewolf.game.Game
import net.hareworks.werewolf.game.GameStatus
import org.bukkit.entity.Player

public class Room(creator: Player, name: String) {
  var roomname: String = name
  val book: RoomMenu = RoomMenu(this)
  val players: MutableList<Player> = mutableListOf(creator)
  var inGame: Boolean
    get() = this.Game.status != GameStatus.Lobby
    set(v) {
      if (v) this.Game.start() else this.Game.end()
    }
  var isPrivate: Boolean = false
  var isFull
    get() = this.players.size >= capacity
    set(v) { }
  var capacity: Int = 8

  val config: Config = Config.default()
  val Game: Game = Game(this)

  public fun join(player: Player): Boolean {
    if (this.isFull) return false
    this.players.add(player)
    return true
  }

  public fun leave(player: Player): Boolean {
    if (!this.players.contains(player)) return false
    this.players.remove(player)
    return true
  }

  public fun start() {
    this.Game.start()
    MCWerewolf.instance.logger.info("Room: $roomname started the game")
  }

  public fun onPlayerDisconnect(player: Player) {
    this.Game.onPlayerDisconnect(player)
  }
  init {
    this.book.build()
  }
}
