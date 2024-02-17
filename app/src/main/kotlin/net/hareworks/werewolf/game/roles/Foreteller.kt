package net.hareworks.werewolf.game.role

import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.game.Player

object foreteller : RoleObject {
  override val name: String = Lang.get("roles.foreteller.name")
  override val description: String = Lang.get("roles.foreteller.desc")
  override val type: RoleObject.Type = RoleObject.Type.Citizen
  override val composite: Boolean = false

  override fun instantiate(): Role {
    return Foreteller()
  }
}

class Foreteller : Role {
  override val meta: RoleObject = foreteller

  override fun openBook(player: Player) { }
}