package com.designpattern.scala.ch03

/**
 * @author cuckoo03
 */
class StringDisplay(var str: String) extends AbstractDisplay {
  private val width: Int = str.getBytes.length
  override def open(): Unit = printLine()
  override def print(): Unit = println(s"|$str|")
  override def close(): Unit = printLine()
  private def printLine(): Unit = {
    Predef.print("+")
    for (i <- 0 until width) {
      printf("-")
    }
    Predef.println("+")
  }
}