package com.designpattern.scala.ch12

/**
 * @author cuckoo03
 */
class UpDownBorder(_display: Display, _borderChar: Char)
    extends Border(_display) {
  private val borderChar = _borderChar

  def getColumns(): Int = {
    return display.getColumns() + 1
  }

  def getRows(): Int = {
    return 1 + display.getRows() + 1
  }

  def getRowText(row: Int): String = {
    if (row == 0) {
      return makeLine(borderChar, display.getColumns())
    } else if (row == getRows() - 1) {
      return makeLine(borderChar, getColumns())
    } else {
      return display.getRowText(row - 1)
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