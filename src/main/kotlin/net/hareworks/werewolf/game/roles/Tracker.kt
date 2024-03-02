package net.hareworks.werewolf.game.role

import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.game.Player

object tracker : RoleObject {
  override val name: String = Lang.get("roles.tracker.name")
  override val description: String = Lang.get("roles.tracker.desc")
  override val type: RoleObject.Type = RoleObject.Type.Citizen
  override val composite: Boolean = false

  override fun instantiate(): Role {
    return Tracker()
  }
}

class Tracker : Role {
  override val meta: RoleObject = tracker
  override fun openBook(player: Player) {
    TODO()
  }
}