package com.scala.pis.ch13

/**
 * @author cuckoo03
 */
abstract class Fruit(val name: String,
	val color: String) {
}
object Fruit {
	object Apple extends Fruit("apple", "red")
}