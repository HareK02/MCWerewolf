package net.hareworks.werewolf.game.role

import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.game.Player


object necromancer : RoleObject {
  override val name: String = Lang.get("roles.necromancer.name")
  override val description: String = Lang.get("roles.necromancer.desc")
  override val type: RoleObject.Type = RoleObject.Type.Werewolf
  override val composite: Boolean = false

  override fun instantiate(): Role {
    return Necromancer()
  }
}

class Necromancer : Role {
  override val meta: RoleObject = necromancer
  override fun openBook(player: Player) {
    TODO()
  }
}
