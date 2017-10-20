package com.designpattern.scala.ch08.factory

import scala.collection.mutable.ListBuffer

/**
 * @author cuckoo03
 */
abstract class Tray(caption: String) extends Item(caption) {
  protected val tray = new ListBuffer[Item]()
  def add(item: Item): Unit = {
    tray += item
  }
}