package com.designpattern.scala.ch08.listfactory

import com.designpattern.scala.ch08.factory.Item
import scala.collection.mutable.ListBuffer

/**
 * @author cuckoo03
 */
abstract class Page(_title: String, _author: String) {
  protected val title = _title
  protected val author = _author
  protected val content = new ListBuffer[Item]
  def add(item: Item): Unit = {
    content += item
  }
  def output(): Unit = {
    println(this.makeHTML())
  }
  def makeHTML(): String = {
    return ""
  }
}