package com.scala.pis.ch22

/**
 * @author cuckoo03
 */
object Chapter22 {
	def main(args: Array[String]) {
		val list1 = Nil
		println(list1.toString())
		
		val apples = new Apple::Nil
		println(apples)
		val fruits = new Orange::apples
		println(fruits)
		
		// listbuffer
		
	}
}
abstract class Fruit
class Apple extends Fruit
class Orange extends Fruit