package net.hareworks.werewolf.book

import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.hareworks.werewolf.App
import org.bukkit.entity.Player

public class Element {
  public var linenum: Int
  public var body: Component
  constructor(linenum: Int, body: Component) {
    this.linenum = linenum
    this.body = body
  }
}

public val MAXLINE: Int = 11

abstract class GameBook {
  companion object {
    public fun space(line: Int): Component {
      var body = Component.text("")
      for (i in 0..line - 1) {
        body = body.append(Component.text("\n"))
      }
      return body
    }
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
  }

  abstract fun content() : MutableList<MutableList<Element>>

  init {
		App.plugin.logger.info("GameBook init")
    build()
  }
}

private val baseColor1: TextColor = TextColor.color(0x504946)
private val baseColor2: TextColor = TextColor.color(0x000000)
private val conceptColor: TextColor = TextColor.color(0x000000)

private val separator: Component =
    Component.text("-------------------\n", baseColor1)
        .decoration(TextDecoration.STRIKETHROUGH, true)
        .decoration(TextDecoration.BOLD, false)
private val footer: Component =
    Component.text("            ")
        .decoration(TextDecoration.STRIKETHROUGH, false)
        .decoration(TextDecoration.BOLD, false)
        .append(Component.text("Wiki", baseColor1))
        .append(Component.text(" | ", baseColor1))
        .append(Component.text("GitHub", baseColor1))
private val title: Component =
    Component.text("---", baseColor1)
        .decoration(TextDecoration.STRIKETHROUGH, true)
        .append(
            Component.text("  MC Werewolf  ", baseColor2)
                .decoration(TextDecoration.STRIKETHROUGH, false)
        )
        .append(Component.text("---\n"))
        .decoration(TextDecoration.STRIKETHROUGH, true)
