package net.hareworks.werewolf.book

import net.hareworks.werewolf.MCWerewolf
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent

public object GameMenu : GameBook() {
  private var pagenum: Int = 0

  @JvmStatic
  private fun roomlist(lineRange: Int, page: Int): Element {
    var linenum = 2
    var component =
        Component.text("Rooms   ", textColor)
            .append(
                Component.text("[Create room]\n", textColor)
                    .hoverEvent(Component.text("Click to create a room.", hoverColor))
                    .clickEvent(ClickEvent.runCommand("/werewolf& create"))
            )

    val list =
        MCWerewolf.instance.rooms.subList(
            page * (lineRange - 2),
            MCWerewolf.instance.rooms.size.coerceAtMost((page + 1) * (lineRange - 2))
        )
    if (list.isEmpty()) {
      component = component.append(Component.text("\n        No rooms.\n", textColor))
      linenum += 2
    }
    for (room in list) {
      component =
          component
              .append(
                  if (room.isPrivate)
                      Component.text(" 🔒 ").hoverEvent(Component.text("Locked", textColor))
                  else if (room.isFull)
                      Component.text(" ■ ").hoverEvent(Component.text("Full", textColor))
                  else
                      Component.text(" □ ")
                          .hoverEvent(Component.text("Open", textColor))
                          .clickEvent(ClickEvent.runCommand("/werewolf& join " + room.roomname))
              )
              .append(
                  Component.text(room.roomname, textColor)
                      .hoverEvent(Component.text("Click to join the room.", hoverColor))
                      .clickEvent(ClickEvent.runCommand("/werewolf& join " + room.roomname))
              )
              .append(Component.text("\n"))
      linenum++
    }
    var pageControl =
        GameBook.space(lineRange - linenum)
            .append(Component.text("          "))
            .append(
                if (page != 0) Component.text(" < ").clickEvent(ClickEvent.changePage(page))
                else Component.text("   ")
            )
            .append(Component.text((page + 1).toString(), textColor))
            .append(
                if (page != pagenum)
                    Component.text(" > \n").clickEvent(ClickEvent.changePage(page + 2))
                else Component.text("   \n")
            )
    return Element(lineRange, component.append(pageControl))
  }

  override fun content(): MutableList<MutableList<Element>> {
    pagenum = (MCWerewolf.instance.rooms.size - 1) / (GameBook.MAXLINE - 2)
    var _pages: MutableList<MutableList<Element>> = mutableListOf()
    (0..pagenum).forEach { _pages.add(mutableListOf(roomlist(GameBook.MAXLINE, it))) }
    return _pages
  }

  init {
    build()
  }
}
