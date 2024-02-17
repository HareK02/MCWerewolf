package net.hareworks.kommandlib

import kotlin.collections.listOf
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin

class KommandLib(plugin: JavaPlugin, vararg routes: Pair<String, Argument>) {
  val routes = routes.toMap()
  init {
    val f = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
    f.isAccessible = true
    val commandMap = f.get(Bukkit.getServer()) as CommandMap
    for ((name, _) in routes) {
      commandMap.register(
          plugin.getName(),
          (PluginCommand::class
                  .java
                  .declaredConstructors
                  .first()
                  .apply { isAccessible = true }
                  .newInstance(name, plugin) as
                  PluginCommand)
              .apply {
                this.name = name
                this.setExecutor { sender, _, alias, args ->
                  val route = getLastRoute(arrayOf(alias, *args))
                  if (route.size == args.size + 1) route.last().onCommand(sender, args)
                  true
                }
                this.tabCompleter = TabCompleter { sender, _, alias, args ->
                  val route = getLastRoute(arrayOf(alias, *args))
                  if (route.size == args.size) route.last().getCompletList(sender, args)
                  else listOf()
                }
              }
      )
    }
  }

  fun getLastRoute(args: Array<String>): List<Argument> {
    val list = mutableListOf<Argument>(routes[args[0]] ?: throw Exception("Invalid command"))
    var i = 1
    while (i + 1 <= args.size) {
      if (list.last().routes.isEmpty()) break
      val route =
          list.last().routes.values.sortedBy { it.priority }.find { it.typeCheck(args[i]) } ?: break
      list.add(route)
      i += route.unit
    }
    return list
  }

  fun unregister() {
    val f = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
    f.isAccessible = true
    val commandMap = f.get(Bukkit.getServer()) as CommandMap
    for ((name, _) in routes) {
      commandMap.getCommand(name)?.unregister(commandMap)
    }
  }
}

abstract class Argument(
    vararg routes: Pair<String, Argument>,
    val execute: (CommandSender, Array<Any>) -> Unit
) {
  val routes = routes.toMap()
  var name: String = ""
    get() = field
    protected set(value) {
      field = value
    }
  var permission: String = ""
    set(value) {
      field = value
      if (value.isEmpty()) return
      for ((_, route) in routes) {
        route.permission = value + "." + route.name
      }
    }

  init {
    for ((name, route) in routes) {
      route.name = name
    }
  }

  abstract var priority: Int
  open var unit: Int = 1
  abstract fun onCommand(sender: CommandSender, args: Array<String>)
  abstract fun typeCheck(arg: String): Boolean
  abstract fun toValue(args: Array<String>): Any

  abstract fun suggest(sender: CommandSender, args: Array<String>): List<String>

  fun getCompletList(sender: CommandSender, args: Array<String>): List<String> {
    return routes
        .values
        .filter { sender.hasPermission(it.permission) }
        .map { it.suggest(sender, args) }
        .flatten()
  }
}

class Route(vararg routes: Pair<String, Argument>, execute: (CommandSender, Array<Any>) -> Unit) :
    Argument(*routes, execute = execute) {
  override var priority: Int = 2
  override fun onCommand(sender: CommandSender, args: Array<String>) {
    execute(sender, args.map { it }.toTypedArray())
  }
  override fun typeCheck(arg: String): Boolean {
    return this.name == arg
  }
  override fun toValue(args: Array<String>): Any {
    return args.joinToString(" ")
  }

  override fun suggest(sender: CommandSender, args: Array<String>): List<String> {
    return if (sender.hasPermission(this.permission) && this.name.startsWith(args.last()))
        listOf(this.name)
    else listOf()
  }
}

class Text(vararg routes: Pair<String, Argument>, execute: (CommandSender, Array<Any>) -> Unit) :
    Argument(*routes, execute = execute) {
  override var priority: Int = 0
  override fun typeCheck(arg: String): Boolean {
    return true
  }
  override fun toValue(args: Array<String>): Any {
    return args.joinToString(" ")
  }
  override fun onCommand(sender: CommandSender, args: Array<String>) {
    execute(sender, args.map { it }.toTypedArray())
  }

  override fun suggest(sender: CommandSender, args: Array<String>): List<String> {
    return listOf(args.last())
  }
}

class Integer(vararg routes: Pair<String, Argument>, execute: (CommandSender, Array<Any>) -> Unit) :
    Argument(*routes, execute = execute) {
  override var priority: Int = 1
  override fun typeCheck(arg: String): Boolean {
    return arg.toIntOrNull() != null
  }

  override fun onCommand(sender: CommandSender, args: Array<String>) {
    execute(sender, args.map { it.toInt() }.toTypedArray())
  }

  override fun toValue(args: Array<String>): Any {
    return args[0].toInt()
  }

  override fun suggest(sender: CommandSender, args: Array<String>): List<String> {
    return listOf()
  }
}

class Position(
    vararg routes: Pair<String, Argument>,
    execute: (CommandSender, Array<Any>) -> Unit
) : Argument(*routes, execute = execute) {
  override var priority: Int = 3
  override var unit: Int = 3
  override fun typeCheck(arg: String): Boolean {
    return true
  }

  override fun onCommand(sender: CommandSender, args: Array<String>) {
    execute(sender, args.map { it }.toTypedArray())
  }

  override fun toValue(args: Array<String>): Any {
    return Triple(args[0].toDouble(), args[1].toDouble(), args[2].toDouble())
  }

  override fun suggest(sender: CommandSender, args: Array<String>): List<String> {
    return listOf()
  }
}
