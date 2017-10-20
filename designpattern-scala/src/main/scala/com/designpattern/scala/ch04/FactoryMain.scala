package com.designpattern.scala.ch04

/**
 * @author cuckoo03
 */
object FactoryMain {
  def main(args: Array[String]) {
    val factory:Factory = new IDCardFactory()
    val card1 = factory.create("A")
    val card2 = factory.create("B")
    card1.use()
    card2.use()
  }
}