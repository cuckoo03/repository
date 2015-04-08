package com.scala.impatient.ch17

object Ch17Main extends App {
	val p = new Pair(42, "s")
	val p2 = new Pair2("42", "s")
	println(p.first)
	println(p2.first)

}
class Pair[T, S](val first: T, val second: S) {

}
class Pair2[Int, String](val first: Int, val second: String) {

}