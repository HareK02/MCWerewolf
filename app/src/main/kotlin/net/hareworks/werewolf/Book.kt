package net.hareworks.werewolf

import net.kyori.adventure.text.Component
import net.kyori.adventure.inventory.Book
import org.bukkit.entity.Player

class Book {
	
}
fun openBook(player: Player, pages: List<Component>) {
	val myBook = Book.book(
		Component.text("title"),
		Component.text("author"),
		pages
	);
	player.openBook(myBook)
}