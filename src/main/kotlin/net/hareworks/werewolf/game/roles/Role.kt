package net.hareworks.werewolf.game.role

import net.hareworks.werewolf.game.Player

interface RoleObject {
  public enum class Type {
    Common,
    Citizen,
    Werewolf,
    Third,
  }

  val name: String
  val description: String
  val type: Type
  val composite: Boolean

  fun instantiate(): Role
}

interface Role {
  companion object {
    public val roles: Map<String, RoleObject> =
        mapOf(
            "villager" to villager,
            "werewolf" to werewolf,
            "foreteller" to foreteller,
            "miner" to miner,
            "sheriff" to sheriff,
            "necromancer" to necromancer,
            "witch" to witch,
            "tracker" to tracker,
        )
  }

  val meta: RoleObject

  fun openBook(player: Player)
}
