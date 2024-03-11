package net.hareworks.werewolf.game.role

import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.game.Player

object foreteller : RoleData {

  override val name: String = Lang.get("roles.foreteller.name")
  override val description: String = Lang.get("roles.foreteller.desc")
  override val type: RoleData.Type = RoleData.Type.Citizen
  override val composite: Boolean = false

  override fun instantiate(): Role {
    return Foreteller()
  }
}

class Foreteller : Role {
  override val meta: RoleData = foreteller

  override fun openBook(player: Player) {}
}
