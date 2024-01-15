package net.hareworks.werewolf

import net.hareworks.werewolf.book.BookBase
import net.hareworks.werewolf.book.Element
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextColor

object GameBook : BookBase() {
  override val baseColor1: TextColor = TextColor.color(0x000000)
  override val baseColor2: TextColor = TextColor.color(0x000000)
  override val conceptColor: TextColor = TextColor.color(0x000000)

  private fun roomlist(lineRange: Int): Element {
    var linenum = 2
    var page: Int = 0
    if (App.plugin.rooms.size > lineRange) {
      page = App.plugin.rooms.size / lineRange
    }
    var component =
        Component.text("Rooms   ", TextColor.color(0x000000))
            .append(Component.text("[Create room]\n", TextColor.color(0x000000)))

    val list = App.plugin.rooms.subList(page * lineRange, App.plugin.rooms.size % lineRange)
    if (list.isEmpty()) {
      component =
          component.append(Component.text("\n        No rooms.\n", TextColor.color(0x000000)))
      linenum += 2
    }
    for (room in list) {
      val state = Component.text(" □ ", TextColor.color(0x696969))
      component =
          component
              .append(state)
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
    val pageControl =
        BookBase.space(lineRange - linenum)
            .append(Component.text("          "))
            .append(Component.text(" < ", TextColor.color(0x000000)))
            .clickEvent(ClickEvent.runCommand("/werewolf roomlist " + (page - 1)))
            .append(Component.text((page + 1).toString(), TextColor.color(0x000000)))
            .append(
                Component.text(" > \n", TextColor.color(0x000000))
                    .clickEvent(ClickEvent.runCommand("/werewolf roomlist " + (page + 1)))
            )
    return Element(lineRange, component.append(pageControl))
  }

  override fun content(): MutableList<Element> {
    var content: MutableList<Element> = mutableListOf(roomlist(BookBase.MAXLINE))
    return content
  }
}
