package net.hareworks.werewolf.book

import net.hareworks.werewolf.MCWerewolf
import net.hareworks.werewolf.Room
import net.kyori.adventure.text.Component

public class RoomMenu(room: Room) : GameBook() {
  private var pagenum: Int = 0
  private val room: Room = room

  private fun test(): Element {
    MCWerewolf.instance.logger.info(room.toString())
    val component =
        Component.text("Room: " + room.roomname + "\n", textColor)
            .append(
                Component.text(
                    "Players:       " + room.players.size + "/" + room.capacity + "\n",
                    textColor
                )
            )
    
    return Element(1, component)
  }

  private fun playerlist() : Element {
    var linenum = 9
    var component = Component.text("Players\n", textColor)
    for (player in room.players) {
      component = component.append(Component.text(player.name + "\n", textColor))
      linenum++
    }
    return Element(linenum, component)
  }

  override fun content(): MutableList<MutableList<Element>> {
    pagenum = (room.players.size - 1) / (GameBook.MAXLINE - 3)
    var _pages: MutableList<MutableList<Element>> = mutableListOf()
    _pages.add(mutableListOf(test()))
    return _pages
  }
}
