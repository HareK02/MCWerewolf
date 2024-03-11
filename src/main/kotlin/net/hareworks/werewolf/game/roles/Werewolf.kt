package net.hareworks.werewolf.game.role

import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.game.Player

object werewolf : RoleData {
  override val name: String = Lang.get("roles.werewolf.name")
  override val description: String = Lang.get("roles.werewolf.desc")
  override val type: RoleData.Type = RoleData.Type.Werewolf
  override val composite: Boolean = false

  override fun instantiate(): Role {
    return Werewolf()
  }
}

class Werewolf : Role {
  override val meta: RoleData = werewolf
  override fun openBook(player: Player) {
    TODO()
  }
}
