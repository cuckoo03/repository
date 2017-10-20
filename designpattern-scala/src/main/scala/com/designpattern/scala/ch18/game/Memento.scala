package com.designpattern.scala.ch18.game

import scala.collection.mutable.ListBuffer

/**
 * @author cuckoo03
 */
class Memento(_money: Int) {
  val money = _money
  var fruits = new ListBuffer[String]()
  def getMoney(): Int = money
  def addFruit(fruit: String): Unit = fruits += fruit
}