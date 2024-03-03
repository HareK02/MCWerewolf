package net.hareworks.werewolf.gui.book

import net.hareworks.guilib.*
import net.hareworks.werewolf.Room
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class RoomMenu(val room: Room) : GUI {
  override fun open(player: Player) {
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
              Interactable("Config", Anchor("Config", room.gui.configPage)).apply { width = 20 },
              Interactable(
                      "Start",
                      ActionElement({ player -> player.sendMessage("Start Game") }).apply {
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
        .open(player)
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
            Interactable(
                "Profile (${room.config.name})",
                ActionElement({ player -> player.sendMessage("Profile") }),
            )
        )
      }
}

// public class RoomMenu(room: Room) : GameBook() {
//   private var pagenum: Int = 0
//   private val room: Room = room

//   private fun menu(page: Int): Element {
//     val component =
//         Component.text("Room: " + room.roomname + "\n", textColor)
//             // .append(
//             //     Component.text("[Config]", textColor)
//             //         .hoverEvent(Component.text("Click to configure the room.", hoverColor))
//             //         .clickEvent(ClickEvent.runCommand("/ww menu config"))
//             // )
//             .append(
//               Component.text(
//                   "Profile: " + room.config.name + "\n",
//               )
//             )
//             .append(Component.text("             "))
//             .append(
//                 Component.text("[Start Game]\n", textColor)
//                     .hoverEvent(Component.text("Click to start the game.", hoverColor))
//                     .clickEvent(ClickEvent.runCommand("/ww start"))
//             )
//             .append(
//                 Component.text(
//                     "Players:        " + room.players.size + "/" + room.capacity + "\n",
//                     textColor
//                 )
//             )
//             .append(playerlist(page))
//     return Element(GameBook.MAXLINE, component)
//   }

//   private fun playerlist(page: Int): Component {
//     var size = GameBook.MAXLINE - 4
//     var linenum = 0
//     var component = Component.empty()
//     val list = room.players.subList(page * size, room.players.size.coerceAtMost((page + 1) *
// size))
//     for (player in list) {
//       component = component.append(Component.text(" " + player.name + "\n", textColor))
//       linenum++
//     }
//     component =
//         component
//             .append(GameBook.space(size - linenum))
//             .append(Component.text("          "))
//             .append(
//                 if (page != 0) Component.text(" < ").clickEvent(ClickEvent.changePage(page))
//                 else Component.text("   ")
//             )
//             .append(Component.text((page + 1).toString(), textColor))
//             .append(
//                 if (page != pagenum)
//                     Component.text(" > ").clickEvent(ClickEvent.changePage(page + 2))
//                 else Component.text("   ")
//             )
//     return component
//   }

//   override fun content(): MutableList<MutableList<Element>> {
//     pagenum = (room.players.size - 1) / (GameBook.MAXLINE - 4)
//     var _pages: MutableList<MutableList<Element>> = mutableListOf()
//     (0..pagenum).forEach { _pages.add(mutableListOf(menu(it))) }
//     return _pages
//   }
// }
