package com.scala.impatient.ch06

object Ch06Main extends App {
	println("a")
	A.aa()

	Action.undo

	//apply method
	Account.apply("apply")

	// application object

	// enueration
	println(TrafficLightColor.red)
}
object A { //컴패니언 오브젝트
	def aa() {
		println("aa")
	}
}
abstract class SuperAction {
	def undo() {
		println("super undo")
	}
}
object Action extends SuperAction {
	override def undo() {
		println("action do")
	}
}
class Account(str: String) {
	private var _str = str
	def Account() {
		println(_str)
	}
}
object Account {
	def apply(str: String) = new Account(str)
}
object TrafficLightColor extends Enumeration {
	val red = Value(0, "a")
}