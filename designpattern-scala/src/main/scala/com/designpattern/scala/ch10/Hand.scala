package com.designpattern.scala.ch10

import scala.collection.mutable.ArrayBuffer

/**
 * @author cuckoo03
 */
object Hand {
  val HANDVALUE_GUU = 0
  val HANDVALUE_CHO = 1
  val HANDVALUE_PAA = 2
  val hand = new ArrayBuffer[Hand]()
  hand += new Hand(HANDVALUE_GUU)
  hand += new Hand(HANDVALUE_CHO)
  hand += new Hand(HANDVALUE_PAA)
  val name = new ArrayBuffer[String]()
  name += "주먹"
  name += "가위"
  name += "보"
  def getHand(handvalue: Int): Hand = new Hand(handvalue)
}

class Hand(val _handvalue: Int) {
  private val handvalue = _handvalue
  def isStrongerThan(h: Hand): Boolean = fight(h) == 1
  def isWeakerThan(h: Hand): Boolean = fight(h) == -1
  def fight(h: Hand): Int = {
    if (this == h) {
      return 0
    } else if ((this.handvalue + 1) % 3 == h.handvalue) {
      return 1
    } else {
      return -1
    }
  }
}