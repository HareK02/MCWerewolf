package net.hareworks.werewolf

import net.hareworks.werewolf.book.GameMenu
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Werewolf : CommandExecutor {
  override fun onCommand(
      sender: CommandSender,
      command: Command,
      label: String,
      args: Array<String>
  ): Boolean {
    if (args.size == 0) {
      return help(sender)
    }
    when (args[0]) {
      "create" -> return create(sender, args) // /werewolf create [roomname]
      "leave" -> return leave(sender) // /werewolf leave
      else -> return help(sender)
    }
  }

  private fun help(sender: CommandSender): Boolean {
    sender.sendMessage("-Werewolf Help-")
    sender.sendMessage("/werewolf start")
    return true
  }

  private fun create(sender: CommandSender, args: Array<String>): Boolean {
    // if (App.plugin.hasRoom(sender as Player)) {
    //   sender.sendMessage("You are already in a room.")
    //   return true
    // }
    var roomname: String
    if (args.size > 2) {
      sender.sendMessage("Too many arguments.")
      return true
    }
    if (args.size == 2) {
      if (App.plugin.getRoom(args[1]) != null) {
        sender.sendMessage("Room already exists.")
        return true
      }
      roomname = args[1]
    } else roomname = sender.name
    App.plugin.rooms.add(Room(sender as Player, roomname))
    GameMenu.build()
    sender.sendMessage("You created the room.")
    return true
  }

  private fun leave(sender: CommandSender): Boolean {
    if (!App.plugin.hasRoom(sender as Player)) {
      sender.sendMessage("You are not in a room.")
      return true
    }
    val room = App.plugin.rooms.find { room -> room.players.contains(sender) }
    room?.players?.remove(sender)
    sender.sendMessage("You left the room.")
    return true
  }
}

class Wbook : CommandExecutor {
  override fun onCommand(
      sender: CommandSender,
      command: Command,
      label: String,
      args: Array<String>
  ): Boolean {
    if (args.size == 0) {
      GameMenu.open(sender as Player)
      return true
    }
    when (args[0]) {
      "create" -> return menu(sender) // /wbook menu
			"help" -> return help(sender) // /wbook help
      else -> return help(sender)
    }
  }

  private fun help(sender: CommandSender): Boolean {
    sender.sendMessage(Lang.get("wbook.test"))
    sender.sendMessage(Lang.get("wbook.fallback_test"))
    return true
  }

  private fun menu(sender: CommandSender): Boolean {
    if (sender !is Player) {
      sender.sendMessage("You must be a player.")
      return true
    }
    GameMenu.open(sender)
    return true
  }
}