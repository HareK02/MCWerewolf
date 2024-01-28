package net.hareworks.werewolf.game.role

import org.bukkit.entity.Player

public enum class Team {
  Villager,
  Werewolf,
  ThirdCamp,
}

data class Meta(
    val name: String,
    val description: String,
    val team: Team,
    val composite: Boolean,
)

abstract class Role {
  companion object {
    public fun valueOf(name: String): Role? {
      return when (name) {
        "villager" -> Villager()
        "werewolf" -> Werewolf()
        else -> null
      }
    }
  }

  abstract val meta: Meta

  abstract fun setConfig(config: Map<String, Any>)
  abstract fun openBook(player: Player)
}
