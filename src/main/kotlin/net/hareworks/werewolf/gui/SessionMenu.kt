package net.hareworks.werewolf.gui

import net.hareworks.guilib.*
import net.hareworks.werewolf.Session
import net.hareworks.werewolf.game.RoleConfig
import net.hareworks.werewolf.game.role.Role
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class SessionMenu(val session: Session) : GUI {
  companion object {
    val instances: MutableMap<Player, SessionMenu> = mutableMapOf()
  }
  override fun open(player: Player) {
    mainPage.open(player)
  }
  val profilePage: net.hareworks.guilib.GUI =
      CUI(Vec2(-80, -40)) {
        register(
            Element("Session>Config>Profile"),
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
            Element("Session>Config>Roles"),
            NewLine(),
            Element("-".repeat(22)).apply {
              position = CUIComponent.Position.CENTER
              width = 20
            },
            NewLine(),
            *(Role.roles
                .map {
                  Interactable(
                          it.value.name,
                          Number(
                              Ref(
                                  {
                                    if (session.config.roles.containsKey(it.key))
                                        session.config.roles[it.key]!!.amount
                                    else 0
                                  },
                                  { value ->
                                    if (session.config.roles.containsKey(it.key)) {
                                      if (value == 0) session.config.roles.remove(it.key)
                                      else session.config.roles[it.key]?.amount = value
                                    } else {
                                      if (value != 0)
                                          session.config.roles[it.key] = RoleConfig(value, null)
                                    }
                                  }
                              ),
                              0,
                              20
                          )
                      )
                      .apply { width = 20 }
                }
                .toTypedArray())
        )
      }
  val configPage: net.hareworks.guilib.GUI =
      CUI(Vec2(-80, -40)) {
        register(
            Element("Session>Config"),
            NewLine(),
            Element("-".repeat(22)).apply {
              position = CUIComponent.Position.CENTER
              width = 20
            },
            NewLine(),
            Interactable("Profile", Anchor("(${session.config.name})", profilePage)).apply {
              width = 20
            },
            Interactable("Roles", Anchor("", rolePage)).apply { width = 20 },
        )
      }
  val PlayersPage: net.hareworks.guilib.GUI =
      CUI(Vec2(-80, -40)) {
        register(
            Element("Session>Players"),
            NewLine(),
            Element("-".repeat(22)).apply {
              position = CUIComponent.Position.CENTER
              width = 20
            },
            NewLine(),
            Element("Players: " + session.players.size + "/" + session.capacity).apply {
              position = CUIComponent.Position.LEFT
              width = 20
            },
            NewLine(),
            *(session.players.run {
              val list: MutableList<CUIComponent> = mutableListOf()
              this.forEach { player ->
                list.add(
                    Interactable(
                            player.name,
                            ActionElement({
                              dialog(
                                      "Confirm",
                                      "Kick " + player.name + "?",
                                      { sender ->
                                        if (sender == player) {
                                          sender.sendMessage("You can't kick yourself.")
                                          return@dialog
                                        }
                                        session.leave(player)
                                        net.hareworks.guilib.GUI.get(sender)?.quit()
                                      }
                                  )
                                  .open(player)
                            })
                        )
                        .apply { width = 20 }
                )
              }
              list.toTypedArray()
            }),
        )
      }
  fun dialog(title: String, message: String, func: (Player) -> Unit): net.hareworks.guilib.GUI =
      CUI(Vec2(-80, -40)) {
        register(
            Element(title),
            NewLine(),
            Element("-".repeat(22)).apply {
              position = CUIComponent.Position.CENTER
              width = 20
            },
            NewLine(),
            Element(message).apply { width = 20 },
            NewLine(),
            Interactable("OK", ActionElement(func)).apply { width = 20 },
            Interactable("Cancel", Quit("")).apply { width = 20 },
        )
      }
  val mainPage: net.hareworks.guilib.GUI =
      CUI(Vec2(-80, -40)) {
        register(
            NewLine(),
            Element("Session"),
            NewLine(),
            Element("-".repeat(22)).apply {
              position = CUIComponent.Position.CENTER
              width = 20
            },
            NewLine(),
            Element("Session: " + session.roomname).apply {
              position = CUIComponent.Position.LEFT
              width = 20
            },
            NewLine(),
            Element("Profile: " + session.config.name).apply {
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
            Interactable(
                    "Players:",
                    ActionElement({ player -> session.gui.PlayersPage.open(player) }).apply {
                      component = Component.text("${session.players.size}/${session.capacity}")
                    }
                )
                .apply { width = 20 },
            NewLine(),
            *(session.players.run {
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
