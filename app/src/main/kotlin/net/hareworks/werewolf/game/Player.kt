package net.hareworks.werewolf.game

import net.hareworks.werewolf.game.role.Role
import org.bukkit.entity.Player

abstract class User(var playerEntity: Player) {
  public fun sendMessage(message: String) {
    playerEntity.sendMessage(message)
  }

  abstract fun openBook()
}

public class Player(playerEntity: Player, val role: Role) : User(playerEntity) {
  var alive: Boolean = true
  var disconnected: Boolean = false

  override fun openBook() {
    role.openBook(playerEntity)
  }
}

public class spectator(playerEntity: Player) : User(playerEntity) {
  override fun openBook() {
    TODO()
  }
}
