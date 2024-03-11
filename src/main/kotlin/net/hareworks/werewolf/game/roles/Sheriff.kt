package net.hareworks.werewolf.game.role

import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.game.Player

object sheriff : RoleData {
  override val name: String = Lang.get("roles.sheriff.name")
  override val description: String = Lang.get("roles.sheriff.desc")
  override val type: RoleData.Type = RoleData.Type.Citizen
  override val composite: Boolean = false

  override fun instantiate(): Role {
    return Sheriff()
  }
}

class Sheriff : Role {
  override val meta: RoleData = sheriff
  override fun openBook(player: Player) {
    TODO()
  }
}
