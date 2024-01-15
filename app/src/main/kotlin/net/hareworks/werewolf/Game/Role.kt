package net.hareworks.werewolf.game

public enum class Team {
	Villager,
	Werewolf,
	Neutral
}

abstract class Role {
	abstract var name: String
	abstract var description: String
	abstract var team: List<Team>
	abstract var composite: Boolean

  abstract fun openBook()
}