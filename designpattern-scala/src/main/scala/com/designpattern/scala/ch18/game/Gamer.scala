package com.designpattern.scala.ch18.game

import java.util.Random

import scala.collection.mutable.ListBuffer

/**
 * @author cuckoo03
 */
class Gamer(_money: Int) {
  private var money = _money
  private var fruits = new ListBuffer[String]()
  private val random = new Random()
  private val fruitsName = Array("apple", "grape", "banana")

  def getMoney(): Int = money
  def bet(): Unit = {
    var dice = random.nextInt(6) + 1
    if (dice == 1) {
      money += 100
      println("increase money")
    } else if (dice == 2) {
      money /= 2
      println("decrease money")
    } else if (dice == 6) {
      var f = getFruit()
      println(s"add $f")
      fruits += f
    }
  }
  def createMemento(): Memento = {
    val m = new Memento(money)
    val it = fruits.iterator
    while (it.hasNext) {
      val f = it.next()
      if (f.startsWith("taist"))
        m.addFruit(f)
    }
    return m
  }
  def restoreMemento(memento: Memento): Unit = {
    this.money = memento.money
    this.fruits = memento.fruits
  }
  def getFruit(): String = {
    var prefix = ""
    if (random.nextBoolean()) {
      prefix = "taist "
    }
    return prefix + fruitsName(random.nextInt(fruitsName.length))
  }
  override def toString() = s"[money:$money, fruits:" + fruits.mkString(",") + "]"
}