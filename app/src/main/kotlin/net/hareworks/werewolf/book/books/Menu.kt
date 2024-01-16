package net.hareworks.werewolf.book

import net.hareworks.werewolf.App
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextColor

public object GameMenu : GameBook() {
  private var pagenum: Int = 0

  @JvmStatic
  private fun roomlist(lineRange: Int, page: Int): Element {
    var linenum = 2
    var component =
        Component.text("Rooms   ", TextColor.color(0x000000))
            .append(Component.text("[Create room]\n", TextColor.color(0x000000)))

    val list =
        App.plugin.rooms.subList(
            page * (lineRange - 2),
            App.plugin.rooms.size.coerceAtMost((page + 1) * (lineRange - 2))
        )
    App.plugin.logger.info(
        (page * (lineRange - 2)).toString() +
            " " +
            (App.plugin.rooms.size.coerceAtMost((page + 1) * (lineRange - 2))).toString()
    )
    if (list.isEmpty()) {
      component =
          component.append(Component.text("\n        No rooms.\n", TextColor.color(0x000000)))
      linenum += 2
    }
    for (room in list) {
      component =
          component
              .append(
                  if (room.isPrivate)
                      Component.text(" 🔒 ")
                          .hoverEvent(Component.text("Locked", TextColor.color(0x111111)))
                  else if (room.isFull)
                      Component.text(" ■ ")
                          .hoverEvent(Component.text("Full", TextColor.color(0x111111)))
                  else
                      Component.text(" □ ")
                          .hoverEvent(Component.text("Open", TextColor.color(0x111111)))
                          .clickEvent(ClickEvent.runCommand("/werewolf join " + room.roomname))
              )
              .append(
                  Component.text(room.roomname, TextColor.color(0x111111))
                      .hoverEvent(
                          Component.text("Click to join the room.", TextColor.color(0x111111))
                      )
                      .clickEvent(ClickEvent.runCommand("/werewolf join " + room.roomname))
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
            .append(Component.text((page + 1).toString(), TextColor.color(0x000000)))
            .append(
                if (page != pagenum)
                    Component.text(" > \n").clickEvent(ClickEvent.changePage(page + 2))
                else Component.text("   \n")
            )
    return Element(lineRange, component.append(pageControl))
  }

  override fun content(): MutableList<MutableList<Element>> {
    pagenum = (App.plugin.rooms.size-1) / (MAXLINE - 2)
    App.plugin.logger.info(pagenum.toString())
    var _pages: MutableList<MutableList<Element>> = mutableListOf()
    (0..pagenum).forEach { _pages.add(mutableListOf(roomlist(MAXLINE, it))) }
    return _pages
  }
}
