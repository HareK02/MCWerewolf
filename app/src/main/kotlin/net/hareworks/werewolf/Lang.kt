package net.hareworks.werewolf

import org.bukkit.configuration.file.YamlConfiguration
import java.io.ByteArrayOutputStream

object Lang {
	private val fallback = YamlConfiguration()
  public fun get(key: String): String {
    return MCWerewolf.instance.langConfig.getString(key) ?: fallback.getString(key) ?: "non-existent key: $key"
  }

	init {
		val result = ByteArrayOutputStream();
		val buffer: ByteArray = ByteArray(1024)
		var ImputStream = MCWerewolf.instance.getResource("lang.yml")
		while (true) {
			val length = ImputStream!!.read(buffer)
			if (length == -1) break
			result.write(buffer, 0, length)
		}
		fallback.loadFromString(result.toString("UTF-8"))
	}
}

fun String.assign(vararg args: String): String {
  var result = this
  for (i in 0..args.size - 1) {
    result = result.replace("%$i", args[i])
  }
  return result
}