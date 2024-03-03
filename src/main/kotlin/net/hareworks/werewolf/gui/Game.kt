package net.hareworks.werewolf.gui.book

import java.util.UUID
import net.hareworks.guilib.*
import net.hareworks.werewolf.MCWerewolf
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.w3c.dom.Element

class GameMenu() : GUI {
  companion object {
    val instances = HashMap<UUID, GameMenu>()
  }
  val ui: net.hareworks.guilib.GUI =
      CUI(Vec2(-80, -40)) {
        register(
            Element("MC Werewolf").apply { width = 20 },
            Element("v0.1.0").apply {
              position = CUIComponent.Position.RIGHT
              color = NamedTextColor.GRAY
              width = 0
            },
            NewLine(),
            Element("-".repeat(22)).apply {
              position = CUIComponent.Position.CENTER
              width = 20
            },
            NewLine(),
            Interactable(
                    "Links",
                    ActionElement({ player ->
                          player.sendMessage(
                              Component.text("MC Werewolf Links")
                                  .append(Component.newline())
                                  .append(
                                      Component.text("Wiki")
                                          .hoverEvent(
                                              Component.text(
                                                  "Click to open the wiki.",
                                                  NamedTextColor.GRAY
                                              )
                                          )
                                          .clickEvent(
                                              ClickEvent.openUrl(
                                                  "https://github.com/HareK02/MCWerewolf/wiki"
                                              )
                                          )
                                  )
                                  .append(Component.newline())
                                  .append(
                                      Component.text("Discord")
                                          .hoverEvent(
                                              Component.text(
                                                  "Click to join the discord server.",
                                                  NamedTextColor.GRAY
                                              )
                                          )
                                          .clickEvent(
                                              ClickEvent.openUrl("https://discord.gg/KDcfDY3AUt")
                                          )
                                  )
                                  .append(Component.newline())
                                  .append(
                                      Component.text("Github")
                                          .hoverEvent(
                                              Component.text(
                                                  "Click to open the github repository.",
                                                  NamedTextColor.GRAY
                                              )
                                          )
                                          .clickEvent(
                                              ClickEvent.openUrl(
                                                  "https://github.com/HareK02/MCWerewolf"
                                              )
                                          )
                                  )
                          )
                        })
                        .apply { component = Component.text("Wiki/Github") }
                )
                .apply { width = 20 },
            Interactable(
                    "New Room",
                    ActionElement({ player ->
                      GameMenu.instances[player.uniqueId]?.ui?.forceQuit()
                      player.performCommand("ww create")
                      player.performCommand("ww menu")
                    })
                )
                .apply { width = 20 },
            Element("-".repeat(22)).apply {
              position = CUIComponent.Position.CENTER
              width = 20
            },
            NewLine(),
            Element("Rooms").apply { width = 20 },
            NewLine(),
            *(MCWerewolf.instance
                .rooms
                .map {
                  Interactable(
                          "",
                          ActionElement({ player -> player.sendMessage("Join room") }).apply {
                            component = Component.text(it.roomname)
                          }
                      )
                      .apply { width = 20 }
                }
                .toTypedArray()
                .run {
                  if (isEmpty())
                      arrayOf(
                          Element("No rooms").apply {
                            width = 20
                            position = CUIComponent.Position.CENTER
                          }
                      )
                  else this
                }),
        )
      }

  override fun open(player: Player) {
    instances[player.uniqueId] = this
    ui.open(player)
  }
}