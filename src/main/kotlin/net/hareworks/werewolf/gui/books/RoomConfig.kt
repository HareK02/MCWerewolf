package net.hareworks.werewolf.gui.book

import net.hareworks.werewolf.Lang
import net.hareworks.werewolf.debuglog
import net.hareworks.werewolf.game.Config
import net.hareworks.werewolf.game.role.Role
import net.hareworks.werewolf.game.role.RoleObject
import net.hareworks.werewolf.gui.inventory.RoleConfig
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

public class RoomConfigMenu(config: Config) : GameBook() {
  private val config: Config = config
  public val roleConfig: RoleConfig = RoleConfig(config)

  private fun indent(level: Int): Component {
    var component = Component.empty()
    for (i in 0 until level) {
      component = component.append(Component.text("  "))
    }
    return component
  }

  private fun getlist(roletype: RoleObject.Type): Array<Component> {
    return config
        .roles
        .filter { it -> Role.roles[it.name]!!.type == roletype }
        .map {
          Component.text(" - " + it.name + " : " + it.amount + "\n", textColor)
              .hoverEvent(Component.text(Role.roles[it.name]!!.description + "\n", hoverColor))
        }
        .toTypedArray()
  }

  private fun section(
      camp: String,
      color: Int,
  ): Component {
    return Component.text("● ", signColor)
        .append(
            Component.text(Lang.get("game.role-type.$camp.name") + "\n", TextColor.color(color))
                .hoverEvent(
                    Component.text(Lang.get("game.role-type.$camp.name") + " \n", hoverColor)
                        .append(
                            Component.text(Lang.get("game.role-type.$camp.desc") + "\n", hoverColor)
                        )
                )
        )
  }

  private fun rolelist(): MutableList<Component> {
    var component: MutableList<Component> =
        mutableListOf(
            *getlist(RoleObject.Type.Common).let { arrayOf(section("common", 0x856859), *it) },
            *getlist(RoleObject.Type.Citizen).let { arrayOf(section("citizen", 0x028760), *it) },
            *getlist(RoleObject.Type.Werewolf).let { arrayOf(section("werewolf", 0xc9171e), *it) },
            *getlist(RoleObject.Type.Third).let { arrayOf(section("third-party", 0x165e83), *it) }
        )
    return component
  }

  private fun rolemenu(): Array<Element> {
    val arr = mutableListOf<Element>()
    val fulllist = rolelist()
    val rolePageNum = (fulllist.size - 1) / (GameBook.MAXLINE - 2)
    debuglog("RolePageNum: $rolePageNum")
    for (page in 0..rolePageNum) {
      var component =
          Component.empty()
              .append(
                  Component.text(
                      "Config > ",
                      textColor,
                  )
              )
              .append(Component.text("Roles", textColor, TextDecoration.UNDERLINED))
              .append(Component.text("   "))
              .append(
                  Component.text("[back]\n", textColor)
                      .hoverEvent(Component.text("Back to Config Menu", hoverColor))
                      .clickEvent(ClickEvent.runCommand("/werewolf menu"))
              )
      val list =
          fulllist.subList(
              page * (GameBook.MAXLINE - 2),
              fulllist.size.coerceAtMost((page + 1) * (GameBook.MAXLINE - 2))
          )
      var line = 2
      for (role in list) {
        component = component.append(role)
        line++
      }
      arr.add(
          Element(
              GameBook.MAXLINE,
              component
                  .append(GameBook.space(GameBook.MAXLINE - line))
                  .append(
                      Component.text("          ")
                          .append(
                              if (page != 0)
                                  Component.text(" < ").clickEvent(ClickEvent.changePage(page))
                              else Component.text("   ")
                          )
                          .append(Component.text((page + 1).toString(), textColor))
                          .append(
                              if (page != rolePageNum)
                                  Component.text(" > ").clickEvent(ClickEvent.changePage(page + 2))
                              else Component.text("   ")
                          )
                  )
          )
      )
    }
    return arr.toTypedArray()
  }

  override fun content(): MutableList<MutableList<Element>> {
    var _pages: MutableList<MutableList<Element>> = mutableListOf()
    _pages.add(
        mutableListOf(
            // *rolemenu(),
            Element(
                1,
                Component.text("RoleConfig")
                    .hoverEvent(Component.text("Click to configure the role.", hoverColor))
                    .clickEvent(ClickEvent.runCommand("/ww menu config role"))
            )
        )
    )
    return _pages
  }
}
