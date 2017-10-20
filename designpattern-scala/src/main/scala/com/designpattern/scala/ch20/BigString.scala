package com.designpattern.scala.ch20

import scala.collection.mutable.ArrayBuffer

class BigString(_string: String) {
  private val bigChars = new ArrayBuffer[BigChar]()
  val factory = BigCharFactory.getInstance()
  
  for (i <- 0 until _string.length()) {
    bigChars += factory.getBigChar(_string.charAt(i).toString())
  }

  def print(): Unit = {
    bigChars.foreach { x =>
      println(x)
    }
  }
}