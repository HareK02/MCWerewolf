package net.hareworks.werewolf.game

import net.hareworks.werewolf.game.role.Role
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.entity.Player

public enum class Team {
  Villager,
  Werewolf,
  None,
}

abstract class User(var playerEntity: Player) : Audience {
  public fun sendMessage(message: String) {
    playerEntity.sendMessage(message)
  }
  override public fun sendMessage(message: Component) {
    playerEntity.sendMessage(message)
  }
  override public fun showTitle(title: Title) {
    playerEntity.showTitle(title)
  }
  override public fun playSound(sound: Sound) {
    playerEntity.playSound(sound)
  }
  override public fun showBossBar(bar: BossBar) {
    playerEntity.showBossBar(bar)
  }
  override public fun hideBossBar(bar: BossBar) {
    playerEntity.hideBossBar(bar)
  }
  fun setPlayerTime(time: Long, relative: Boolean) {
    playerEntity.setPlayerTime(time, relative)
  }

  abstract fun openBook()
}

public class Player(playerEntity: Player, val role: Role) : User(playerEntity) {
  var alive: Boolean = true
  var disconnected: Boolean = false

  override fun openBook() {
    role.openBook(this)
  }
}

public class Spectator(playerEntity: Player) : User(playerEntity) {
  override fun openBook() {
    TODO()
  }
}
