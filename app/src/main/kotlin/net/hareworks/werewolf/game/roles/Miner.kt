package net.hareworks.werewolf.game.role

import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.game.Player

object miner : RoleObject {
  override val name: String = Lang.get("roles.miner.name")
  override val description: String = Lang.get("roles.miner.desc")
  override val type: RoleObject.Type = RoleObject.Type.Citizen
  override val composite: Boolean = false

  override fun instantiate(): Role {
    return Miner()
  }
}

class Miner : Role {
  override val meta: RoleObject = miner
  override fun openBook(player: Player) {}
}
