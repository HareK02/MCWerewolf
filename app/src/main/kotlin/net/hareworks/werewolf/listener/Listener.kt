package net.hareworks.werewolf

import com.google.common.collect.Maps
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.md_5.bungee.api.chat.ClickEvent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.scheduler.BukkitRunnable

class EventListener : Listener {
  @EventHandler
  public fun onPlayerInteract(e: PlayerInteractEvent) {
    val a = e.getAction()
		when (a) {
			Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> this.onLeftClick(ClickEvent(e))
			Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> this.onRightClick(ClickEvent(e))
			else -> {}
		}
  }
  @EventHandler
  public fun onPlayerInteractEntity(e: PlayerInteractEntityEvent) {
    this.onRightClick(ClickEvent(e))
  }
  @EventHandler
  public fun onPrePlayerAttackEntity(e: PrePlayerAttackEntityEvent) {
    this.onLeftClick(ClickEvent(e))
  }

	
  private class ClickEvent(private val event: PlayerEvent) {
    private val _slot: EquipmentSlot =
        if (event is PlayerInteractEvent) event.getHand() ?: EquipmentSlot.HAND
        else EquipmentSlot.HAND
    public fun getSlot(): EquipmentSlot {
      return _slot
    }
    public fun getPlayer(): Player {
      return event.getPlayer()
    }
    public fun getEventName(): String {
      return event.getEventName()
    }
    public fun setCancelled(cancel: Boolean) {
      if (event is Cancellable) event.setCancelled(cancel)
    }
  }

  private val lmap = Maps.newHashMap<Player, BukkitRunnable>()
  private fun onLeftClick(event: ClickEvent) {
    val player = event.getPlayer()
    if (lmap.containsKey(player)) return
    val runnable =
        object : BukkitRunnable() {
          override fun run() {
            lmap.remove(player)
          }
        }
    runnable.runTaskLater(MCWerewolf.instance, 1L)
    lmap.put(player, runnable)
  }

  private val rmap = Maps.newHashMap<Player, BukkitRunnable>()
  private fun onRightClick(event: ClickEvent) {
    val player = event.getPlayer()
    if (rmap.containsKey(player)) return
    val runnable =
        object : BukkitRunnable() {
          override fun run() {
            rmap.remove(player)
          }
        }
    runnable.runTaskLater(MCWerewolf.instance, 1L)
    rmap.put(player, runnable)

		val item = player.getInventory().getItem(event.getSlot()).getType()
    if (item == Material.WRITABLE_BOOK) {
			player.sendMessage(item.toString())
      event.setCancelled(true)
    }
  }

  @EventHandler
  public fun onPlayerJoin(e: PlayerJoinEvent) {
    val player: Player = e.getPlayer()

    if (MCWerewolf.instance.hasRoom(player)) {
      player.sendMessage("You are in a room.")
      return
    }
  }
}
