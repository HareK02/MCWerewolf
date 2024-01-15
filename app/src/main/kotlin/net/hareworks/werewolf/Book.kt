package net.hareworks.werewolf.book

import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player

public class Element {
  public var linenum: Int
  public var body: Component
  constructor(linenum: Int, body: Component) {
    this.linenum = linenum
    this.body = body
  }
}

abstract class BookBase {
  companion object {
    public val MAXLINE: Int = 11
    public fun space(line: Int): Component {
      var body = Component.text("")
      for (i in 0..line - 1) {
        body = body.append(Component.text("\n"))
      }
      return body
    }
  }

  abstract val baseColor1: TextColor
  abstract val baseColor2: TextColor
  abstract val conceptColor: TextColor

  private var linenum: Int = 0
  abstract fun content(): MutableList<Element>

  public fun build(): Component {
    var page = title
    var line = 0
    for (i in content()) {
      page = page.append(i.body)
      line += i.linenum
    }
    return page.append(space(MAXLINE - line)).append(separator).append(footer)
  }

  public fun open(player: Player) {
    player.openBook(Book.book(Component.text(""), Component.text(""), build()))
  }

  private val separator: Component =
      Component.text("----------------\n", baseColor1)
          .decoration(TextDecoration.STRIKETHROUGH, true)
          .decoration(TextDecoration.BOLD, true)

  private val footer: Component =
      Component.text("            ")
          .decoration(TextDecoration.STRIKETHROUGH, false)
          .decoration(TextDecoration.BOLD, false)
          .append(Component.text("Wiki", baseColor1))
          .append(Component.text(" | ", baseColor1))
          .append(Component.text("GitHub", baseColor1))
  private val title: Component =
      Component.text("---", baseColor1)
          .append(
              Component.text("  MC Werewolf  ", baseColor2)
                  .decoration(TextDecoration.STRIKETHROUGH, false)
                  .decoration(TextDecoration.BOLD, false)
          )
          .append(Component.text("---\n"))
}
