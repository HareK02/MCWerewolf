package net.hareworks.werewolf

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player

private class Item {
  var linenum: Int = 0
  var body: Component = Component.text("body")
  constructor(linenum: Int, body: Component) {
    this.linenum = linenum
    this.body = body
  }
}

private val MAXLINE = 14
private val separator: Component =
    Component.text("---------------\n")
        .decoration(TextDecoration.STRIKETHROUGH, true)
        .decoration(TextDecoration.BOLD, true)
private val title: Component =
    Component.text("---", TextColor.color(0xd3d3d3))
        .append(
            Component.text("  MC Werewolf  ", TextColor.color(0x696969))
                .decoration(TextDecoration.STRIKETHROUGH, false)
                .decoration(TextDecoration.BOLD, false)
        )
        .append(Component.text("---\n"))
private val footer: Component =
    Component.text("            ")
        .decoration(TextDecoration.STRIKETHROUGH, false)
        .decoration(TextDecoration.BOLD, false)
        .append(Component.text("Wiki", TextColor.color(0x696969)))
        .append(Component.text(" | "))
        .append(Component.text("GitHub", TextColor.color(0x696969)))

private fun space(line: Int): Item {
  var body = Component.text("")
  for (i in 0..line - 1) {
    body = body.append(Component.text("\n"))
  }
  return Item(line, body)
}

private fun roomlist(): Item {
  var linenum = 1
  var roomlist = Component.text("RoomList:\n", TextColor.color(0x000000))
  for (room in App.plugin.rooms) {
    roomlist = roomlist.append(Component.text(room.roomname + "\n", TextColor.color(0x000000)))
    linenum++
  }
  return Item(linenum, roomlist)
}

fun GameBook(player: Player): Boolean {
  if (!App.plugin.hasRoom(player)) { // GameMenu
    var roomlist = roomlist()
    openBook(
        player,
        listOf(
            title
                .append(roomlist.body)
                .append(space(MAXLINE - 3 - roomlist.linenum).body)
                .append(separator)
                .append(footer)
        )
    )
  } else { // RoomMenu
    openBook(
        player,
        listOf(
            Component.text("---", TextColor.color(0xd3d3d3))
                .append(
                    Component.text("  MC Werewolf  ", TextColor.color(0x696969))
                        .decoration(TextDecoration.STRIKETHROUGH, false)
                        .decoration(TextDecoration.BOLD, false)
                )
                .append(Component.text("---\n"))
                .append(Component.text("Room: ", TextColor.color(0x696969)))
                .append(Component.text("RoomName\n", TextColor.color(0x000000)))
        )
    )
  }
  return true
}
