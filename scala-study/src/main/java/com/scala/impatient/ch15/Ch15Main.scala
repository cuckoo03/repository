package com.scala.impatient.ch15

import scala.beans.BeanProperty

@unchecked
object Ch15Main extends App {
	@volatile var done = false
	@transient var trans = false
	var person = new Person()
	person.setName("n")
	println(person.getName())

}
class unchecked extends annotation.Annotation {

}
class Person {
	@BeanProperty var name: String = _
}