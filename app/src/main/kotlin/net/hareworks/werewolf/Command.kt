package net.hareworks.werewolf

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.hareworks.werewolf.GameBook
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Command : CommandExecutor {
  override fun onCommand(
      sender: CommandSender,
      command: Command,
      label: String,
      args: Array<String>
  ): Boolean {
    if (args.size == 0) return GameBook(sender as Player)
    when (args[0]) {
      else -> {
        return help(sender)
      }
    }
  }
  private fun help(sender: CommandSender): Boolean {
    sender.sendMessage("-Werewolf Help-")
    sender.sendMessage("/werewolf start")
    return true
  }
}

fun create(sender: CommandSender, args: Array<String>): Boolean {
  if (args.size != 2) args[1] = sender.getName()
  Game(sender as Player, args[1])
  return true
}
