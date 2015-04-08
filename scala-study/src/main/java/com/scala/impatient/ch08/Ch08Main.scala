package com.scala.impatient.ch08

object Ch08Main extends App {
	def person = new Person()
	println(person)
	def emp = new Emp()
	println(emp)

	//type checking
	if (person.isInstanceOf[Person]) {
		println("person")
	}
	
}
class Person {
	override def toString = getClass.getName()
}
class Emp extends Person {
	override def toString = super.toString + "person"
}