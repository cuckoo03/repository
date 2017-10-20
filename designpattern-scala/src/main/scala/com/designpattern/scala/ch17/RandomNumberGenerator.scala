package com.designpattern.scala.ch17

import java.util.Random

/**
 * @author cuckoo03
 */
class RandomNumberGenerator extends NumberGenerator {
  private val random = new Random()
  private var number = 0
  def getNumber(): Int = {
    number
  }
  def execute(): Unit = {
    for (i<- 0 until 20) {
      number = random.nextInt(50)
      notifyObservers()
    }
  }
}