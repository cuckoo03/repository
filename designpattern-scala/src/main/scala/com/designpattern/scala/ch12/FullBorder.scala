package com.designpattern.scala.ch12

/**
 * @author cuckoo03
 */
class FullBorder(_display: Display) extends Border(_display) {
  def getColumns(): Int = {
    return 1 + display.getColumns() + 1
  }

  def getRows(): Int = {
    return 1 + display.getRows() + 1
  }

  def getRowText(row: Int): String = {
    if (row == 0) {
      return "+" + makeLine('-', display.getColumns()) + "+"
    } else if (row == display.getRows() + 1) {
      return "+" + makeLine('-', display.getColumns()) + "+"
    } else {
      return "|" + display.getRowText(row - 1) + "|"
    }
  }

  private def makeLine(ch: Char, count: Int): String = {
    val buf = new StringBuilder()
    for (i <- 0 until count) {
      buf += ch
    }
    return buf.toString()
  }
}