package net.hareworks.werewolf.game.role

import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.game.Player

object sheriff : RoleObject {
  override val name: String = Lang.get("roles.sheriff.name")
  override val description: String = Lang.get("roles.sheriff.desc")
  override val type: RoleObject.Type = RoleObject.Type.Citizen
  override val composite: Boolean = false

  override fun instantiate(): Role {
    return Sheriff()
  }
}

class Sheriff : Role {
  override val meta: RoleObject = sheriff
  override fun openBook(player: Player) {
    TODO()
  }
}