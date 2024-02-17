package net.hareworks.werewolf.gui.inventory

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

abstract class InventoryGUI(val size: Int) {
  companion object {
    val list: MutableList<InventoryGUI> = mutableListOf()
  }
  init {
    list.add(this)
  }

  var title: Component = Component.text("")
  val instances: MutableMap<Player, Triple<Inventory, PlayerInventory, Array<ItemStack?>>> =
      mutableMapOf()

  fun open(player: org.bukkit.entity.Player) {
    val bak = player.inventory.contents.clone()
    player.inventory.clear()
    instances[player] =
        Triple(org.bukkit.Bukkit.createInventory(null, size * 9, title), player.inventory, bak)
    player.openInventory(instances[player]!!.first)
    setup(player)
  }

  fun close(player: org.bukkit.entity.Player) {
    instances[player] ?: return
    player.closeInventory()
  }

  fun reload(player: Player) {
    instances[player]?.first?.clear() ?: return
    player.inventory.clear()
    setup(player)
  }

  fun setItem(player: org.bukkit.entity.Player, slot: Int, item: org.bukkit.inventory.ItemStack) {
    val (inventory, playerInventory, _) = instances[player] ?: return
    if (slot < size * 9) inventory.setItem(slot, item)
    else playerInventory.setItem(slot - size * 9, item)
  }
  fun setItem(
      player: org.bukkit.entity.Player,
      slot: List<Int>,
      item: org.bukkit.inventory.ItemStack
  ) {
    for (i in slot) {
      setItem(player, i, item)
    }
  }
  fun addItem(
      player: org.bukkit.entity.Player,
      slotType: SlotType,
      item: org.bukkit.inventory.ItemStack
  ) {
    val (inventory, playerInventory, _) = instances[player] ?: return
    if (slotType == SlotType.Container) inventory.addItem(item) else playerInventory.addItem(item)
  }
  fun addItems(
      player: org.bukkit.entity.Player,
      slotType: SlotType,
      items: List<org.bukkit.inventory.ItemStack>
  ) {
    items.forEach { addItem(player, slotType, it) }
  }
  fun getItem(player: org.bukkit.entity.Player, slot: Int): org.bukkit.inventory.ItemStack? {
    return instances[player]?.first?.getItem(slot)
  }
  fun getItems(
      player: org.bukkit.entity.Player,
      slot: IntRange
  ): List<org.bukkit.inventory.ItemStack?> {
    return slot.map { getItem(player, it) }
  }
  fun getItems(
      player: org.bukkit.entity.Player,
      slot: List<Int>
  ): List<org.bukkit.inventory.ItemStack?> {
    return slot.map { getItem(player, it) }
  }

  enum class Direction {
    Row,
    Column
  }
  enum class SlotType {
    Container,
    Player
  }

  fun slot(direction: Direction, slotType: SlotType, slot: List<Int>): List<Int> {
    return if (direction == Direction.Row)
        slot.toList().map { if (slotType == SlotType.Container) it else it + (this.size * 9) }
    else
        slot.toList().map {
          val size = if (slotType == SlotType.Container) this.size else 4
          (it % size) * 9 + (it / size) + if (slotType == SlotType.Container) 0 else (this.size * 9)
        }
  }
  fun slot(direction: Direction, slotType: SlotType, slot: IntRange): List<Int> {
    return slot(direction, slotType, slot.toList())
  }
  fun slot(direction: Direction, slotType: SlotType, vararg slot: IntRange): List<Int> {
    return slot.flatMap { slot(direction, slotType, it) }
  }

  abstract fun setup(player: org.bukkit.entity.Player)

  abstract fun onClick(event: org.bukkit.event.inventory.InventoryClickEvent)

  abstract fun onOpen(event: org.bukkit.event.inventory.InventoryOpenEvent)

  abstract fun onClose(event: org.bukkit.event.inventory.InventoryCloseEvent)
}

class InventoryGUIListener : org.bukkit.event.Listener {
  @org.bukkit.event.EventHandler
  fun onInventoryClick(event: org.bukkit.event.inventory.InventoryClickEvent) {
    val inv = InventoryGUI.list.find { it.instances[event.whoClicked as Player] != null } ?: return
    inv.onClick(event)
  }

  @org.bukkit.event.EventHandler
  fun onInventoryOpen(event: org.bukkit.event.inventory.InventoryOpenEvent) {
    val inv = InventoryGUI.list.find { it.instances[event.player as Player] != null } ?: return
    inv.onOpen(event)
  }

  @org.bukkit.event.EventHandler
  fun onInventoryClose(event: org.bukkit.event.inventory.InventoryCloseEvent) {
    val inv = InventoryGUI.list.find { it.instances[event.player as Player] != null } ?: return
    val instance = inv.instances[event.player as Player] ?: return
    val (_, playerInv, bak) = instance
    inv.onClose(event)
    playerInv.contents = bak
    inv.instances.remove(event.player as Player)
  }
}
