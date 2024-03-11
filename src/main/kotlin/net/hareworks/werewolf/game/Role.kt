package net.hareworks.werewolf.game.role

import net.hareworks.werewolf.game.Player

interface Role {
  companion object {
    public val roles: Map<String, RoleData> =
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

  val meta: RoleData

  fun openBook(player: Player)
}

interface RoleData {
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