package net.hareworks.werewolf;

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class Command : CommandExecutor {
    override fun onCommand(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
    ): Boolean {
        when (command.name) {
            "werewolf" -> return werewolf(sender, args)
        }
        return true
    }
}

fun werewolf(sender: CommandSender, args: Array<String>): Boolean {
	return true
}