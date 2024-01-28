package net.hareworks.werewolf.game

import net.hareworks.werewolf.MCWerewolf
import net.hareworks.werewolf.game.role.Role
import org.bukkit.configuration.file.YamlConfiguration

public class Config {
  companion object {
    public fun default(): Config {
      val config = YamlConfiguration()
      config.loadFromString(MCWerewolf.instance.defaultConfig.saveToString())
      return Config(config)
    }
  }
  private var yaml: YamlConfiguration

  public var scenario: String
  public var roles: MutableMap<Role, Int> = mutableMapOf()

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
    this.scenario = this.yaml.getString("scenario") ?: "default"
    this.dayLength = this.yaml.getInt("dayLength")
    this.nightLength = this.yaml.getInt("nightLength")
    this.allowRejoin = this.yaml.getBoolean("allowRejoin")
    this.createLeavingDummy = this.yaml.getBoolean("createLeavingDummy")

    for (role in this.yaml.getConfigurationSection("roles")?.getKeys(false) ?: listOf<String>()) {
      val roleObj = Role.valueOf(role) ?: continue
      this.roles.put(roleObj, this.yaml.getInt("roles.$role.count"))
      roleObj.setConfig(
          this.yaml.getConfigurationSection("roles.$role")?.getValues(false) ?: mapOf()
      )
    }
  }

  public fun save() {}
}
