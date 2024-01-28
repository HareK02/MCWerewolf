package net.hareworks.werewolf.game.role

import org.bukkit.entity.Player

val WerewolfMeta =
    Meta(name = "werewolf", description = "A werewolf", team = Team.Werewolf, composite = false)

public class Werewolf : Role() {
  override val meta: Meta = WerewolfMeta

  override fun openBook(player: Player) {
    TODO()
  }

  override fun setConfig(config: Map<String, Any>) {}
}
