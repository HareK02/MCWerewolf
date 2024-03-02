package net.hareworks.werewolf

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
                  ) { sender, args ->
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
                  ) { sender, _ -> },
              "start" to
                  Route { sender, _ ->
                    val room = MCWerewolf.instance.getRoom(sender as Player)
                    if (room == null) sender.sendMessage(Lang.get("command.werewolf.not_in_room"))
                    else if (room.inGame)
                        sender.sendMessage(Lang.get("command.werewolf.already_started"))
                    else room.start()
                  },
              "forceend" to Route { sender, _ -> sender.sendMessage("forceend") },
          ) { sender, _ -> (sender as Player).performCommand("ww menu") }
          .apply { permission = "werewolf.player" }
  return KommandLib(
      JavaPlugin.getPlugin(MCWerewolf::class.java),
      "ww" to werewolf,
      "werewolf" to werewolf,
  )
}
