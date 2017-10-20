package com.designpattern.scala.ch13

import scala.collection.mutable.ListBuffer

/**
 * @author cuckoo03
 */
class Directory(_name: String) extends Entry {
  private val name = _name
  private val dir = new ListBuffer[Entry]()

  def getName(): String = {
    return name
  }

  def getSize(): Int = {
    var size = 0
    val it = dir.iterator
    while (it.hasNext) {
      val entry = it.next()
      size += entry.getSize()
    }
    return size
  }

  def accept(v: Visitor): Unit = {
    v.visit(this)
  }
  override def add(entry: Entry): Entry = {
    dir += entry
    return entry
  }

  override def iterator(): Iterator[Entry] = {
    return dir.iterator
  }
}