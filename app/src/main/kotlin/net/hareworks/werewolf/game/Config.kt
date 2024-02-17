package net.hareworks.werewolf.game

import net.hareworks.werewolf.MCWerewolf
import net.hareworks.werewolf.debuglog
import net.hareworks.werewolf.game.role.Role
import net.hareworks.werewolf.game.role.RoleObject
import org.bukkit.configuration.file.YamlConfiguration

data class RoleConfig(val name: String, var amount: Int, val config: Map<String, Any>?)

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
  public var roles: MutableList<RoleConfig> = mutableListOf()

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
      this.roles.add(
          RoleConfig(
              role,
              roleConfig.getInt("count"),
              roleConfig.getConfigurationSection("config")?.getValues(false) as? Map<String, Any>
          )
      )
    }

    debuglog("Config: loaded $name")
  }

  public fun getRoleCount(): Int {
    var count = 0
    for (role in this.roles) {
      count += role.amount
    }
    return count
  }

  public fun getRoleCounts(): Array<Int> {
    val counts = Array(RoleObject.Type.values().size) { 0 }
    for (role in this.roles) {
      counts[Role.roles[role.name]?.type?.ordinal ?: 0] += role.amount
    }
    return counts
  }

  public fun save() {}
}
