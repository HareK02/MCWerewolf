package net.hareworks.werewolf.game.role

import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.game.Player

object necromancer : RoleData {
  override val name: String = Lang.get("roles.necromancer.name")
  override val description: String = Lang.get("roles.necromancer.desc")
  override val type: RoleData.Type = RoleData.Type.Werewolf
  override val composite: Boolean = false

  override fun instantiate(): Role {
    return Necromancer()
  }
}

class Necromancer : Role {
  override val meta: RoleData = necromancer
  override fun openBook(player: Player) {
    TODO()
  }
}
