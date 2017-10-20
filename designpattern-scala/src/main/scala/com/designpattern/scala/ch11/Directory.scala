package com.designpattern.scala.ch11

import scala.collection.mutable.ListBuffer

/**
 * @author cuckoo03
 */
class Directory(val _name: String) extends Entry {
  private val name = _name
  private val directory = new ListBuffer[Entry]()

  def getName(): String = {
    return name
  }

  def getSize(): Int = {
    var size = 0
    val it = directory.iterator
    while (it.hasNext) {
      val entry = it.next()
      size += entry.getSize()
    }

    return size
  }

  @Override
  override def add(entry: Entry): Entry = {
    directory += entry
    return this
  }

  @Override
  override def printList(prefix: String): Unit = {
    println(prefix + "-" + this)
    val it = directory.iterator
    while (it.hasNext) {
      val entry = it.next()
      entry.printList(prefix + "|" + name)
    }
  }

}