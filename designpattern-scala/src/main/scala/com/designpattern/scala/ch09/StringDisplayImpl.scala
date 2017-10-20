package com.designpattern.scala.ch09

/**
 * @author cuckoo03
 */
class StringDisplayImpl(_str: String) extends DisplayImpl {
  private val str = _str
  private var width = _str.getBytes.length
  def rawOpen(): Unit = {
    printLine()
  }

  def rawPrint(): Unit = {
    println(str)
  }

  def rawClose(): Unit = {
    printLine()
  }

  def printLine(): Unit = {
    print("+")
    for (i <- 0 to width) {
      print("-")
    }
    println("+")
  }
}