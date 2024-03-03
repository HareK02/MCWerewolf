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
                              Component.text("MC Werewolf Links", NamedTextColor.GRAY)
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
                        .apply { component = Component.text("Wiki/Discord/Github") }
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

// public object GameMenu : GameBook() {
//   private var pagenum: Int = 0

//   @JvmStatic
//   private fun roomlist(lineRange: Int, page: Int): Element {
//     var linenum = 2
//     var component =
//         Component.text("Rooms   ", textColor)
//             .append(
//                 Component.text("[Create room]\n", textColor)
//                     .hoverEvent(Component.text("Click to create a room.", hoverColor))
//                     .clickEvent(ClickEvent.runCommand("/ww menu create"))
//             )

//     val list =
//         MCWerewolf.instance.rooms.subList(
//             page * (lineRange - 2),
//             MCWerewolf.instance.rooms.size.coerceAtMost((page + 1) * (lineRange - 2))
//         )
//     if (list.isEmpty()) {
//       component = component.append(Component.text("\n        No rooms.\n", textColor))
//       linenum += 2
//     }
//     for (room in list) {
//       component =
//           component
//               .append(
//                   if (room.isPrivate)
//                       Component.text(" 🔒 ").hoverEvent(Component.text("Locked", textColor))
//                   else if (room.isFull)
//                       Component.text(" ■ ").hoverEvent(Component.text("Full", textColor))
//                   else
//                       Component.text(" □ ")
//                           .hoverEvent(Component.text("Open", textColor))
//                           .clickEvent(ClickEvent.runCommand("/ww menu join " + room.roomname))
//               )
//               .append(
//                   Component.text(room.roomname, textColor)
//                       .hoverEvent(Component.text("Click to join the room.", hoverColor))
//                       .clickEvent(ClickEvent.runCommand("/ww menu join " + room.roomname))
//               )
//               .append(Component.text("\n"))
//       linenum++
//     }
//     var pageControl =
//         GameBook.space(lineRange - linenum)
//             .append(Component.text("          "))
//             .append(
//                 if (page != 0) Component.text(" < ").clickEvent(ClickEvent.changePage(page))
//                 else Component.text("   ")
//             )
//             .append(Component.text((page + 1).toString(), textColor))
//             .append(
//                 if (page != pagenum)
//                     Component.text(" > \n").clickEvent(ClickEvent.changePage(page + 2))
//                 else Component.text("   \n")
//             )
//     return Element(lineRange, component.append(pageControl))
//   }

//   override fun content(): MutableList<MutableList<Element>> {
//     pagenum = (MCWerewolf.instance.rooms.size - 1) / (GameBook.MAXLINE - 2)
//     var _pages: MutableList<MutableList<Element>> = mutableListOf()
//     (0..pagenum).forEach { _pages.add(mutableListOf(roomlist(GameBook.MAXLINE, it))) }
//     return _pages
//   }

//   init {
//     build()
//   }
// }
