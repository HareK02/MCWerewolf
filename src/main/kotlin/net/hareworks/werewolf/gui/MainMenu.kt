package net.hareworks.werewolf.gui

import java.util.UUID
import net.hareworks.guilib.*
import net.hareworks.werewolf.MCWerewolf
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.w3c.dom.Element

class MainMenu() : GUI {
  companion object {
    val instances = HashMap<UUID, MainMenu>()
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
                    "Start Session",
                    ActionElement({ player ->
                      MainMenu.instances[player.uniqueId]?.ui?.quit()
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
            Element("Sessions:").apply { width = 20 },
            NewLine(),
            *(MCWerewolf.instance
                .sessionList
                .map {
                  Interactable(
                          it.roomname,
                          ActionElement({ player ->
                            MCWerewolf.instance.getRoom(player)?.let { room ->
                              if (room.roomname == it.roomname) {
                                player.sendMessage(Component.text("You are already in this room."))
                                return@ActionElement
                              }
                              room.leave(player)
                            }
                            player.performCommand("ww join " + it.roomname)
                            player.performCommand("ww menu")
                          })
                      )
                      .apply { width = 20 }
                }
                .toTypedArray()
                .run {
                  if (isEmpty())
                      arrayOf(
                          Element("No sessions").apply {
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
