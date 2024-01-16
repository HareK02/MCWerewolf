package net.hareworks.werewolf.game


class test1: Role() {
	override var name: String = "test1"
	override var description: String = "test1"
	override var team: List<Team> = listOf(Team.Citizen)
	override var composite: Boolean = false

	override fun openBook() {
	}
}

