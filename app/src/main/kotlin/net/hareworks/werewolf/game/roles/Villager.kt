package net.hareworks.werewolf.game.role

import org.bukkit.entity.Player

val VillagerMeta =
    Meta(name = "villager", description = "A villager", team = Team.Villager, composite = false)

public class Villager : Role() {
  override val meta: Meta = VillagerMeta

  override fun openBook(player: Player) {
    TODO()
  }

  override fun setConfig(config: Map<String, Any>) {}
}
