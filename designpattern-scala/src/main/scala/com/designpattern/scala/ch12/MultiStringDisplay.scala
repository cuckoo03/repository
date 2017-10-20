package com.designpattern.scala.ch12

import scala.collection.mutable.ListBuffer

/**
 * @author cuckoo03
 */
class MultiStringDisplay extends Display {
  private val strList = new ListBuffer[String]()

  def getColumns(): Int = {
    val maxStr = strList.maxBy { x => x.size }
    return maxStr.size
  }

  def getRows(): Int = {
    return strList.length
  }

  def getRowText(row: Int): String = {
    return strList(row)
  }

  def add(str: String): Unit = {
    strList += str
  }
}