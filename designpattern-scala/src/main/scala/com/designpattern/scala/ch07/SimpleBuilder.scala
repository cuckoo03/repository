package com.designpattern.scala.ch07

import scala.collection.mutable.ArrayBuffer

/**
 * @author cuckoo03
 */
class SimpleBuilder extends Builder {
  def makeTitle(title: String): Unit = {
    println("title")
  }

  def makeString(str: String): Unit = {
    println("string")
  }

  def makeItems(items: ArrayBuffer[String]): Unit = {
    println("items")
  }

  def getResult(): String = {
    return "result"
  }
}