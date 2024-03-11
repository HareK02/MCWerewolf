package net.hareworks.werewolf.game.role

import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.game.Player

object villager : RoleData {
  override val name: String = Lang.get("roles.villager.name")
  override val description: String = Lang.get("roles.villager.desc")
  override val type: RoleData.Type = RoleData.Type.Citizen
  override val composite: Boolean = false

  override fun instantiate(): Role {
    return Villager()
  }
}

class Villager : Role {
  override val meta: RoleData = villager
  override fun openBook(player: Player) {
    TODO()
  }
}
