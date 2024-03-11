package net.hareworks.werewolf.game.role

import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.game.Player

object witch : RoleData {
  override val name: String = Lang.get("roles.witch.name")
  override val description: String = Lang.get("roles.witch.desc")
  override val type: RoleData.Type = RoleData.Type.Werewolf
  override val composite: Boolean = false

  override fun instantiate(): Role {
    return Witch()
  }
}

class Witch : Role {
  override val meta: RoleData = witch
  override fun openBook(player: Player) {
    TODO()
  }
}
