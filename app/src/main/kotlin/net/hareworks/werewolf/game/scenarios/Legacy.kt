package net.hareworks.werewolf.game.scenario

import net.hareworks.werewolf.game.Game

/*
 * Scenario: Legacy
 * Description:
 *   ユニークな役職が存在しない、古典的な人狼ゲーム。
 *   人狼が全滅するか、村人が全滅するか、どちらかが勝利するまでゲームが続く。
 */
class LegacyScenario : Scenario() {
  override lateinit var game: Game


	override fun onGameStart() {
		game.broadcast("scenario:Game started.")
	}
	
	override fun onPlayerDeath() {
		game.broadcast("scenario:Player died.")
	}
}