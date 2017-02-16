package com.scala.pis.ch29

/**
 * @author cuckoo03
 */
class Recipe(
		val name: String,
		val ingredients: List[Food],
		val instructions: String) {
	override def toString = name
}