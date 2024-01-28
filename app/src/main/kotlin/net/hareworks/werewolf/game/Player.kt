package net.hareworks.werewolf.game

import net.hareworks.werewolf.game.role.Role
import org.bukkit.entity.Player

abstract class User(val player: Player) {
  public fun sendMessage(message: String) {
    player.sendMessage(message)
  }

  abstract fun openBook()
}

public class Player(player: Player, val role: Role) : User(player) {
  var alive: Boolean = true
  var disconnected: Boolean = false

  override fun openBook() {
    role.openBook(player)
  }
}

public class spectator(player: Player) : User(player) {
  override fun openBook() {
    TODO()
  }
}
