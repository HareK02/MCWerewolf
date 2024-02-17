package net.hareworks.werewolf.gui.inventory

import net.hareworks.werewolf.MCWerewolf
import net.hareworks.werewolf.debuglog
import net.hareworks.werewolf.game.Config
import net.hareworks.werewolf.game.role.Role
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class RoleConfig(
    val config: Config,
) : InventoryGUI(size = 4) {
  init {
    title = Component.text("Role Config")
  }

  override fun setup(player: Player) {
    setItem(
        player,
        0,
        ItemStack(org.bukkit.Material.BARRIER).apply {
          itemMeta =
              itemMeta.apply {
                setCustomModelData(502)
                displayName(Component.text("Return"))
              }
        }
    )
    val fill =
        ItemStack(org.bukkit.Material.BARRIER).apply {
          itemMeta =
              itemMeta.apply {
                setCustomModelData(500)
                displayName(Component.text(""))
              }
        }
    setItem(player, slot(Direction.Column, SlotType.Container, 1..7), fill)
    setItem(player, slot(Direction.Column, SlotType.Player, 0..7), fill)
    setItem(player, slot(Direction.Row, SlotType.Player, 0..8), fill)

    applyRoleCount(player)

    Role.roles.toList().sortedByDescending { it.second.type }.forEach { role ->
      addItem(
          player,
          SlotType.Player,
          ItemStack(org.bukkit.Material.PLAYER_HEAD).apply {
            itemMeta =
                itemMeta.apply {
                  persistentDataContainer.set(
                      NamespacedKey(MCWerewolf.instance, "role"),
                      PersistentDataType.STRING,
                      role.first
                  )
                  displayName(Component.text(role.second.name))
                  lore(
                      mutableListOf<Component>(
                          Component.text(role.second.description)
                              .decoration(TextDecoration.ITALIC, false)
                              .color(TextColor.color(1f, 1f, 1f)),
                          Component.text("Type: ${role.second.type}")
                              .decoration(TextDecoration.ITALIC, false)
                              .color(TextColor.color(1f, 1f, 1f)),
                      )
                  )
                }
          }
      )
    }
    config.roles.forEach { config ->
      val role = Role.roles[config.name] ?: return
      addItem(
          player,
          SlotType.Container,
          ItemStack(org.bukkit.Material.PLAYER_HEAD, config.amount).apply {
            itemMeta =
                itemMeta.apply {
                  persistentDataContainer.set(
                      NamespacedKey(MCWerewolf.instance, "role"),
                      PersistentDataType.STRING,
                      config.name
                  )
                  displayName(Component.text(role.name))
                  lore(
                      mutableListOf<Component>(
                          Component.text(role.description)
                              .decoration(TextDecoration.ITALIC, false)
                              .color(TextColor.color(1f, 1f, 1f)),
                          Component.text("Type: ${role.type}")
                              .decoration(TextDecoration.ITALIC, false)
                              .color(TextColor.color(1f, 1f, 1f)),
                      )
                  )
                }
          }
      )
    }
  }

  fun applyRoleCount(player: Player) {
    val roleCounts = config.getRoleCounts()
    setItem(
        player,
        18,
        ItemStack(
                if (roleCounts[0] > 0) org.bukkit.Material.GREEN_STAINED_GLASS_PANE
                else org.bukkit.Material.GRAY_STAINED_GLASS_PANE,
                if (roleCounts[0] > 0) roleCounts[0] else 1
            )
            .apply { itemMeta = itemMeta.apply { displayName(Component.text("Common Roles")) } }
    )
    setItem(
        player,
        19,
        ItemStack(
                if (roleCounts[1] > 0) org.bukkit.Material.BLUE_STAINED_GLASS_PANE
                else org.bukkit.Material.GRAY_STAINED_GLASS_PANE,
                if (roleCounts[1] > 0) roleCounts[1] else 1
            )
            .apply { itemMeta = itemMeta.apply { displayName(Component.text("Citizen Roles")) } }
    )
    setItem(
        player,
        27,
        ItemStack(
                if (roleCounts[2] > 0) org.bukkit.Material.RED_STAINED_GLASS_PANE
                else org.bukkit.Material.GRAY_STAINED_GLASS_PANE,
                if (roleCounts[2] > 0) roleCounts[2] else 1
            )
            .apply { itemMeta = itemMeta.apply { displayName(Component.text("Werewolf Roles")) } }
    )
    setItem(
        player,
        28,
        ItemStack(
                if (roleCounts[3] > 0) org.bukkit.Material.YELLOW_STAINED_GLASS_PANE
                else org.bukkit.Material.GRAY_STAINED_GLASS_PANE,
                if (roleCounts[3] > 0) roleCounts[3] else 1
            )
            .apply { itemMeta = itemMeta.apply { displayName(Component.text("Third Roles")) } }
    )
  }

  override fun onClick(event: InventoryClickEvent) {
    val clickedItem = event.currentItem
    val replacedItem = event.cursor

    val isPlayerInventory: Boolean = (event.getRawSlot() >= this.size * 9)

    if (clickedItem == null) return
    if (clickedItem.type == org.bukkit.Material.BARRIER) {
      when {
        clickedItem.itemMeta.customModelData == 500 -> {
          event.isCancelled = true
        }
        clickedItem.itemMeta.customModelData == 502 -> {
          event.setCancelled(true)
          event.whoClicked.closeInventory()
        }
      }
    }
    when {
      clickedItem.type == org.bukkit.Material.PLAYER_HEAD -> {
        event.isCancelled = true
        if (event.click != ClickType.LEFT) return
        if (!isPlayerInventory) { // remove role
          val name =
              clickedItem.itemMeta.persistentDataContainer.get(
                  NamespacedKey(MCWerewolf.instance, "role"),
                  PersistentDataType.STRING
              )
                  ?: return debuglog("RoleConfig: item has no role")
          val role = Role.roles[name] ?: return debuglog("RoleConfig: role not found")
          val roleConfig = config.roles.firstOrNull { it.name == name }
          if (roleConfig == null) return
          if (roleConfig.amount > 1) {
            roleConfig.amount -= 1
            val item = clickedItem.clone()
            item.amount = roleConfig.amount
            event.currentItem = item
            debuglog("RoleConfig: removed 1 $name")
          } else {
            config.roles.remove(roleConfig)
            reload(event.whoClicked as Player)
            debuglog("RoleConfig: removed $name")
          }
        } else { // add role
          val name =
              clickedItem.itemMeta.persistentDataContainer.get(
                  NamespacedKey(MCWerewolf.instance, "role"),
                  PersistentDataType.STRING
              )
                  ?: return debuglog("RoleConfig: item has no role")
          val role = Role.roles[name] ?: return debuglog("RoleConfig: role not found")
          if (config.roles.any { it.name == name }) {
            val roleConfig =
                config.roles.firstOrNull() { it.name == name }
                    ?: return debuglog("RoleConfig: role not found")
            if (roleConfig.amount >= 16) {
              event.whoClicked.playSound(
                  Sound.sound(Key.key("block.note_block.bass"), Sound.Source.BLOCK, 0.6f, 1f)
              )
              return
            }
            roleConfig.amount += 1
            event.inventory.contents
                .firstOrNull {
                  it?.itemMeta?.persistentDataContainer?.get(
                      NamespacedKey(MCWerewolf.instance, "role"),
                      PersistentDataType.STRING
                  ) == name
                }
                ?.amount = roleConfig.amount
            debuglog("RoleConfig: $name is now ${roleConfig.amount} players")
          } else {
            config.roles.add(
                net.hareworks.werewolf.game.RoleConfig(
                    name,
                    1,
                    MCWerewolf.instance
                        .defaultConfig
                        .getConfigurationSection("roles.${role.name}.config")
                        ?.getValues(false) as?
                        Map<String, Any>
                )
            )
            reload(event.whoClicked as Player)
            debuglog("RoleConfig: added $name")
          }
        }
        event.whoClicked.playSound(
            Sound.sound(Key.key("ui.button.click"), Sound.Source.MASTER, 0.6f, 1f)
        )
        applyRoleCount(event.whoClicked as Player)
      }
    }
  }

  override fun onOpen(event: InventoryOpenEvent) {}

  override fun onClose(event: InventoryCloseEvent) {}
}
