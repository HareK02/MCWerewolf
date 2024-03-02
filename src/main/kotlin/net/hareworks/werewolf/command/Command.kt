package net.hareworks.werewolf

import net.hareworks.guilib.*
import net.hareworks.kommandlib.*
import net.hareworks.werewolf.gui.book.GameMenu
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

fun command(): KommandLib {
  val werewolf =
      Route(
              "menu" to
                  Route(
                      "join" to
                          Route(
                              "roomname" to
                                  Text { sender, args ->
                                    (sender as Player).run {
                                      performCommand("ww join " + args.last())
                                      performCommand("ww menu")
                                    }
                                  },
                          ) { sender, _ ->
                            (sender as Player).run {
                              performCommand("ww join")
                              performCommand("ww menu")
                            }
                          },
                      "create" to
                          Route { sender, _ ->
                            (sender as Player).run {
                              performCommand("ww create")
                              performCommand("ww menu")
                            }
                          },
                      "config" to
                          Route(
                              "role" to
                                  Route { sender, _ ->
                                    val room = MCWerewolf.instance.getRoom(sender as Player)
                                    if (room == null)
                                        sender.sendMessage(Lang.get("command.werewolf.not_in_room"))
                                    else if (room.inGame)
                                        sender.sendMessage(
                                            Lang.get("command.werewolf.already_started")
                                        )
                                    else room.configBook.roleConfig.open(sender)
                                  }
                          ) { sender, _ ->
                            val room = MCWerewolf.instance.getRoom(sender as Player)
                            if (room == null)
                                sender.sendMessage(Lang.get("command.werewolf.not_in_room"))
                            else if (room.inGame)
                                sender.sendMessage(Lang.get("command.werewolf.already_started"))
                            else room.configBook.open(sender)
                          },
                  ) { sender, _ ->
                    if (sender !is Player) {
                      sender.sendMessage(Lang.get("command.must_be_player"))
                    } else {
                      val room = MCWerewolf.instance.getRoom(sender)
                      if (room == null) GameMenu.open(sender)
                      else if (room.inGame) room.Game?.getGamePlayer(sender)?.openBook()
                      else room.book.open(sender)
                    }
                  },
              "create" to
                  Route(
                      "roomname" to
                          Text { sender, args ->
                            if (MCWerewolf.instance.hasRoom(sender as Player))
                                sender.sendMessage(Lang.get("command.werewolf.already_joined"))
                            else {
                              val roomname = args[1] as String
                              if (MCWerewolf.instance.newRoom(sender, roomname))
                                  sender.sendMessage(Lang.get("command.werewolf.created"))
                              else sender.sendMessage(Lang.get("command.werewolf.room_name_exists"))
                            }
                          },
                  ) { sender, _ ->
                    if (MCWerewolf.instance.hasRoom(sender as Player))
                        sender.sendMessage(Lang.get("command.werewolf.already_joined"))
                    else {
                      var roomname: String = sender.name
                      if (MCWerewolf.instance.newRoom(sender, roomname))
                          sender.sendMessage(Lang.get("command.werewolf.created"))
                      else sender.sendMessage(Lang.get("command.werewolf.room_name_exists"))
                    }
                  },
              "leave" to Route { sender, _ -> sender.sendMessage("leave") },
              "join" to
                  Route(
                      "roomname" to
                          Text { sender, args ->
                            if (MCWerewolf.instance
                                    .getRoom(args.last() as String)
                                    ?.join(sender as Player) == true
                            )
                                sender.sendMessage(Lang.get("command.werewolf.joined"))
                            else sender.sendMessage(Lang.get("command.werewolf.room_not_exists"))
                          },
                  ) { _, _ -> },
              "start" to
                  Route { sender, _ ->
                    val room = MCWerewolf.instance.getRoom(sender as Player)
                    if (room == null) sender.sendMessage(Lang.get("command.werewolf.not_in_room"))
                    else if (room.inGame)
                        sender.sendMessage(Lang.get("command.werewolf.already_started"))
                    else room.start()
                  },
              "forceend" to Route { sender, _ -> sender.sendMessage("forceend") },
              "test" to
                  Route { sender, _ ->
                    var bool1 = true
                    var number1 = 0
                    var number2 = 0
                    CUI(Vec2(-80, -30)) {
                          register(
                              Element("Test GUI").apply {
                                width = 20
                                position = CUIComponent.Position.CENTER
                              },
                              Element("v1.0").apply {
                                width = 0
                                position = CUIComponent.Position.RIGHT
                              },
                              NewLine(),
                              Element("-".repeat(20)),
                              NewLine(),
                              *arrayOf(
                                      Interactable(
                                          "Toggle",
                                          ToggleButton(Ref({ bool1 }, { bool1 = it }))
                                      ),
                                      Interactable(
                                          "Number",
                                          Number(Ref({ number1 }, { number1 = it }), 0, 10)
                                      ),
                                      Interactable(
                                          "Slider",
                                          Slider(Ref({ number2 }, { number2 = it }), 0, 10)
                                      ),
                                      Interactable(
                                          "NextPage",
                                          Anchor(
                                              "NextPage",
                                              CUI(Vec2(-80, -30)) {
                                                register(
                                                    Element("Test GUI").apply {
                                                      width = 20
                                                      position = CUIComponent.Position.CENTER
                                                    },
                                                    Element("v1.0").apply {
                                                      width = 0
                                                      position = CUIComponent.Position.RIGHT
                                                    },
                                                    NewLine(),
                                                    Element("-".repeat(20)),
                                                    NewLine(),
                                                    Element("This is the next page"),
                                                )
                                              }
                                          )
                                      ),
                                  )
                                  .also { it.forEach { it.width = 20 } }
                          )
                        }
                        .open(sender as Player)
                  },
          ) { sender, _ -> (sender as Player).performCommand("ww menu") }
          .apply { permission = "werewolf.player" }
  return KommandLib(
      JavaPlugin.getPlugin(MCWerewolf::class.java),
      "ww" to werewolf,
      "werewolf" to werewolf,
  )
}
