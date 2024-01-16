package net.hareworks.werewolf

import org.bukkit.configuration.file.YamlConfiguration
import java.io.ByteArrayOutputStream

object Lang {
	private val fallback = YamlConfiguration()
  public fun get(key: String): String {
    return App.plugin.langConfig.getString(key) ?: fallback.getString(key)!!
  }

	init {
		val result = ByteArrayOutputStream();
		val buffer: ByteArray = ByteArray(1024)
		var ImputStream = App.plugin.getResource("lang.yml")
		while (true) {
			val length = ImputStream!!.read(buffer)
			if (length == -1) break
			result.write(buffer, 0, length)
		}
		fallback.loadFromString(result.toString("UTF-8"))
	}
}
