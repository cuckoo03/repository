package com.designpattern.scala.ch12

/**
 * @author cuckoo03
 */
abstract class Display {
  def getColumns(): Int
  def getRows(): Int
  def getRowText(row: Int): String
  final def show(): Unit = {
    for (i <- 0 until getRows()) {
      println(getRowText(i))
    }
  }
}