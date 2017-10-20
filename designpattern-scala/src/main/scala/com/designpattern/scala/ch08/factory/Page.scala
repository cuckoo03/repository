package com.designpattern.scala.ch08.factory

import scala.collection.mutable.ListBuffer

/**
 * @author cuckoo03
 */
abstract class Page(_title: String, _author: String) {
  protected var title: String = _title
  protected var author: String = _author
  protected var content = new ListBuffer[Item]()
  def add(item: Item): Unit = {
    content += item
  }
  def output(): Unit = {
    println(makeHTML())
  }
  def makeHTML():String = {
    throw new Exception("error")
  }
}