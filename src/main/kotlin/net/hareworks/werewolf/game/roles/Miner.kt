package net.hareworks.werewolf.game.role

import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.game.Player

object miner : RoleData {
  override val name: String = Lang.get("roles.miner.name")
  override val description: String = Lang.get("roles.miner.desc")
  override val type: RoleData.Type = RoleData.Type.Citizen
  override val composite: Boolean = false

  override fun instantiate(): Role {
    return Miner()
  }
}

class Miner : Role {
  override val meta: RoleData = miner
  override fun openBook(player: Player) {}
}
