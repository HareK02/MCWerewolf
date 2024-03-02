package net.hareworks.werewolf.gui.book

import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.key.Key
import org.bukkit.entity.Player

public class Element {
  public var linenum: Int
  public var body: Component
  constructor(linenum: Int, body: Component) {
    this.linenum = linenum
    this.body = body
  }
}

abstract class GameBook {
  companion object {
    public fun space(line: Int): Component {
      var body = Component.text("")
      for (i in 0..line - 1) {
        body = body.append(Component.text("\n"))
      }
      return body
    }

    const val MAXLINE: Int = 11
  }

  private var linenum: Int = 0
  private var _contents: MutableList<Component> = mutableListOf()
  public fun build() {
    val _pages = mutableListOf<Component>()
    content().forEach { comp ->
      var line = 0
      var page =
          Component.text("")
              .decorations(
                  mutableMapOf(
                      TextDecoration.BOLD to TextDecoration.State.FALSE,
                      TextDecoration.STRIKETHROUGH to TextDecoration.State.FALSE,
                  )
              )
              .append(title)
      for (elem in comp) {
        page = page.append(elem.body)
        line += elem.linenum
        if (line >= MAXLINE) break
      }
      _pages.add(page.append(space(MAXLINE - line)).append(separator).append(footer))
    }
    _contents = _pages
  }

  public fun open(player: Player) {
    player.openBook(Book.book(Component.text(""), Component.text(""), _contents))
    player.playSound(Sound.sound(Key.key("item.book.page_turn"), Sound.Source.MASTER, 0.6f, 1f))
  }

  abstract fun content(): MutableList<MutableList<Element>>
}

public val baseColor: TextColor = TextColor.color(0x524e4d)
public val textColor: TextColor = TextColor.color(0x2b2b2b)
public val signColor: TextColor = TextColor.color(0x727171)
public val hoverColor: TextColor = TextColor.color(0xf8fbf8)

private val separator: Component =
    Component.text("-------------------\n", baseColor)
        .decoration(TextDecoration.STRIKETHROUGH, true)
        .decoration(TextDecoration.BOLD, false)
private val footer: Component =
    Component.text("            ")
        .decoration(TextDecoration.STRIKETHROUGH, false)
        .decoration(TextDecoration.BOLD, false)
        .append(Component.text("Wiki", textColor))
        .append(Component.text(" | ", textColor))
        .append(Component.text("GitHub", textColor))
private val title: Component =
    Component.text("---", baseColor)
        .decoration(TextDecoration.STRIKETHROUGH, true)
        .append(
            Component.text("  MC Werewolf  ", textColor)
                .decoration(TextDecoration.STRIKETHROUGH, false)
        )
        .append(Component.text("---\n"))
        .decoration(TextDecoration.STRIKETHROUGH, true)
