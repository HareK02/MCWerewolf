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
      return auto(sender)
    }
    when (args[0]) {
      "create" -> create(sender, args) // /werewolf create [roomname]
      "leave" -> leave(sender) // /werewolf leave
      "join" -> join(sender, args) // /werewolf join [roomname]
      "start" -> start(sender) // /werewolf start
      "forceend" -> forceend(sender) // /werewolf forceend
      else -> return help(sender)
    }
    if (command.name == "werewolf&") auto(sender)
    return true
  }

  private fun auto(sender: CommandSender): Boolean {
    if (sender !is Player) {
      sender.sendMessage(Lang.get("command.must_be_player"))
      return true
    }
    val room = MCWerewolf.instance.getRoom(sender)
    if (room == null) GameMenu.open(sender)
    else if (room.inGame) room.Game.getGamePlayer(sender)?.openBook()
     else room.book.open(sender)
    return true
  }

  private fun help(sender: CommandSender): Boolean {
    sender.sendMessage(Lang.get("command.werewolf.help"))
    return true
  }

  private fun create(sender: CommandSender, args: Array<String>) {
    if (MCWerewolf.instance.hasRoom(sender as Player))
        return sender.sendMessage(Lang.get("command.werewolf.already_joined"))
    var roomname: String
    if (args.size > 2) return sender.sendMessage(Lang.get("command.werewolf.invalid_argument"))
    if (args.size == 2) roomname = args[1] else roomname = sender.name
    if (MCWerewolf.instance.newRoom(sender, roomname))
        sender.sendMessage(Lang.get("command.werewolf.created"))
    else sender.sendMessage(Lang.get("command.werewolf.room_name_exists"))
  }

  private fun join(sender: CommandSender, args: Array<String>) {
    if (MCWerewolf.instance.hasRoom(sender as Player))
        return sender.sendMessage(Lang.get("command.werewolf.already_joined"))
    if (args.size != 2) return sender.sendMessage(Lang.get("command.invalid_argument"))
    val room = MCWerewolf.instance.getRoom(args[1])
    if (room == null) return sender.sendMessage(Lang.get("command.werewolf.room_not_found"))
    if (room.isFull) return sender.sendMessage(Lang.get("command.werewolf.room_full"))
    if (room.join(sender)) sender.sendMessage(Lang.get("command.werewolf.joined"))
    else sender.sendMessage(Lang.get("command.werewolf.failed_to_join"))
  }

  private fun leave(sender: CommandSender) {
    val room = MCWerewolf.instance.getRoom(sender as Player)
    if (room == null) return sender.sendMessage(Lang.get("command.werewolf.not_in_room"))
    if (room.leave(sender)) sender.sendMessage(Lang.get("command.werewolf.left"))
    if (room.players.size != 0) return
    MCWerewolf.instance.deleteRoom(room)
    sender.sendMessage(Lang.get("command.werewolf.disbanded_by_no_player"))
  }

  private fun start(sender: CommandSender) {
    val room = MCWerewolf.instance.getRoom(sender as Player)
    if (room == null) return sender.sendMessage(Lang.get("command.werewolf.not_in_room"))
    if (room.inGame) return sender.sendMessage(Lang.get("command.werewolf.already_started"))
    room.start()
  }

  private fun forceend(sender: CommandSender) {
    val room = MCWerewolf.instance.getRoom(sender as Player)
    if (room == null) return sender.sendMessage(Lang.get("command.werewolf.not_in_room"))
    if (!room.inGame) return sender.sendMessage(Lang.get("command.werewolf.not_started"))
    room.Game.end()
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
      "help" -> return help(sender) // /wbook help
      else -> return help(sender)
    }
  }

  private fun help(sender: CommandSender): Boolean {
    sender.sendMessage(Lang.get("command.wbook.help"))
    return true
  }

  private fun menu(sender: CommandSender): Boolean {
    if (sender !is Player) {
      sender.sendMessage(Lang.get("command.must_be_player"))
      return true
    }
    GameMenu.open(sender)
    return true
  }
}
