package net.hareworks.werewolf.game.scenario

import net.hareworks.werewolf.game.Game

abstract class Scenario {
  companion object {
    public fun load(name: String, game: Game): Scenario {
			return when (name) {
				"legacy" -> LegacyScenario()
				else -> LegacyScenario()
			}.apply { this.game = game }
		}
  }
	abstract var game: Game

	open fun onGameStart() {}
	open fun onGameEnd() {}
	open fun onDayEnd() {}
	open fun onNightEnd() {}
	open fun onPlayerDeath() {}
}