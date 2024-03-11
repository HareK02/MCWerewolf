package net.hareworks.werewolf.game

import net.hareworks.werewolf.MCWerewolf
import net.hareworks.werewolf.debuglog
import net.hareworks.werewolf.game.role.Role
import net.hareworks.werewolf.game.role.RoleData
import org.bukkit.configuration.file.YamlConfiguration

data class RoleConfig(var amount: Int, val config: Map<String, Any>?)

public class Config {
  companion object {
    public fun default(): Config {
      val config = YamlConfiguration()
      config.loadFromString(MCWerewolf.instance.defaultConfig.saveToString())
      return Config(config)
    }
  }
  private var yaml: YamlConfiguration

  public var name: String
  public var scenario: String

  public var roles: MutableMap<String, RoleConfig> = mutableMapOf()

  public var dayLength: Int
  public var nightLength: Int
  public var allowRejoin: Boolean
  public var createLeavingDummy: Boolean

  constructor(config: YamlConfiguration?) {
    this.yaml =
        config
            ?: let {
              YamlConfiguration().apply {
                loadFromString(MCWerewolf.instance.defaultConfig.saveToString())
              }
            }
    this.name = this.yaml.getString("profile_name") ?: "none"
    this.scenario = this.yaml.getString("scenario") ?: "default"
    this.dayLength = this.yaml.getInt("dayLength") * 20
    this.nightLength = this.yaml.getInt("nightLength") * 20
    this.allowRejoin = this.yaml.getBoolean("allowRejoin")
    this.createLeavingDummy = this.yaml.getBoolean("createLeavingDummy")

    for (role in this.yaml.getConfigurationSection("roles")?.getKeys(false) ?: listOf<String>()) {
      val roleConfig = this.yaml.getConfigurationSection("roles.$role") ?: continue
      if (Role.roles[role] == null) {
        debuglog("Config: Role $role does not exist")
        continue
      }
      this.roles[role] =
          RoleConfig(
              roleConfig.getInt("count"),
              roleConfig.getConfigurationSection("config")?.getValues(false) as? Map<String, Any>
          )
    }

    debuglog("Config: loaded $name")
  }

  val roleCount: Int
    get() {
      var count = 0
      for (role in this.roles) {
        count += role.value.amount
      }
      return count
    }

  val roleCounts: Array<Int>
    get() {
      val counts = Array(RoleData.Type.values().size) { 0 }
      for (role in this.roles) {
        counts[Role.roles[role.key]!!.type.ordinal] += role.value.amount
      }
      return counts
    }

  public fun save() {}
}
