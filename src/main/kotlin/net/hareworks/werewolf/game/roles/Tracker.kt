package net.hareworks.werewolf.game.role

import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.game.Player

object tracker : RoleData {
  override val name: String = Lang.get("roles.tracker.name")
  override val description: String = Lang.get("roles.tracker.desc")
  override val type: RoleData.Type = RoleData.Type.Citizen
  override val composite: Boolean = false

  override fun instantiate(): Role {
    return Tracker()
  }
}

class Tracker : Role {
  override val meta: RoleData = tracker
  override fun openBook(player: Player) {
    TODO()
  }
}
