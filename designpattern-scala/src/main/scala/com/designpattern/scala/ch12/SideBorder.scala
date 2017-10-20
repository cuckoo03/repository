package com.designpattern.scala.ch12

/**
 * @author cuckoo03
 */
class SideBorder(_display:Display, _char:Char) extends Border(_display) {
  protected var borderChar: Char = _char 

  def getColumns(): Int = {
    return display.getColumns() + 1
  }
  
  def getRows(): Int = {
    return display.getRows()
  }

  def getRowText(row: Int): String = {
    return borderChar + display.getRowText(row) + borderChar
  }
}