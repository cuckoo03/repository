package com.scala.impatient.ch05

import scala.beans.BeanProperty
import scala.collection.mutable.ArrayBuffer

object Ch05Main extends App {
  val counter = new Counter()
  counter.increment()
  println(counter.current)

  //getter setter
  var person = new Person()
  println(person.age)
  person.age = 1
  person.age_(2)
  println(person.age)

  // bean property
  var p2: Person2 = new Person2()
  p2.name = "a"
  p2.getName()
  p2.setName("b")
  println(p2.name)
  //
  var p1 = new Person
  var p3 = new Person("a")
  println(p3)

  var p4: Person3 = new Person3("p4", 4)
  println(p4.name)
  println(p4.age)

  //inner class
  var chatter = new Network
  var myface = new Network
  println(chatter.join("a"))
  println(myface.join("a"))
}

class Counter {
  private var value = 0
  def increment() {
    value += 1
  }
  def current() = value
}
class Person {
  var age = 0
  private var name = ""
  def age_(newValue: Int) {
    age = newValue
  }
  def this(name: String) {
    this()
    this.name = name
  }
}
class Person2 {
  @BeanProperty var name: String = _
}
class Person3(val name: String, val age: Int) {
}
class Network {
  class Member(val name: String) {
    val c = new ArrayBuffer[Member]
  }
  private val mm = new ArrayBuffer[Member]
  def join(name: String) = {
    val m = new Member(name)
    mm += m
    m
  }
}
