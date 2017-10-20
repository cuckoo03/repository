package com.designpattern.scala.ch20

import java.util.concurrent.ConcurrentHashMap

class BigCharFactory {
  private val pool = new ConcurrentHashMap[String, BigChar]()

  def getBigChar(charName: String): BigChar = {
    var bc = pool.get(charName)
    if (bc == null) {
      bc = new BigChar(charName)
      pool.put(charName, bc)
    }

    return bc
  }
}
object BigCharFactory {
  def getInstance(): BigCharFactory = return new BigCharFactory()
}