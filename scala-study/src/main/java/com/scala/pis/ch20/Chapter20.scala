package com.scala.pis.ch20

/**
 * @author cuckoo03
 */
object Chapter20 {
	def main(args: Array[String]) {
		val c = new Concrete()
		
		// abstract val 
		println(c.transform("a"))
		println(c.current)
		
		
		//
	}
}
trait Abstract {
	type T
	def transform(x: T): T
	val initial: T
	val current: T
}
class Concrete extends Abstract {
	type T = String
	def transform(x: String) = x + x
	val initial = "hi"
	val current = initial
}
abstract class Fruit {
	val v: String
}
abstract class Apple extends Fruit {
	val v: String
}