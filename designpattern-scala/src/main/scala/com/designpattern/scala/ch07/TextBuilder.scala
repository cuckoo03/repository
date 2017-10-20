package com.designpattern.scala.ch07

import scala.collection.mutable.ArrayBuffer

/**
 * @author cuckoo03
 */
class TextBuilder extends Builder {
  private val buffer:StringBuffer = new StringBuffer

  def getResult(): String = {
    buffer.append("-getResult")
    return buffer.toString()
  }

  def makeItems(items: ArrayBuffer[String]): Unit = {
    buffer.append("-makeItems start\n")
    for (i<- 0 until items.size) {
      buffer.append(items(i)+"\n")
    }
    buffer.append("-makeItems end\n")
  }

  def makeString(str: String): Unit = {
    buffer.append("-makeString\n")
    buffer.append(s"$str\n")
    buffer.append("\n")
  }

  def makeTitle(title: String): Unit = {
    buffer.append("-title\n")
    buffer.append(s"$title\n")
    buffer.append("\n")
  }
}