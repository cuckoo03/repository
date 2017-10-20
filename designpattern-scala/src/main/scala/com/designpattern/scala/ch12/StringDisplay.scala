package com.designpattern.scala.ch12

/**
 * @author cuckoo03
 */
class StringDisplay(_str: String) extends Display {
  private val str = _str

  def getColumns(): Int = {
    return str.getBytes.length
  }

  def getRows(): Int = {
    return 1
  }

  def getRowText(row: Int): String = {
    if (row == 0) {
      return str
    } else {
      return null
    }
  }
}