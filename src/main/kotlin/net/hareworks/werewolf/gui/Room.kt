package net.hareworks.werewolf.gui.book

import net.hareworks.guilib.*
import net.hareworks.werewolf.Room
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class RoomMenu(val room: Room) : GUI {
  override fun open(player: Player) {
    mainPage.open(player)
  }
  val profilePage: net.hareworks.guilib.GUI =
      CUI(Vec2(-80, -40)) {
        register(
            Element("RoomMenu>Config>Profile"),
            NewLine(),
            Element("-".repeat(22)).apply {
              position = CUIComponent.Position.CENTER
              width = 20
            },
            NewLine(),
            Element("Left Click to change profile,"),
            NewLine(),
            Element("Right Click to show details."),
            NewLine(),
            Element("-".repeat(22)).apply {
              position = CUIComponent.Position.CENTER
              width = 20
            },
            NewLine(),
        )
      }
  val rolePage: net.hareworks.guilib.GUI =
      CUI(Vec2(-80, -40)) {
        register(
            Element("RoomMenu>Config>Roles"),
            NewLine(),
            Element("-".repeat(22)).apply {
              position = CUIComponent.Position.CENTER
              width = 20
            },
            NewLine(),
        )
      }
  val configPage: net.hareworks.guilib.GUI =
      CUI(Vec2(-80, -40)) {
        register(
            Element("RoomMenu>Config"),
            NewLine(),
            Element("-".repeat(22)).apply {
              position = CUIComponent.Position.CENTER
              width = 20
            },
            NewLine(),
            Interactable("Profile (${room.config.name})", Anchor("Change", profilePage)).apply {
              width = 20
            },
            Interactable("Roles", Anchor("Roles", rolePage)).apply { width = 20 },
        )
      }
  val mainPage: net.hareworks.guilib.GUI =
      CUI(Vec2(-80, -40)) {
        register(
            NewLine(),
            Element("RoomMenu"),
            NewLine(),
            Element("-".repeat(22)).apply {
              position = CUIComponent.Position.CENTER
              width = 20
            },
            NewLine(),
            Element("Room: " + room.roomname).apply {
              position = CUIComponent.Position.LEFT
              width = 20
            },
            NewLine(),
            Element("Profile: " + room.config.name).apply {
              position = CUIComponent.Position.LEFT
              width = 20
            },
            NewLine(),
            Interactable("Config", Anchor("Config", configPage)).apply { width = 20 },
            Interactable(
                    "Start",
                    ActionElement({ player -> player.performCommand("ww start") }).apply {
                      component = Component.text("[Start Game]")
                    }
                )
                .apply { width = 20 },
            Element("Players: " + room.players.size + "/" + room.capacity).apply {
              position = CUIComponent.Position.LEFT
              width = 20
            },
            NewLine(),
            *(room.players.run {
              val list: MutableList<CUIComponent> = mutableListOf()
              this.forEach { player ->
                list.add(Element(player.name).apply { position = CUIComponent.Position.LEFT })
                list.add(NewLine())
              }
              list.toTypedArray()
            }),
        )
      }
}
